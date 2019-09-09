package com.meirenmeitu.library.refresh

import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import android.widget.FrameLayout
import androidx.annotation.Size
import androidx.core.view.*
import com.meirenmeitu.library.R


/**
 * https://github.com/qstumn/RefreshLayout
 * https://blog.csdn.net/tyk0910/article/details/54907245
 * https://blog.csdn.net/x87648510/article/details/51882040
 * @author chqiu
 * Email:qstumn@163.com
 * Jooyer修改:
 * 适配 CoordinatorLayout
 */
class JRefreshLayout(context: Context, attrs: AttributeSet?) :
    FrameLayout(context, attrs), NestedScrollingChild, NestedScrollingParent {

    private var mHandler: RefreshHandler? = null
    private var mHeaderView: IHeaderWrapper? = null
    private var mFooterView: IFooterWrapper? = null
    private var mTarget: View? = null
    private var mResistance: Float = 0.toFloat()
    private var mLoadMoreEnable: Boolean = true
    private var mTouchY: Float = 0.toFloat()
    private var mMovedDisY: Int = 0
    private var mRefreshDis: Float = 0.toFloat()
    private var mMode: Int = 0

    private var mHasMoreData = true

    private var mNestedChildHelper = NestedScrollingChildHelper(this)
    private var mNestedParentHelper = NestedScrollingParentHelper(this)
    private var mParentOffsetInWindow = IntArray(2)

    private val parentConsumed = IntArray(2)

    private var mTouchSlop: Int = 0

    /**
     * 当刷新时, 用户再次滑动界面, 恰巧此时刷新完成用户上拉会出现bug
     */
    private var mTouching = false

    // 刷新锁,防止同时回调多个刷新操作
    private var mRefreshingOrLoading: Boolean = false
    // 加载或者刷新完成
    private var mRefreshedOrLoaded: Boolean = false

    private val mResetRefreshOrLoadState = Runnable {
        mRefreshingOrLoading = false
    }

    private val isNestedScrollInProgress: Boolean
        get() = isNestedScrollingEnabled

    init {
        mTouchSlop = ViewConfiguration.get(getContext()).scaledTouchSlop
        mNestedChildHelper = NestedScrollingChildHelper(this)
        mNestedParentHelper = NestedScrollingParentHelper(this)
        mParentOffsetInWindow = IntArray(2)
        initAttrs(context, attrs)
    }

    private fun initAttrs(context: Context, attrs: AttributeSet?) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.JRefreshLayout)
        val resistance = typedArray.getFloat(R.styleable.JRefreshLayout_resistance, 0.65f)
        setResistance(resistance)
        typedArray.recycle()
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        if (childCount > 1) {
            throw IllegalStateException("JRefreshLayout can only have one child")
        }
        mTarget = getChildAt(0)
        mTarget!!.isClickable = true
        var targetNestedScrollingEnabled = false
        if (mTarget is NestedScrollingChild) {
            targetNestedScrollingEnabled = (mTarget as NestedScrollingChild).isNestedScrollingEnabled
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            targetNestedScrollingEnabled = mTarget!!.isNestedScrollingEnabled
        }
        isNestedScrollingEnabled = targetNestedScrollingEnabled
        val params = mTarget!!.layoutParams
        params.width = LayoutParams.MATCH_PARENT
        params.height = LayoutParams.MATCH_PARENT
        mTarget!!.layoutParams = params
        if (mHeaderView == null) {
            setHeaderView(DefaultHeaderView(context))
        }
        if (mFooterView == null) {
            setFooterView(DefaultFooterView(context))
        }
    }

    //////////////////////////////////////////  NestedScrollingParent  //////////////////////////////////////////////

    override fun onStartNestedScroll(child: View, target: View, nestedScrollAxes: Int): Boolean {
        return isNestedScrollingEnabled && isEnabled &&
                nestedScrollAxes and ViewCompat.SCROLL_AXIS_VERTICAL != 0
    }

    override fun onNestedScrollAccepted(child: View, target: View, axes: Int) {
        mNestedParentHelper.onNestedScrollAccepted(child, target, axes)
        startNestedScroll(axes and ViewCompat.SCROLL_AXIS_VERTICAL)
        mMovedDisY = 0
    }

    override fun onStopNestedScroll(child: View) {
        mNestedParentHelper.onStopNestedScroll(child)
        onPointerUp()
        mMovedDisY = 0
        stopNestedScroll()
    }

    /**
     * @param target
     * @param dx
     * @param dy       --> dy < 0 ,界面往下滑动
     * @param consumed
     */
    override fun onNestedPreScroll(target: View, dx: Int, dy: Int, consumed: IntArray) {
        // PS: mMovedDisY = 0 ,要么没有出发滑动,要么正在下拉刷新或者上拉加载

        // dy > 0 则表示往上滑动
        // mMode == MODE_REFRESH 表示此时想下拉刷新
        // mMovedDisY > 0 || !mHeaderView!!.canTargetScroll() ,
        // mHeaderView!!.canTargetScroll() 判断是否可以滑动, 根据其 height > 0 返回 false 反之返回 true
        // 1). mMovedDisY > 0 表明此时用户没有松手,因为一旦松手再次下滑 mMovedDisY = 0
        // 2). mMovedDisY = 0 表明此时 1> 用户刚开始下拉, 2>正在刷新
        if (!RefreshAnimUtil.mAnimRunning) {
            if (dy > 0 && (mMovedDisY > 0 || !mHeaderView!!.canTargetScroll()) && mMode == MODE_REFRESH) {
                // 1).如果 mMovedDisY = 0 , consumed[1] = dy - mMovedDisY 相当于消耗所有的滑动
                // 2). 如果 mMovedDisY > 0 , 则表示用户下拉后往上滑动, consumed[1] = dy - mMovedDisY 相当于 JRefreshLayout 先消费多余的滑动
                if (dy > mMovedDisY) {
                    consumed[1] = dy - mMovedDisY  // 把消费的距离放进去
                } else {
                    // 消费掉所有的
                    consumed[1] = dy
                }
                // 上面这个判断就是为了用户下拉后往上滑动将 HeaderView 慢慢隐藏
                handleHeaderScroll((-dy).toFloat())
            } else if (mLoadMoreEnable && dy < 0 && (mMovedDisY > 0 || !mFooterView!!.canTargetScroll()) && mMode == MODE_LOADMORE) {
                if (Math.abs(dy) > mMovedDisY) {
                    consumed[1] = dy + mMovedDisY
                } else {
                    consumed[1] = dy
                }
                println("handleFooterScroll============= 1")
                handleFooterScroll((-dy).toFloat())
            }
        }

        // 当前 View 将消耗后剩余的 dy 传递给 父View(支持嵌套滑动)
        if (dispatchNestedPreScroll(dx, dy - consumed[1], parentConsumed, null)) {
            consumed[1] += parentConsumed[1] // 将 当前View 和 父View 消耗的滑动累加
//            println("onNestedPreScroll=============consumed[1]: ${consumed[1]} , dy: $dy")
        }
    }

    // 参数target: ViewParent包含触发嵌套滚动的view的对象
    // 参数dxConsumed: 表示target已经消费的x方向的距离
    // 参数dyConsumed: 表示target已经消费的x方向的距离
    // 参数dxUnconsumed: 表示x方向剩下的滑动距离
    // 参数dyUnconsumed: 表示y方向剩下的滑动距离
    override fun onNestedScroll(target: View, dxConsumed: Int, dyConsumed: Int, dxUnconsumed: Int, dyUnconsumed: Int) {
//        println("onNestedScroll=============dyUnconsumed: " + dyUnconsumed + " =======mParentOffsetInWindow[1]: " + mParentOffsetInWindow[1])
        // 先将滑动事件转发给 父View
        dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, mParentOffsetInWindow)
        // 父View 未消耗的滑动距离 + 当前 View 在父布局中移动的距离(理解为 父View 移动时携带子View也移动了)
        val dy = dyUnconsumed + mParentOffsetInWindow[1]
        if (!RefreshAnimUtil.mAnimRunning) {
            if (dy < 0 && !targetCanScrollUp()) { // 往下滑动,且 target 不能往下滑动了
                mMode = MODE_REFRESH
                handleHeaderScroll((-dy).toFloat())
            } else if (mLoadMoreEnable && dy > 0 && !targetCanScrollDown()) { // 往上滑动,且 target 不能往上滑动了
                mMode = MODE_LOADMORE
                handleFooterScroll((-dy).toFloat())
                println("handleFooterScroll============= 2")
            } else if (mHasMoreData) {
                println("handleFooterScroll============= 3")
                handleFooterScroll(0F)
            }
        }
    }

    override fun onNestedFling(target: View, velocityX: Float, velocityY: Float, consumed: Boolean): Boolean {
        return dispatchNestedFling(velocityX, velocityY, consumed)
    }

    override fun onNestedPreFling(target: View, velocityX: Float, velocityY: Float): Boolean {
        //        return flingAndDispatch(velocityX, velocityY);
        return dispatchNestedPreFling(velocityX, velocityY)
    }

    private fun flingAndDispatch(velocityX: Float, velocityY: Float): Boolean {
        val consumed = dispatchNestedPreFling(velocityX, velocityY)
        return if (!consumed) {
            consumeFling(velocityY)
        } else true
    }

    private fun consumeFling(velocityY: Float): Boolean {
        var consumeFling = false
        if (mMode == MODE_REFRESH) {
            consumeFling = velocityY > 0 && !targetCanScrollUp()
        } else if (mMode == MODE_LOADMORE) {
            consumeFling = velocityY < 0 && !targetCanScrollDown()
        }
        return consumeFling
    }

    override fun getNestedScrollAxes(): Int {
        return mNestedParentHelper.nestedScrollAxes
    }

    //////////////////////////////////////////  NestedScrollingParent  //////////////////////////////////////////////


    //////////////////////////////////////////  NestedScrollingChild  //////////////////////////////////////////////

    override fun setNestedScrollingEnabled(enabled: Boolean) {
        mNestedChildHelper.isNestedScrollingEnabled = enabled
    }

    override fun isNestedScrollingEnabled(): Boolean {
        return mNestedChildHelper.isNestedScrollingEnabled
    }

    override fun startNestedScroll(axes: Int): Boolean {
        return mNestedChildHelper.startNestedScroll(axes)
    }

    override fun stopNestedScroll() {
        mNestedChildHelper.stopNestedScroll()
    }

    override fun hasNestedScrollingParent(): Boolean {
        return mNestedChildHelper.hasNestedScrollingParent()
    }

    // 参数dxConsumed: 表示view消费了x方向的距离长度
    // 参数dyConsumed: 表示view消费了y方向的距离长度
    // 参数dxUnconsumed: 表示滚动产生的x滚动距离还剩下多少没有消费
    // 参数dyUnconsumed: 表示滚动产生的y滚动距离还剩下多少没有消费
    // 参数offsetInWindow: 支持嵌套滑动的 父View 消费滑动事件后,导致 本View 的移动距离
    override fun dispatchNestedScroll(
        dxConsumed: Int, dyConsumed: Int, dxUnconsumed: Int, dyUnconsumed: Int,
        @Size(value = 2) offsetInWindow: IntArray?
    ): Boolean {
        return mNestedChildHelper.dispatchNestedScroll(
            dxConsumed,
            dyConsumed,
            dxUnconsumed,
            dyUnconsumed,
            offsetInWindow
        )
    }

    // 参数dx: 表示view本次x方向的滚动的总距离长度
    // 参数dy: 表示view本次y方向的滚动的总距离长度
    // 参数consumed: 表示父布局消费的距离,consumed[0]表示x方向,consumed[1]表示y方向
    // 参数offsetInWindow: 表示剩下的距离dxUnconsumed和dyUnconsumed使得view在父布局中的位置偏移了多少
    override fun dispatchNestedPreScroll(
        dx: Int,
        dy: Int, @Size(value = 2) consumed: IntArray?, @Size(value = 2) offsetInWindow: IntArray?
    ): Boolean {
        return mNestedChildHelper.dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow)
    }

    override fun dispatchNestedFling(velocityX: Float, velocityY: Float, consumed: Boolean): Boolean {
        return mNestedChildHelper.dispatchNestedFling(velocityX, velocityY, consumed)
    }

    override fun dispatchNestedPreFling(velocityX: Float, velocityY: Float): Boolean {
        return mNestedChildHelper.dispatchNestedPreFling(velocityX, velocityY)
    }

    //////////////////////////////////////////  NestedScrollingChild  //////////////////////////////////////////////


    override fun dispatchTouchEvent(event: MotionEvent): Boolean {

        if (MotionEvent.ACTION_UP == event.actionMasked || MotionEvent.ACTION_CANCEL == event.actionMasked) {
            mTouching = false
            if (mRefreshedOrLoaded) {
                mRefreshingOrLoading = false
            }
        } else if (MotionEvent.ACTION_DOWN == event.actionMasked) {
            mTouching = true
        }

        if (isNestedScrollInProgress) {
            return super.dispatchTouchEvent(event)
        }
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                mTouchY = event.y
                mMovedDisY = 0
            }
            MotionEvent.ACTION_MOVE -> {
                val currY = event.y
                val dy = currY - mTouchY
                mTouchY = currY
                if (dy > 0 && !targetCanScrollUp() && mFooterView!!.canTargetScroll()) {
                    mMode = MODE_REFRESH
                } else if (dy < 0 && mLoadMoreEnable && !targetCanScrollDown()
                    && mHeaderView!!.canTargetScroll()
                ) {
                    mMode = MODE_LOADMORE
                }
                handleScroll(dy)
                if (mMode == MODE_REFRESH && !mHeaderView!!.canTargetScroll() || mMode == MODE_LOADMORE && !mFooterView!!.canTargetScroll()) {
                    return true
                }
            }
            MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP -> {
                val slop = mTouchSlop / 2
                //if mMovedDisY < mTouchSlop this event is targetView do onClick
                if (mMovedDisY > (if (slop > 0) slop else mTouchSlop)) {
                    onPointerUp()
                    cancelPressed(mTarget, event)
                    return true
                }
            }
        }
        return super.dispatchTouchEvent(event)
    }

    private fun cancelPressed(view: View?, event: MotionEvent) {
        val obtain = MotionEvent.obtain(event)
        obtain.action = MotionEvent.ACTION_CANCEL
        view!!.dispatchTouchEvent(obtain)
    }


    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mRefreshDis = (h / 6).toFloat()
    }


    private fun onPointerUp() {
        if (mMode == MODE_REFRESH) {
            onRefreshPointerUp()
        } else if (mMode == MODE_LOADMORE) {
            onLoadPointUp()
        } else { // 头尾都 重置
//            mHeaderView!!.onPrepare(mTarget!!, 0)
//            mFooterView!!.onPrepare(mTarget!!, 0)
        }
    }

    /**
     * 松开手指后
     */
    private fun onRefreshPointerUp() {
        if (mHeaderView!!.getState() != IHeaderWrapper.STATE.REFRESH) {
            if (mMovedDisY >= mRefreshDis) {
                if (mRefreshingOrLoading && mFooterView!!.getState() == IFooterWrapper.STATE.REFRESH) {
                    mHeaderView!!.updateState(IHeaderWrapper.STATE.LOADING)
                    mHeaderView!!.onPrepare(mTarget!!, 0)
                } else {
                    mHeaderView!!.updateState(IHeaderWrapper.STATE.REFRESH)
                    mHeaderView!!.onRefreshBegin(mTarget!!)
                }

                if (!mRefreshingOrLoading && mHandler != null) {
                    mRefreshingOrLoading = true
                    mRefreshedOrLoaded = false
                    mRefreshOrLoadStartTime = System.currentTimeMillis()
                    mHandler!!.onRefresh(this)

                    // 重置加载更多没有数据,即下拉时设置为有,加载完成后再根据数据设置
                    mFooterView!!.updateState(IFooterWrapper.STATE.START)
                }
            } else if (mMovedDisY < mRefreshDis) {
                mHeaderView!!.onPrepare(mTarget!!, 0)
            }
        } else { // 此时头部在刷新,底部应该重置
            if (targetCanScrollDown()) { // 没有达到底部
                if (mHasMoreData) {
                    mFooterView!!.updateState(IFooterWrapper.STATE.START)
                } else {
                    mFooterView!!.updateState(IFooterWrapper.STATE.NOTHING)
                }
                mFooterView!!.onPrepare(mTarget!!, 0)
            }
        }
    }

    /**
     * 松开手指后
     */
    private fun onLoadPointUp() {
        if (mFooterView!!.getState() != IFooterWrapper.STATE.REFRESH) {
            if (mMovedDisY >= mRefreshDis) {
                if (mRefreshingOrLoading) { // 头部正在刷新
                    if (mHasMoreData && mHeaderView!!.getState() == IHeaderWrapper.STATE.REFRESH) {
                        println("onLoadPointUp============= 1")
                        mFooterView!!.updateState(IFooterWrapper.STATE.LOADING)
                        mFooterView!!.onPrepare(mTarget!!, -1)
                    } else {
                        println("onLoadPointUp============= 2")
                        mFooterView!!.updateState(IFooterWrapper.STATE.NOTHING)
                        mFooterView!!.onPrepare(mTarget!!, -1)
                    }
                } else {
                    if (mHasMoreData) {
                        println("onLoadPointUp============= 3")
                        mFooterView!!.updateState(IFooterWrapper.STATE.REFRESH)
                        mFooterView!!.onRefreshBegin(mTarget!!)
                    } else {
                        println("onLoadPointUp============= 4")
                        mFooterView!!.updateState(IFooterWrapper.STATE.NOTHING)
                        mFooterView!!.onNoMoreData(mTarget!!)
                    }
                }

                if (!mRefreshingOrLoading && mHandler != null && mHasMoreData) {
                    println("onLoadPointUp============= 5")
                    mRefreshingOrLoading = true
                    mRefreshedOrLoaded = false
                    mRefreshOrLoadStartTime = System.currentTimeMillis()
                    mHandler!!.onLoadMore(this)
                }
            } else if (mMovedDisY < mRefreshDis) {
                if (mHasMoreData) {
                    println("onLoadPointUp============= 6")
                    mFooterView!!.onPrepare(mTarget!!, 1)
                } else {
                    println("onLoadPointUp============= 7")
                    mFooterView!!.onNoMoreData(mTarget!!)
                }
            }
        }
    }

    private fun handleScroll(dy: Float) {
        if (mMode == MODE_REFRESH) {
            handleHeaderScroll(dy)
        } else if (mMode == MODE_LOADMORE) {
            handleFooterScroll(dy)
        }
    }

    private fun handleHeaderScroll(offsetY: Float) {
        var dy = offsetY
        if (mRefreshingOrLoading && mFooterView!!.getState() == IFooterWrapper.STATE.REFRESH) {
            mHeaderView!!.updateState(IHeaderWrapper.STATE.LOADING)
        }
        if (!targetCanScrollUp() && dy > 0) {
            var dragIndex = Math.exp((-(mMovedDisY / mResistance)).toDouble())
            if (dragIndex < 0) dragIndex = 0.0
            dy = (dy * dragIndex).toFloat()
            mMovedDisY += dy.toInt()
            mHeaderView!!.onPulling(dy, mTarget!!)
            if (mMovedDisY >= mRefreshDis && mHeaderView!!.getState() != IHeaderWrapper.STATE.REFRESH
                && mHeaderView!!.getState() != IHeaderWrapper.STATE.PULL
            ) {
                if (mRefreshingOrLoading && mFooterView!!.getState() == IFooterWrapper.STATE.REFRESH) {
                    mHeaderView!!.updateState(IHeaderWrapper.STATE.LOADING)
                } else {
                    mHeaderView!!.updateState(IHeaderWrapper.STATE.PULL)
                }

            }
        } else if (!targetCanScrollUp() && dy < 0) {
            mMovedDisY += dy.toInt()
            mHeaderView!!.onPulling(dy, mTarget!!)
            if (mMovedDisY < 0) mMovedDisY = 0
            if (mMovedDisY < mRefreshDis && mHeaderView!!.getState() != IHeaderWrapper.STATE.REFRESH
                && mHeaderView!!.getState() != IHeaderWrapper.STATE.START
            ) {
                if (mRefreshingOrLoading) {
                    mHeaderView!!.updateState(IHeaderWrapper.STATE.LOADING)
                } else {
                    mHeaderView!!.updateState(IHeaderWrapper.STATE.START)
                }
            }
        }
    }

    /**
     * @param offsetY --> offsetY < 0, 界面向上滑动
     */
    private fun handleFooterScroll(offsetY: Float) {
        var dy = offsetY
        if (mRefreshingOrLoading && mHasMoreData && mHeaderView!!.getState() == IHeaderWrapper.STATE.REFRESH) {
            println("handleFooterScroll============= 4")
            mFooterView!!.updateState(IFooterWrapper.STATE.LOADING)
        }
        if (dy < 0 && !targetCanScrollDown()) { // Target 不能向上滑动了
            dy = Math.abs(dy)
            var dragIndex = Math.exp((-(mMovedDisY / mResistance)).toDouble())
            if (dragIndex < 0) dragIndex = 0.0
            dy = (dy * dragIndex).toFloat()
            mMovedDisY += dy.toInt()
            mFooterView!!.onPulling(dy, mTarget!!)
            if (mMovedDisY >= mRefreshDis && mFooterView!!.getState() != IFooterWrapper.STATE.REFRESH
                && mFooterView!!.getState() != IFooterWrapper.STATE.PULL && mHasMoreData
            ) {
                if (mRefreshingOrLoading && mHeaderView!!.getState() == IHeaderWrapper.STATE.REFRESH) {
                    println("handleFooterScroll============= 5")
                    mFooterView!!.updateState(IFooterWrapper.STATE.LOADING)
                } else {
                    println("handleFooterScroll============= 6 --> mHasMoreData: $mHasMoreData")
                    mFooterView!!.updateState(IFooterWrapper.STATE.PULL)
                }
            }
        } else if (dy > 0 && !targetCanScrollDown()) {
            mFooterView!!.onPulling(-dy, mTarget!!)
            mMovedDisY -= dy.toInt()
            if (mMovedDisY < 0) mMovedDisY = 0
            if (mMovedDisY < mRefreshDis && mFooterView!!.getState() != IFooterWrapper.STATE.REFRESH
                && mFooterView!!.getState() != IFooterWrapper.STATE.START && mHasMoreData
            ) {
                if (mRefreshingOrLoading) {
                    println("handleFooterScroll============= 7")
                    mFooterView!!.updateState(IFooterWrapper.STATE.LOADING)
                } else {
                    println("handleFooterScroll============= 8")
                    mFooterView!!.updateState(IFooterWrapper.STATE.START)
                }
            }
        } else { // 此时处理没有数据情况
            println("handleFooterScroll============= 9")
            mFooterView!!.onPulling(0F, mTarget!!)
            mMovedDisY = 0
        }
    }


    private fun targetCanScrollUp(): Boolean {
        return if (mTarget == null) false else mTarget!!.canScrollVertically(-1)
    }

    private fun targetCanScrollDown(): Boolean {
        return if (mTarget == null) false else mTarget!!.canScrollVertically(1)
    }

    fun setHeaderView(view: IHeaderWrapper) {
        if (view != mHeaderView) {
            if (null != mHeaderView) {
                removeView(mHeaderView!!.getHeaderView())
            }
            view.addToRefreshLayout(this)
        }
        mHeaderView = view
    }

    fun setFooterView(view: IFooterWrapper) {
        if (view != mFooterView) {
            if (null != mFooterView) {
                removeView(mFooterView!!.getFooterView())
            }
            view.addToRefreshLayout(this)
        }
        mFooterView = view
    }

    fun setRefreshHandler(handler: RefreshHandler) {
        mHandler = handler
    }


    private val mRefreshOrLoadTimeOffsetTime = 3000L

    private val mAutoRefresh = Runnable {
        mHandler!!.onRefresh(this)
    }

    private val mResetRefresh = Runnable {
        mMode = 0
        mHeaderView!!.updateState(IHeaderWrapper.STATE.COMPLETE)
        mHeaderView!!.onPrepare(mTarget!!, 2)
    }

    private val mResetLoad = Runnable {
        mMode = 0
        mFooterView!!.updateState(IFooterWrapper.STATE.COMPLETE)
        mFooterView!!.onPrepare(mTarget!!, 2)
    }

    private var mRefreshOrLoadStartTime = 0L

    fun refreshComplete() {
        val offsetTime = System.currentTimeMillis() - mRefreshOrLoadStartTime
        if (offsetTime >= mRefreshOrLoadTimeOffsetTime) {
            mMode = 0
            mHeaderView!!.updateState(IHeaderWrapper.STATE.COMPLETE)
            mHeaderView!!.onPrepare(mTarget!!, 2)
        } else {
            postDelayed(mResetRefresh, mRefreshOrLoadTimeOffsetTime - offsetTime)
        }

//        if (!mTouching){ // 此时刷新/加载完成,用户手机没有抬起接着滑动,会有bug
//            postDelayed(mResetRefreshOrLoadState, 500)
//        }else{
//            mRefreshedOrLoaded = true
//        }
    }

    fun loadMoreComplete() {
        val offsetTime = System.currentTimeMillis() - mRefreshOrLoadStartTime
        if (offsetTime >= mRefreshOrLoadTimeOffsetTime) {
            mMode = 0
            mHeaderView!!.updateState(IHeaderWrapper.STATE.COMPLETE)
            mHeaderView!!.onPrepare(mTarget!!, 2)
        } else {
            postDelayed(mResetLoad, mRefreshOrLoadTimeOffsetTime - offsetTime)
        }

//        mMode = 0
//        mFooterView!!.updateState(IFooterWrapper.STATE.COMPLETE)
//        mFooterView!!.onPrepare(mTarget!!, 2)
//        if (!mTouching) { // 此时刷新/加载完成,用户手机没有抬起接着滑动,会有bug
//            postDelayed(mResetRefreshOrLoadState, 500)
//        } else {
//            mRefreshedOrLoaded = true
//        }
    }

    fun autoRefresh() {
        mRefreshOrLoadStartTime = System.currentTimeMillis()
        mMode = MODE_REFRESH
        mRefreshingOrLoading = true
        mRefreshedOrLoaded = false
        mHeaderView!!.updateState(IHeaderWrapper.STATE.REFRESH)
        mHeaderView!!.onAutoRefresh(mTarget!!)
        postDelayed(mAutoRefresh, 1200)
    }

    fun setNoMoreData() {
        mMode = 0
        mHasMoreData = false
        mRefreshingOrLoading = false
        mFooterView!!.updateState(IFooterWrapper.STATE.NOTHING)
    }

    /**
     * @param resistance range 0 ~ 1f
     */
    private fun setResistance(resistance: Float) {
        if (resistance in 0F..1F) {
            mResistance = 1000F - 1000F * resistance
        }
    }

    fun setLoadMoreEnable(enable: Boolean) {
        mLoadMoreEnable = enable
    }

    companion object {
        val MODE_REFRESH = 1
        val MODE_LOADMORE = 2
    }


}

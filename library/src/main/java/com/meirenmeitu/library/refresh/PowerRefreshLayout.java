package com.meirenmeitu.library.refresh;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.NestedScrollingChild;
import androidx.core.view.NestedScrollingChildHelper;
import androidx.core.view.NestedScrollingParent;
import androidx.core.view.NestedScrollingParentHelper;
import androidx.core.view.ViewCompat;

import org.jetbrains.annotations.NotNull;

/**
 * 参考 https://github.com/lovejjfg/PowerRefresh
 */
@SuppressWarnings("unused")
public class PowerRefreshLayout extends ViewGroup implements NestedScrollingParent, NestedScrollingChild {
    private static final String TAG = PowerRefreshLayout.class.getSimpleName();
    /**
     * 当前默认状态
     */
    private static final int STATE_DEFAULT = -1;
    /**
     * 当前状态为 刷新 (包括所有刷新动作)
     */
    private static final int STATE_REFRESH = 1;
    /**
     * 当前状态为 加载 (包括所有加载动作)
     */
    private static final int STATE_LOAD = 2;
    /**
     * 前状态
     */
    private int currentStatus = STATE_DEFAULT;

    private final NestedScrollingParentHelper mNestedScrollingParentHelper;
    private final NestedScrollingChildHelper mNestedScrollingChildHelper;

    /**
     * 计算父类嵌套滑动消耗值
     */
    private final int[] mParentScrollConsumed = new int[2];
    /**
     * 计算父类偏移量
     */
    private final int[] mParentOffsetInWindow = new int[2];

    @Nullable
    public IHeaderWrapper mHeaderListener;
    @Nullable
    public IFooterWrapper mFooterListener;
    /**
     * HeaderView
     */
    protected View mHeaderView;
    /**
     * FooterView
     */
    protected View mFooterView;
    /**
     * FooterView 高度
     */
    private int mFooterViewHeight;
    /**
     * HeaderView 高度
     */
    private int mHeaderViewHeight;
    /**
     * 下拉消费值
     */
    private int mTotalUnconsumed;
    /**
     * 上拉消费值
     */
    private int mTotalUnconsumedLoadMore;
    /**
     * 底部偏移量(无用)
     */
    public int bottomScroll;
    /**
     * 嵌套的目标 View
     */
    private View mTarget;
    /**
     * 控制 TargetView 嵌套滑动=逻辑
     */
    private OnChildScrollUpCallback mChildScrollUpCallback;
    /**
     * 回调监听
     */
    private OnRefreshAndLoadListener listener;
    /**
     * 记录 HeaderView / FooterView  的状态
     */
    private int mStatus = State.DEFAULT;
    /**
     * 拖拽的衰减系数
     */
    private static final float DRAG_RATE = 0.5f;
    /**
     * 无效手指操作
     */
    private static final int INVALID_POINTER = -1;
    /**
     * 动画执行时间
     */
    public int ANIMATION_DURATION = 300;
    public int ANIMATION_AUTO_REFRESH_DURATION = 500;
    /**
     * 刷新是否成功
     */
    private boolean isRefreshSuccess = false;
    /**
     * 加载是否成功
     */
    private boolean isLoadSuccess = false;
    /**
     * 正在加载
     */
    public boolean isLoading = false;
    /**
     * 正在刷新
     */
    public boolean isRefreshing = false;
    /**
     * 是否自动加载
     */
    private boolean isAutoRefresh = false;
    /**
     * 是否自动刷新
     */
    private boolean isAutoLoad = false;
    /**
     * 加载是否可用
     */
    private boolean loadEnable = true;
    /**
     * 刷新是否可用
     */
    private boolean refreshEnable = true;
    /**
     * 是否有更多数据
     */
    private boolean noMoreData = false;
    /**
     * 是否支持嵌套滑动
     */
    private boolean mNestedScrollInProgress;
    /**
     * 是否在拖拽
     */
    private boolean mIsBeingDragged;
    /**
     * 有效滑动距离阀值
     */
    private float mTouchSlop;
    /**
     * 有效手指ID
     */
    private int mActivePointerId;
    /**
     * 按下时 Y 记录
     */
    private float mInitialDownY;
    /**
     * 移动 Y 距离
     */
    private float mInitialMotionY;
    /**
     * 刷新 Runnable
     */
    private Runnable refreshAction = new Runnable() {
        @Override
        public void run() {
            scrollToDefaultStatus(State.HEADER_COMPLETED);
            isRefreshing = false;
        }
    };
    /**
     * 加载 Runnable
     */
    private Runnable loadAction = new Runnable() {
        @Override
        public void run() {
            scrollToDefaultStatus(State.FOOTER_COMPLETED);
            isLoading = false;
        }
    };


    public PowerRefreshLayout(Context context) {
        this(context, null);
    }

    public PowerRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mNestedScrollingParentHelper = new NestedScrollingParentHelper(this);
        mNestedScrollingChildHelper = new NestedScrollingChildHelper(this);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        setNestedScrollingEnabled(true);
        ensureTarget();

    }

    @Override
    public void setEnabled(boolean enabled) {
        setNestedScrollingEnabled(enabled);
        super.setEnabled(enabled);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ensureTarget();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // 以默认方式测量自身尺寸
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (mTarget == null) {
            ensureTarget();
        }
        if (mTarget == null) {
            return;
        }

        // 测量目标View的尺寸
        mTarget.measure(MeasureSpec.makeMeasureSpec(getMeasuredWidth() - getPaddingLeft() - getPaddingRight(), MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(getMeasuredHeight() - getPaddingTop() - getPaddingBottom(), MeasureSpec.EXACTLY));

        // 测量 HeaderView
        if (null != mHeaderView && null != mHeaderListener) {
            mHeaderView.measure(MeasureSpec.makeMeasureSpec(getMeasuredWidth() - getPaddingLeft() - getPaddingRight(),
                    MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(mHeaderViewHeight, MeasureSpec.EXACTLY));
        }

        // 测量 FooterView
        if (null != mFooterView && null != mFooterListener) {
            mFooterView.measure(MeasureSpec.makeMeasureSpec(getMeasuredWidth() - getPaddingLeft() - getPaddingRight(),
                    MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(mFooterViewHeight, MeasureSpec.EXACTLY));
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int contentHeight = 0;
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        int childLeft = getPaddingLeft();
        int childRight = getPaddingLeft();
        int childTop = getPaddingTop();
        int childWidth = width - childLeft - childRight;
        int childHeight;
        View child;
        if (mHeaderView != null) {
            child = mHeaderView;
            child.layout(childLeft, childTop - mHeaderViewHeight, childLeft + childWidth, childTop);
        }
        //make sure there is the target!
        if (mTarget == null) {
            ensureTarget();
        }
        if (mTarget == null) {
            return;
        }
        child = mTarget;
        childLeft = getPaddingLeft();
        childTop = getPaddingTop();
        childWidth = width - getPaddingLeft() - getPaddingRight();
        childHeight = height - getPaddingTop() - getPaddingBottom();
        child.layout(childLeft, childTop, childLeft + childWidth, childTop + childHeight);
        contentHeight += child.getMeasuredHeight();

        if (mFooterView != null) {
            child = mFooterView;
            child.layout(0, contentHeight, child.getMeasuredWidth(), contentHeight + mFooterViewHeight);
        }
        bottomScroll = contentHeight - getMeasuredHeight();
    }

    private void ensureTarget() {
        if (mTarget == null) {
            for (int i = 0; i < getChildCount(); i++) {
                View child = getChildAt(i);
                if (!child.equals(mHeaderView) || !child.equals(mFooterView)) {
                    mTarget = child;
                    break;
                }
            }
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        ensureTarget();

        final int action = ev.getActionMasked();
        int pointerIndex;


        if (!isEnabled() || isRefreshing || isLoading || canChildScrollUp()
                || mNestedScrollInProgress) {
            // Fail fast if we're not in a state where a swipe is possible
            return false;
        }

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mActivePointerId = ev.getPointerId(0);
                mIsBeingDragged = false;

                pointerIndex = ev.findPointerIndex(mActivePointerId);
                if (pointerIndex < 0) {
                    return false;
                }
                mInitialDownY = ev.getY(pointerIndex);
                break;

            case MotionEvent.ACTION_MOVE:
                if (mActivePointerId == INVALID_POINTER) {
                    return false;
                }

                pointerIndex = ev.findPointerIndex(mActivePointerId);
                if (pointerIndex < 0) {
                    return false;
                }
                final float y = ev.getY(pointerIndex);
                startDragging(y);
                break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                mIsBeingDragged = false;
                mActivePointerId = INVALID_POINTER;
                break;
        }

        return mIsBeingDragged;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        final int action = ev.getActionMasked();
        int pointerIndex;


        if (!isEnabled() || canChildScrollUp()
                || isLoading || isRefreshing || mNestedScrollInProgress) {
            // Fail fast if we're not in a state where a swipe is possible
            return false;
        }

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mActivePointerId = ev.getPointerId(0);
                mIsBeingDragged = false;
                break;

            case MotionEvent.ACTION_MOVE: {
                pointerIndex = ev.findPointerIndex(mActivePointerId);
                if (pointerIndex < 0) {
                    return false;
                }

                final float y = ev.getY(pointerIndex);
                startDragging(y);
                //in this case ,just can refresh.
                if (mIsBeingDragged) {
                    final float overScrollTop = (y - mInitialMotionY);
                    if (overScrollTop < 0 && getScrollY() > 0) {
                        ev.setAction(MotionEvent.ACTION_CANCEL);
                        return false;
                    }
                    currentStatus = STATE_REFRESH;
                    goToRefresh((int) overScrollTop);
                }
                mInitialMotionY = y;
                break;
            }
            case MotionEvent.ACTION_POINTER_DOWN: {
                pointerIndex = ev.getActionMasked();
                if (pointerIndex < 0) {
                    return false;
                }
                mActivePointerId = ev.getPointerId(pointerIndex);
                break;
            }

//            case MotionEventCompat.ACTION_POINTER_UP:
//                onSecondaryPointerUp(ev);
//                break;

            case MotionEvent.ACTION_UP: {
                pointerIndex = ev.findPointerIndex(mActivePointerId);
                if (pointerIndex < 0) {
                    return false;
                }

                if (mIsBeingDragged) {
                    mIsBeingDragged = false;
                    resetScroll();
                }
                mActivePointerId = INVALID_POINTER;
                return false;
            }
            case MotionEvent.ACTION_CANCEL:
                resetScroll();
                return false;
        }

        return true;
    }

    @SuppressLint("NewApi")
    private void startDragging(float y) {
        final float yDiff = y - mInitialDownY;
        if (yDiff > mTouchSlop && !mIsBeingDragged) {
            mInitialMotionY = mInitialDownY + mTouchSlop;
            mIsBeingDragged = true;
        }
    }

    //////////////////////////////////////                         ////////////////////////////////////////
    //////////////////////////////////////  NestedScrollingChild  ////////////////////////////////////////
    //////////////////////////////////////                         ////////////////////////////////////////
    @Override
    public void setNestedScrollingEnabled(boolean enabled) {
        mNestedScrollingChildHelper.setNestedScrollingEnabled(enabled);
    }

    @Override
    public boolean isNestedScrollingEnabled() {
        return mNestedScrollingChildHelper.isNestedScrollingEnabled();
    }

    @Override
    public boolean startNestedScroll(int axes) {
        return mNestedScrollingChildHelper.startNestedScroll(axes);
    }

    @Override
    public void stopNestedScroll() {
        mNestedScrollingChildHelper.stopNestedScroll();
    }

    @Override
    public boolean hasNestedScrollingParent() {
        return mNestedScrollingChildHelper.hasNestedScrollingParent();
    }

    @Override
    public boolean dispatchNestedScroll(int dxConsumed, int dyConsumed, int dxUnconsumed,
                                        int dyUnconsumed, int[] offsetInWindow) {
        return mNestedScrollingChildHelper.dispatchNestedScroll(dxConsumed, dyConsumed,
                dxUnconsumed, dyUnconsumed, offsetInWindow);
    }

    @Override
    public boolean dispatchNestedPreScroll(int dx, int dy, int[] consumed, int[] offsetInWindow) {
        return mNestedScrollingChildHelper.dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow);
    }

    @Override
    public boolean onNestedPreFling(@NotNull View target, float velocityX,
                                    float velocityY) {

        return dispatchNestedPreFling(velocityX, velocityY);
    }


    @Override
    public boolean dispatchNestedFling(float velocityX, float velocityY, boolean consumed) {
        return mNestedScrollingChildHelper.dispatchNestedFling(velocityX, velocityY, consumed);
    }

    @Override
    public boolean dispatchNestedPreFling(float velocityX, float velocityY) {
        return mNestedScrollingChildHelper.dispatchNestedPreFling(velocityX, velocityY);
    }
    //////////////////////////////////////                         ////////////////////////////////////////
    //////////////////////////////////////  NestedScrollingChild  ////////////////////////////////////////
    //////////////////////////////////////                         ////////////////////////////////////////

    //////////////////////////////////////                         ////////////////////////////////////////
    //////////////////////////////////////  NestedScrollingParent  ////////////////////////////////////////
    //////////////////////////////////////                         ////////////////////////////////////////
    @Override
    public int getNestedScrollAxes() {
        return mNestedScrollingParentHelper.getNestedScrollAxes();
    }

    @Override
    public boolean onStartNestedScroll(@NotNull View child, @NotNull View target, int nestedScrollAxes) {
        return (nestedScrollAxes & 2) != 0;
    }

    @Override
    public void onNestedScrollAccepted(@NotNull View child, @NotNull View target, int axes) {
        mNestedScrollingParentHelper.onNestedScrollAccepted(child, target, axes);
        // Dispatch up to the nested parent
        startNestedScroll(axes & ViewCompat.SCROLL_AXIS_VERTICAL);
        mTotalUnconsumed = 0;
        mTotalUnconsumedLoadMore = 0;
        mNestedScrollInProgress = true;
    }


    @Override
    public void onNestedPreScroll(@NotNull View target, int dx, int dy, @NotNull int[] consumed) {
//        Log.e("PowerRefresh", "onNestedPreScroll========= 1 --->dy: " + dy + " ===mTotalUnconsumedLoadMore: " + mTotalUnconsumedLoadMore + " ====bottomScroll: " + bottomScroll + " ===mTotalUnconsumed: " + mTotalUnconsumed + " ===getScrollY: " + getScrollY());
        if (refreshEnable && mHeaderView != null && !isRefreshing && currentStatus == STATE_REFRESH && dy > 0 && mTotalUnconsumed > 0) {
            mTotalUnconsumed -= dy;
            if (mTotalUnconsumed <= 0) {// over
                mTotalUnconsumed = 0;
                dy = (int) (-getScrollY() / DRAG_RATE);
            }
            goToRefresh(-dy);
            consumed[1] = dy;
//            Log.e("PowerRefresh", "onNestedPreScroll========= 2 --->dy: " + dy);
        } else if (refreshEnable && mHeaderView != null && isRefreshing && mTotalUnconsumed == 0 && dy > 0 && getScrollY() < 0) { // 正在刷新时,向上滑动
//            Log.e("PowerRefresh", "onNestedPreScroll========= 3 --->dy: " + dy);
            performScroll(-dy);
            consumed[1] = dy;
        }

        if (loadEnable && !isLoading && mFooterView != null && dy < 0 && getScrollY() >= bottomScroll && mTotalUnconsumedLoadMore > 0 && currentStatus == STATE_LOAD) { // 移动 FooterView ,使其显示
            mTotalUnconsumedLoadMore += dy;
            goToLoad(dy);
            consumed[1] = dy;
//            Log.e("PowerRefresh", "onNestedPreScroll========= 5 --->dy: " + dy);
        } else if (loadEnable && isLoading && mFooterView != null && dy < 0 && getScrollY() > 0) { // 正在加载时,向下滑动
//            Log.e("PowerRefresh", "onNestedPreScroll========= 6 --->dy: " + dy);
            performScroll(-dy);
            consumed[1] = dy;
        }


        final int[] parentConsumed = mParentScrollConsumed;
        if (dispatchNestedPreScroll(dx - consumed[0], dy - consumed[1], parentConsumed, null)) {
            consumed[0] += parentConsumed[0];
            consumed[1] += parentConsumed[1];
        }
    }


    @Override
    public void onNestedScroll(@NotNull View target, int dxConsumed, int dyConsumed,
                               int dxUnconsumed, int dyUnconsumed) {
        // Dispatch up to the nested parent first
        dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed,
                mParentOffsetInWindow);

        final int dy = dyUnconsumed + mParentOffsetInWindow[1];
//        Log.e("PowerRefresh", "onNestedScroll========= 1 --->dy: " + dy + " ====canChildScrollDown: " + canChildScrollDown());
        if (!isLoading && refreshEnable && mHeaderView != null && dy < 0 && !isRefreshing && !canChildScrollUp()) {
            mTotalUnconsumed += Math.abs(dy);
            if (currentStatus == STATE_DEFAULT || mTotalUnconsumed != 0)
                currentStatus = STATE_REFRESH;
            goToRefresh(Math.abs(dy));
//            Log.e("PowerRefresh", "onNestedScroll========= 2 --->dy: " + dy);
        } else if (refreshEnable && mHeaderView != null && dy < 0 && isRefreshing && !canChildScrollUp()) {
            performScroll(-dy);
//            Log.e("PowerRefresh", "onNestedScroll========= 3 --->dy: " + dy);
        }


        if (!isRefreshing && loadEnable && (isAutoLoad || mFooterView != null) && getScrollY() >= bottomScroll && dy > 0 && !isLoading && mTotalUnconsumedLoadMore <= 4 * mFooterViewHeight) {
            mTotalUnconsumedLoadMore += dy;
            if (currentStatus == STATE_DEFAULT || mTotalUnconsumedLoadMore != 0) {
                currentStatus = STATE_LOAD;
            }
            goToLoad(dy);
//            Log.e("PowerRefresh", "onNestedScroll========= 4 --->dy: " + dy);
        } else if (loadEnable && (isAutoLoad || mFooterView != null) && getScrollY() >= bottomScroll && dy > 0 && isLoading && !canChildScrollDown()) { // 正在加载,向上滑动
//            Log.e("PowerRefresh", "onNestedScroll========= 5 --->dy: " + dy);
            performScroll(-dy);
        }

    }

    @Override
    public boolean onNestedFling(@NotNull View target, float velocityX, float velocityY, boolean consumed) {
        return dispatchNestedFling(velocityX, velocityY, consumed);
    }


    @Override
    public void onStopNestedScroll(@NotNull View target) {
        mNestedScrollingParentHelper.onStopNestedScroll(target);
        mNestedScrollInProgress = false;
//        Log.e("PowerRefresh", "onStopNestedScroll========= 1 --->mStatus: " + mStatus + " ====headerHeight: " + mHeaderViewHeight);
        resetScroll();
        stopNestedScroll();
    }

    //////////////////////////////////////                         ////////////////////////////////////////
    //////////////////////////////////////  NestedScrollingParent  ////////////////////////////////////////
    //////////////////////////////////////                         ////////////////////////////////////////

    private void performScroll(int dy) {
        int ddy = (int) (-dy * DRAG_RATE);
        scrollBy(0, ddy);
    }

    /**
     * 松手后的各种动画
     *
     * @param start    --> 开始位置
     * @param end      --> 结束为止
     * @param listener --> 动画进行中 和 动画结束 回调
     */
    private void performAnim(int start, int end, final AnimListener listener) {
        ValueAnimator animator = ValueAnimator.ofInt(start, end);
        animator.setDuration(isAutoRefresh ? ANIMATION_AUTO_REFRESH_DURATION : ANIMATION_DURATION).start();
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();
                scrollTo(0, value);
                postInvalidate();
                if (null != listener) {
                    listener.onGoing();
                }
            }
        });
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if (null != listener) {
                    listener.onEnd();
                }
            }
        });
    }

    /**
     * 当手指抬起时
     */
    private void resetScroll() {
        // 判断本次触摸系列事件结束时,Layout的状态
        switch (mStatus) {
            //下拉刷新
            case State.HEADER_DRAG:
                refreshEnable = false;
                scrollToDefaultStatus(State.HEADER_CANCEL);
                break;
            // 释放刷新
            case State.HEADER_RELEASE:
                scrollToRefreshStatus(true);
                break;
            // 正在刷新
            case State.HEADER_REFRESHING:
                if (Math.abs(getScrollY()) >= mHeaderViewHeight) { // 滑动超过 Header 高度
                    scrollToRefreshStatus(false);
                } else { // 滑动超过 Header 高度
                    scrollToInitialPosition();
                }
                break;
            //上拉加载更多
            case State.FOOTER_PULL:
                loadEnable = false;
                scrollToDefaultStatus(State.FOOTER_CANCEL);
                break;
            // 正在加载
            case State.FOOTER_RELEASE:
                scrollToLoadStatus(true);
                break;
            case State.FOOTER_LOADING:
                if (Math.abs(getScrollY()) >= mFooterViewHeight) { // 滑动超过 Footer 高度
                    scrollToLoadStatus(false);
                } else { // 滑动没有超过 Footer 高度
                    scrollToInitialPosition();
                }
                break;
            //没有更多数据
            case State.FOOTER_NO_MORE:
                scrollToInitialPosition();
                break;
            default:
                currentStatus = STATE_DEFAULT;
                break;
        }
        mTotalUnconsumed = 0;
        mTotalUnconsumedLoadMore = 0;
    }

    /**
     * @return Whether it is possible for the child view of this layout to
     * scroll up. Override this if the child view is a custom view.
     */
    public boolean canChildScrollUp() {
        if (mChildScrollUpCallback != null) {
            return mChildScrollUpCallback.canChildScrollUp(this, mTarget);
        }
        return mTarget.canScrollVertically(-1);
    }

    /**
     * I. 传入 -1 ,判断可以下滑
     * II. 传入 1, 判断可以上滑
     * {@link androidx.swiperefreshlayout.widget.SwipeRefreshLayout#canChildScrollUp()}
     */
    private boolean canChildScrollDown() {
        return mTarget.canScrollVertically(1);
    }

    private void autoRefresh() {
        if (!isAutoRefresh) return;
        isRefreshing = true;
        // 这里是为了改变自动刷新时默认显示文本,否则会显示下拉刷新,然后在动画执行完成后立马改为 正在刷新
        updateStatus(State.HEADER_REFRESHING);
        performAnim(0, -mHeaderViewHeight, new AnimListener() {
            @Override
            public void onGoing() {
                updateStatus(State.HEADER_READY);
            }

            @Override
            public void onEnd() {
                updateStatus(State.HEADER_REFRESHING);
            }
        });

    }

    private void goToRefresh(int dy) {
        int scrollY = getScrollY();
        if (currentStatus == STATE_REFRESH) {
            performScroll(dy);
            int end = mHeaderViewHeight;
            if (Math.abs(scrollY) > end) {
                updateStatus(State.HEADER_RELEASE);
            } else {
                updateStatus(State.HEADER_DRAG);
            }
        }
    }

    private void goToLoad(int dy) {
        if (mFooterView == null && !isAutoLoad) {
            return;
        }
        if (currentStatus == STATE_LOAD) {
            performScroll(-dy);
            if (noMoreData) {
                updateStatus(State.FOOTER_NO_MORE);
            } else if (getScrollY() >= bottomScroll + mFooterViewHeight) {
                updateStatus(State.FOOTER_RELEASE);
            } else {
                updateStatus(State.FOOTER_PULL);
            }
        }
    }


    private void updateStatus(int status) {
        this.mStatus = status;
        int scrollY = getScrollY();
        switch (status) {
            case State.DEFAULT:
                onDefault();
                break;
            case State.HEADER_DRAG:
                if (mHeaderListener != null) {
                    mHeaderListener.onPullDown(scrollY, mHeaderViewHeight);
                }
                break;
            case State.HEADER_RELEASE:
                if (mHeaderListener != null) {
                    mHeaderListener.onPullDownAndReleasable(scrollY, mHeaderViewHeight);
                }
                break;
            case State.HEADER_READY:
                if (mHeaderListener != null) {
                    mHeaderListener.onRefreshReady(scrollY, mHeaderViewHeight);
                }
                break;
            case State.HEADER_REFRESHING:
                if (mHeaderListener != null) {
                    mHeaderListener.onRefreshing(scrollY, mHeaderViewHeight);
                }
                if (listener != null)
                    listener.onRefresh(this);
                break;
            case State.HEADER_COMPLETED:
                if (mHeaderListener != null) {
                    mHeaderListener.onRefreshComplete(scrollY, mHeaderViewHeight, isRefreshSuccess);
                }
                break;
            case State.HEADER_CANCEL:
                if (mHeaderListener != null) {
                    mHeaderListener.onRefreshCancel(scrollY, mHeaderViewHeight);
                }
                break;
            case State.FOOTER_PULL:
                if (mFooterListener != null) {
                    mFooterListener.onPullUp(scrollY);
                }
                break;
            case State.FOOTER_RELEASE:
                if (mFooterListener != null) {
                    mFooterListener.onPullUpAndReleasable(scrollY);
                }
                break;
            case State.FOOTER_READY:
                if (mFooterListener != null) {
                    mFooterListener.onLoadReady(scrollY);
                }
                break;
            case State.FOOTER_LOADING:
                if (mFooterListener != null) {
                    mFooterListener.onLoading(scrollY);
                }
                if (listener != null)
                    listener.onLoad(this);
                break;
            case State.FOOTER_COMPLETED:
                if (mFooterListener != null) {
                    mFooterListener.onLoadComplete(scrollY, isLoadSuccess);
                }
                break;
            case State.FOOTER_CANCEL:
                if (mFooterListener != null) {
                    mFooterListener.onLoadCancel(scrollY);
                }
                break;
            case State.FOOTER_NO_MORE:
                if (mFooterListener != null) {
                    mFooterListener.onNoMore();
                }
                break;
        }
    }

    /**
     * 重置
     */
    private void onDefault() {
        isRefreshSuccess = false;
        isLoadSuccess = false;
    }

    /**
     * 当正在加载时,则不用回调  onGoing() 和  onEnd()
     *
     * @param notify --> true 需要通知
     */
    private void scrollToLoadStatus(final boolean notify) {
        isLoading = true;
        int start = getScrollY();
        int end = mFooterViewHeight + bottomScroll;
        performAnim(start, end, new AnimListener() {
            @Override
            public void onGoing() {
                if (notify) {
                    updateStatus(State.FOOTER_READY);
                }
            }

            @Override
            public void onEnd() {
                if (notify) {
                    updateStatus(State.FOOTER_LOADING);
                }
            }
        });
    }

    /**
     * 当正在刷新时,则不用回调  onGoing() 和  onEnd()
     *
     * @param notify --> true 需要通知
     */
    private void scrollToRefreshStatus(final boolean notify) {
        isRefreshing = true;
        int start = getScrollY();
        int end = -mHeaderViewHeight;
        performAnim(start, end, new AnimListener() {
            @Override
            public void onGoing() {
                if (notify) {
                    updateStatus(State.HEADER_READY);
                }
            }

            @Override
            public void onEnd() {
                if (notify) {
                    updateStatus(State.HEADER_REFRESHING);
                }
            }
        });
    }

    /**
     * 下拉刷新中,再次下拉没有达到或者超过 HeaderView 高度,此时隐藏 HeaderView
     */
    private void scrollToInitialPosition() {
        int start = getScrollY();
        int end = 0;
        performAnim(start, end, null);
    }

    /**
     * 刷新/加载 没有达到有效距离时,回到初始位置
     */
    private void scrollToDefaultStatus(final int startStatus) {
        int start = getScrollY();
        int end = 0;
        performAnim(start, end, new AnimListener() {
            @Override
            public void onGoing() {
                updateStatus(startStatus);
            }

            @Override
            public void onEnd() {
                refreshEnable = true;
                loadEnable = true;
                updateStatus(State.DEFAULT);
            }
        });
    }


    //////////////////////////////////////            ///////////////////////////////////////
    //////////////////////////////////////  公共方法  ////////////////////////////////////////
    //////////////////////////////////////           ////////////////////////////////////////

    /**
     * 添加 HeaderView
     */
    public void addHeader(@NonNull View header) {
        this.mHeaderView = header;
        if (header instanceof IHeaderWrapper) {
            mHeaderListener = (IHeaderWrapper) header;
            this.mHeaderViewHeight = mHeaderListener.getRefreshHeight();
        } else {
            throw new IllegalArgumentException("HeaderView must be implement IHeaderWrapper");
        }
        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT);
        addView(header, layoutParams);
    }

    /**
     * 添加 FooterView
     */
    public void addFooter(@NonNull View footer) {
        this.mFooterView = footer;
        if (footer instanceof IFooterWrapper) {
            mFooterListener = (IFooterWrapper) footer;
            this.mFooterViewHeight = mFooterListener.getLoadHeight();
        } else {
            throw new IllegalArgumentException("FooterView must be implement IFooterWrapper");
        }
        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT);
        addView(footer, layoutParams);
    }

    /**
     * 设置是否可以自动加载
     *
     * @param autoLoad --> 默认false ,如果设置为 true, 还是需要自己处理嵌套控件滑动到某个位置,调用 {@link #autoLoad()}
     */
    public void setAutoLoadMore(boolean autoLoad) {
        isAutoLoad = autoLoad;
    }

    /**
     * 自动加载的具体逻辑
     */
    public void autoLoad() {
        if (!isAutoLoad) return;
        int end = mFooterViewHeight;
        performAnim(bottomScroll, bottomScroll + end, new AnimListener() {
            @Override
            public void onGoing() {
                updateStatus(State.FOOTER_READY);
            }

            @Override
            public void onEnd() {
                updateStatus(State.FOOTER_LOADING);
            }
        });

    }

    /**
     * 加载/刷新回调
     */
    public void setOnRefreshAndLoadListener(OnRefreshAndLoadListener listener) {
        this.listener = listener;
    }

    /**
     * {@link SwipeRefreshLayout#canChildScrollUp()} method
     * 对 嵌套 View 的滑动逻辑的处理
     */
    public interface OnChildScrollUpCallback {
        /**
         * Callback that will be called when {@link SwipeRefreshLayout#canChildScrollUp()} method
         * is called to allow the implementer to override its behavior.
         *
         * @param parent SwipeRefreshLayout that this callback is overriding.
         * @param child  The child view of Swipe
         * @return Whether it is possible for the child view of parent layout to scroll up.
         */
        boolean canChildScrollUp(PowerRefreshLayout parent, @Nullable View child);
    }

    public void setOnChildScrollUpCallback(@Nullable OnChildScrollUpCallback callback) {
        mChildScrollUpCallback = callback;
    }

    /**
     * 是否可以刷新
     */
    public void setRefreshEnable(boolean isCanRefresh) {
        this.refreshEnable = isCanRefresh;
    }

    /**
     * 是否可以加在
     */
    public void setLoadEnable(boolean isCanLoad) {
        this.loadEnable = isCanLoad;
    }

    /**
     * 设置自动刷新
     */
    public void setAutoRefresh(boolean isAutoRefresh) {
        if (null == mHeaderView) {
            throw new IllegalArgumentException("HeaderView is null");
        }
        this.isAutoRefresh = isAutoRefresh;
        autoRefresh();
    }


    /**
     * 刷新完成
     *
     * @param isSuccess --> 可以根据这个值,设置刷新成功或者失败
     */
    public void stopRefresh(boolean isSuccess) {
        this.isAutoRefresh = false;
        stopRefresh(isSuccess, 800);
    }

    /**
     * 刷新完成
     *
     * @param isSuccess --> 可以根据这个值,设置刷新成功或者失败
     * @param delay     --> 延迟关闭动画,默认延迟 800ms
     */
    public void stopRefresh(boolean isSuccess, long delay) {
        isRefreshSuccess = isSuccess;
        updateStatus(State.HEADER_COMPLETED);
        postDelayed(refreshAction, delay);
    }

    /**
     * 加载完成
     *
     * @param isSuccess --> 可以根据这个值,设置加载成功或者失败
     */
    public void stopLoadMore(boolean isSuccess) {
        stopLoadMore(isSuccess, 800);
    }

    /**
     * 加载完成
     *
     * @param isSuccess --> 可以根据这个值,设置加载成功或者失败
     * @param delay     --> 延迟关闭动画 ,默认 800 ms
     */
    public void stopLoadMore(boolean isSuccess, long delay) {
        isLoadSuccess = isSuccess;
        updateStatus(State.FOOTER_COMPLETED);
        postDelayed(loadAction, delay);
    }

    /**
     * 设置是否有更多数据
     *
     * @param noMoreData --> 默认是 false,即有更多数据
     */
    public void setNoMoreData(boolean noMoreData) {
        this.noMoreData = noMoreData;
        updateStatus(noMoreData ? State.FOOTER_NO_MORE : State.FOOTER_PULL);
    }

}

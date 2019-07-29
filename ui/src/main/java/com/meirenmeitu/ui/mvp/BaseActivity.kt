package com.meirenmeitu.ui.mvp

import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.meirenmeitu.library.rxbind.RxView
import com.meirenmeitu.library.utils.StatusBarUtil
import com.meirenmeitu.net.network.NetWorkReceiver
import com.meirenmeitu.ui.R
import com.meirenmeitu.ui.state.OnRetryListener
import com.meirenmeitu.ui.state.StatusManager
import io.reactivex.disposables.CompositeDisposable

/**
 * Desc: MVP 基类 Activity
 * Author: Jooyer
 * Date: 2018-07-24
 * Time: 12:49
 */
abstract class BaseActivity<T : IBasePresenter> : AppCompatActivity(),
    BaseView, OnRetryListener, RxView.Action1<View> {

    /**
     *  装载 RxBus,防止内存泄漏
     */
    val mCompositeDisposable = CompositeDisposable()

    /**
     * 页面显示加载中,加载失败等管理器
     */
    var mStatusManager: StatusManager? = null

    lateinit var mPresenter: T

    override fun onCreate(savedInstanceState: Bundle?) {
        StatusBarUtil.transparentStatusBar(this, needUseImmersive())
        requestWindowFeature()
        super.onCreate(savedInstanceState)
        mPresenter = createPresenter()
        lifecycle.addObserver(mPresenter)

        // 注册网络变化广播
        NetWorkReceiver.INSTANCE.registerReceiver(this)

        if (useStartAnim()) {
            overridePendingTransition(R.anim.act_bottom_in, R.anim.act_bottom_out)
        }

        if (0 != getLayoutId()) {
            setContentView(initStatusManager(savedInstanceState))
        }

    }

    override fun onAttachedToWindow() {
        setLogic()
        bindEvent()
        Looper.myQueue().addIdleHandler {
            onLoad()
            false
        }
    }

    // 可以放这里加载数据,此时界面UI绘制完成
    open fun onLoad() {
        
    }

    /**
     * 初始化状态管理器
     */
    private fun initStatusManager(savedInstanceState: Bundle?): View {
        if (0 != getLayoutId()) {
            val contentView = LayoutInflater.from(this)
                .inflate(getLayoutId(), null)
            initializedViews(savedInstanceState, contentView)
            return if (useStatusManager()) {
                initialized(contentView)
            } else {
                contentView.visibility = View.VISIBLE
                contentView
            }
        }
        throw IllegalStateException("getLayoutId() 必须调用,且返回正常的布局ID")
    }

    private fun initialized(contentView: View): View {
        mStatusManager = StatusManager.newBuilder(this)
            .contentView(contentView)
            .loadingView(getLoadingViewLayoutId())
            .emptyDataView(getLoadingViewLayoutId())
            .netWorkErrorView(getLoadingViewLayoutId())
            .errorView(getLoadingViewLayoutId())
            .retryViewId(R.id.tv_retry_load_data)
            .onRetryListener(this)
            .build()
        mStatusManager?.showLoading()
        return mStatusManager?.getRootLayout()!!
    }


    /**
     * 是否 fitsSystemWindows, 即在顶部加入一个padding,默认不加入
     */
    open fun needUseImmersive(): Boolean {
        return false
    }

    /**
     * 是否使用启动动画,默认是true
     */
    open fun useStartAnim(): Boolean {
        return true
    }

    override fun finish() {
        super.finish()
        if (useStartAnim()) {
            overridePendingTransition(R.anim.act_bottom_in, R.anim.act_bottom_out)
        }
    }

    override fun onDestroy() {
        // 注消网络变化广播
        NetWorkReceiver.INSTANCE.unRegisterReceiver(this)
        super.onDestroy()
        RxView.disposeBindClick()
        mCompositeDisposable.dispose()
        //结束并移除任务列表
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            finishAndRemoveTask()
        }
    }

    /**
     *   获取 Presenter 对象
     */
    abstract fun createPresenter(): T

    /**
     * 展示布局
     */
    abstract fun getLayoutId(): Int

    /**
     * 初始化 View
     */
    fun initializedViews(savedInstanceState: Bundle?, contentView: View) {

    }

    /**
     * 实现过程
     */
    abstract fun setLogic()

    /**
     * 绑定监听
     */
    abstract fun bindEvent()

    /**
     * 比如全屏,不要 title 等放这里
     */
    open fun requestWindowFeature() {

    }

    /**
     *  是否使用视图布局管理器
     */
    open fun useStatusManager(): Boolean {
        return false
    }

    /**
     *  显示加载Loading
     */
    override fun showLoading() {

    }

    /**
     * 显示错误信息
     */
    override fun showError(message: String) {

    }

    /**
     * 关闭显示Loading
     */
    override fun closeLoading() {

    }

    /**
     * 点击视图中重试按钮
     */
    override fun onRetry() {

    }

    /**
     * 返回加载中布局ID
     */
    open fun getLoadingViewLayoutId() = R.layout.widget_loading_page

    /**
     * 返回空视图布局ID
     */
    open fun getEmptyDataViewLayoutId() = R.layout.widget_empty_page

    /**
     * 返回网路异常布局ID
     */
    open fun getNetWorkErrorViewLayoutId() = R.layout.widget_nonetwork_page

    /**
     * 返回错误/其他异常布局ID
     */
    open fun getErrorViewLayoutId() = R.layout.widget_error_page

    // 延迟 finish(),默认500 ms
    fun delayFinish() {
        delayFinish(500)
    }

    /**
     *@param delayTime 单位 毫秒
     */
    fun delayFinish(delayTime: Int) {
        window.decorView.postDelayed({
            finish()
        }, delayTime.toLong())
    }


    override fun onClick(view: View) {

    }


    /**
     *  布局变亮
     */
    fun lightOn() {
        val attr = window.attributes
        attr.alpha = 1.0f
        window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        window.attributes = attr
    }

    fun lightOff(alpha: Float = 0.4f) {
        val attr = window.attributes
        window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        attr.alpha = alpha
        window.attributes = attr
    }


}
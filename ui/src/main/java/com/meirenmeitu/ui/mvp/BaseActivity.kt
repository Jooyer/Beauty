package com.meirenmeitu.ui.mvp

import android.content.IntentFilter
import android.net.NetworkInfo
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import ccom.meirenmeitu.ui.network.OnNetWorkListener
import com.meirenmeitu.library.rxbind.RxView
import com.meirenmeitu.library.utils.Constants
import com.meirenmeitu.library.utils.StatusBarUtil
import com.meirenmeitu.ui.R
import com.meirenmeitu.ui.network.NetWorkReceiver
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
    BaseView, OnRetryListener, RxView.Action1<View>, OnNetWorkListener {

    /**
     *  装载 RxBus,防止内存泄漏
     */
    val mCompositeDisposable = CompositeDisposable()
    /**
     * 网络监听广播
     */
    private lateinit var mNetWorkReceiver: NetWorkReceiver

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

        if (useStartAnim()) {
            overridePendingTransition(R.anim.act_bottom_in, R.anim.act_bottom_out)
        }
        registerNetWorkReceiver()
        if (0 != getLayoutId()) {
            setContentView(initStatusManager(savedInstanceState))
        }
        setLogic()
        bindEvent()
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
        overridePendingTransition(R.anim.act_bottom_in, R.anim.act_bottom_out)
    }

    /**
     * 注册网络变化的广播
     */
    private fun registerNetWorkReceiver() {
        mNetWorkReceiver = NetWorkReceiver(this)
        val filter = IntentFilter()
        filter.addAction(Constants.CONNECTIVITY_CHANGE)
        filter.addAction(Constants.WIFI_STATE_CHANGED)
        filter.addAction(Constants.STATE_CHANGE)
        registerReceiver(mNetWorkReceiver, filter)
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(mNetWorkReceiver)
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
     *  展示数据
     */
    override fun showData(data: Any?) {

    }

    /**
     * 错误提示
     */
    override fun showError(e: Any?) {

    }

    /**
     * 点击视图中重试按钮
     */
    override fun onRetry() {

    }

    /**
     * 网络正常
     */
    override fun onAvailable(info: NetworkInfo) {

    }

    /**
     * 无网
     */
    override fun onLost() {

    }

    /**
     * 返回加载中布局ID
     */
    fun getLoadingViewLayoutId() = R.layout.widget_loading_page

    /**
     * 返回空视图布局ID
     */
    fun getEmptyDataViewLayoutId() = R.layout.widget_empty_page

    /**
     * 返回网路异常布局ID
     */
    fun getNetWorkErrorViewLayoutId() = R.layout.widget_nonetwork_page

    /**
     * 返回错误/其他异常布局ID
     */
    fun getErrorViewLayoutId() = R.layout.widget_error_page

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
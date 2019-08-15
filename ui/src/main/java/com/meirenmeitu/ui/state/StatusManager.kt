package  com.meirenmeitu.ui.state

import android.content.Context
import android.view.View
import android.view.ViewStub
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes


/**
 * Desc: 视图管理器
 * Author: Jooyer
 * Date: 2018-07-30
 * Time: 11:46
 */
class StatusManager(builder: Builder) {

    private var mContext: Context
    /**
     * 所有视图的根布局
     */
    private var mRootFrameLayout: RootStatusLayout
    /**
     * 内容视图
     */
    var mContentLayoutView: View
    /**
     * 内容视图布局
     */
    var mContentLayoutResId: Int = 0
    /**
     * Loading视图
     */
    var mLoadingLayoutResId: Int = 0

    /**
     * 网络异常 ViewStub
     */
    var mNetworkErrorVs: ViewStub? = null
    /**
     * 网络异常 View
     */
    var mNetworkErrorView: View? = null
    /**
     * 网络异常重试按钮 ID
     */
    var mNetWorkErrorRetryViewId: Int = 0

    /**
     * 空视图 ViewStub
     */
    var mEmptyDataVs: ViewStub? = null
    /**
     * 空视图View
     */
    var mEmptyDataView: View? = null
    /**
     * 空视图重试按钮 ID
     */
    var mEmptyDataRetryViewId: Int = 0
    /**
     * 请求错误 ViewStub
     */
    var mErrorVs: ViewStub? = null
    /**
     * 请求错误 View
     */
    var mErrorView: View? = null
    /**
     * 请求错误重试按钮 ID
     */
    var mErrorRetryViewId: Int = 0
    /**
     * 重试按钮 ID
     */
    var mRetryViewId: Int = 0
    /**
     * 开始加载时间
     */
    var mStartTime: Long = 0

    fun setTransY(transY: Float) {
        mRootFrameLayout.setTransY(transY)
    }

    /**
     * 显示loading
     */
    fun showLoading() {
        mRootFrameLayout.let { root ->
            mStartTime = System.currentTimeMillis()
            root.showLoading()
        }

    }

    /**
     * 显示内容
     */
    fun showContent() {
        val endTime = System.currentTimeMillis()
        if (endTime - mStartTime >= 1200) {
            delayShowContent(0)
        } else {
            delayShowContent(2000 + mStartTime - endTime)
        }
    }

    fun delayShowContent(delay: Long) {
        mRootFrameLayout.let { root ->
            root.postDelayed({
                root.showContent()
            }, delay)
        }
    }

    /**
     * 显示空数据
     */
    fun showEmptyData() {
        mRootFrameLayout.showEmptyData()
    }

    /**
     * 显示网络异常
     */
    fun showNetWorkError() {
        mRootFrameLayout.showNetWorkError()
    }

    /**
     * 显示异常
     */
    fun showError() {
        mRootFrameLayout.showError()
    }

    /**
     * 得到root 布局
     */
    fun getRootLayout(): View {
        return mRootFrameLayout
    }


    class Builder(val context: Context) {
        var loadingLayoutResId: Int = 0
        var contentLayoutResId: Int = 0

        lateinit var contentLayoutView: View

        lateinit var netWorkErrorVs: ViewStub

        var netWorkErrorRetryViewId: Int = 0

        lateinit var emptyDataVs: ViewStub

        var emptyDataRetryViewId: Int = 0

        lateinit var errorVs: ViewStub

        var errorRetryViewId: Int = 0

        var retryViewId: Int = 0

        var onRetryListener: OnRetryListener? = null

        fun loadingView(@LayoutRes loadingLayoutResId: Int): Builder {
            this.loadingLayoutResId = loadingLayoutResId
            return this
        }

        // https://blog.csdn.net/a740169405/article/details/50351013
        // https://www.jianshu.com/p/63a066e7a5a9
        fun netWorkErrorView(@LayoutRes newWorkErrorId: Int): Builder {
            netWorkErrorVs = ViewStub(context)
            netWorkErrorVs.layoutResource = newWorkErrorId
            return this
        }

        fun emptyDataView(@LayoutRes noDataViewId: Int): Builder {
            emptyDataVs = ViewStub(context)
            emptyDataVs.layoutResource = noDataViewId
            return this
        }

        fun errorView(@LayoutRes errorViewId: Int): Builder {
            errorVs = ViewStub(context)
            errorVs.layoutResource = errorViewId
            return this
        }

        fun contentView(contentLayoutView: View): Builder {
            this.contentLayoutView = contentLayoutView
            return this
        }

        fun contentViewResId(@LayoutRes contentLayoutResId: Int): Builder {
            this.contentLayoutResId = contentLayoutResId
            return this
        }

        fun netWorkErrorRetryViewId(@IdRes netWorkErrorRetryViewId: Int): Builder {
            this.netWorkErrorRetryViewId = netWorkErrorRetryViewId
            return this
        }

        fun emptyDataRetryViewId(@IdRes emptyDataRetryViewId: Int): Builder {
            this.emptyDataRetryViewId = emptyDataRetryViewId
            return this
        }

        fun errorRetryViewId(@IdRes errorRetryViewId: Int): Builder {
            this.errorRetryViewId = errorRetryViewId
            return this
        }

        fun retryViewId(@IdRes retryViewId: Int): Builder {
            this.retryViewId = retryViewId
            return this
        }

        fun onRetryListener(onRetryListener: OnRetryListener): Builder {
            this.onRetryListener = onRetryListener
            return this
        }

        fun build(): StatusManager {
            return StatusManager(this)
        }
    }

    companion object {
        fun newBuilder(context: Context): Builder {
            return Builder(context)
        }
    }

    init {
        mContext = builder.context
        mLoadingLayoutResId = builder.loadingLayoutResId
        mNetworkErrorVs = builder.netWorkErrorVs
        mNetWorkErrorRetryViewId = builder.netWorkErrorRetryViewId
        mEmptyDataVs = builder.emptyDataVs
        mEmptyDataRetryViewId = builder.emptyDataRetryViewId
        mErrorVs = builder.errorVs
        mErrorRetryViewId = builder.errorRetryViewId
        mContentLayoutResId = builder.contentLayoutResId
        mRetryViewId = builder.retryViewId
        mContentLayoutView = builder.contentLayoutView
        mRootFrameLayout = RootStatusLayout(mContext)
        mRootFrameLayout.setStatusManager(this)
        mRootFrameLayout.setOnRetryListener(builder.onRetryListener)
    }


}

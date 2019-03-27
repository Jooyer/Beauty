package com.meirenmeitu.beauty.view

import androidx.core.content.ContextCompat
import com.meirenmeitu.beauty.R
import com.meirenmeitu.beauty.presenter.DownloadPresenter
import com.meirenmeitu.library.utils.StatusBarUtil
import com.meirenmeitu.ui.mvp.BaseActivity

/**
 * 我的下载
 */
class DownloadActivity : BaseActivity<DownloadPresenter>(){
    override fun needUseImmersive()= true

    override fun requestWindowFeature() {
        StatusBarUtil.setStatusBarColor(this, ContextCompat.getColor(this,R.color.main_theme_color))
    }

    override fun createPresenter() = DownloadPresenter(this)

    override fun getLayoutId() = R.layout.activity_download

    override fun setLogic() {
    }

    override fun bindEvent() {
    }
}
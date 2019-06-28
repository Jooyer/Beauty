package com.meirenmeitu.beauty.view

import androidx.core.content.ContextCompat
import com.meirenmeitu.beauty.R
import com.meirenmeitu.beauty.presenter.AppreciatePresenter
import com.meirenmeitu.library.utils.StatusBarUtil
import com.meirenmeitu.ui.mvp.BaseActivity

/**
 * 最近欣赏
 */
class AppreciateActivity : BaseActivity<AppreciatePresenter>(){

    override fun needUseImmersive()= true

    override fun requestWindowFeature() {
        StatusBarUtil.setStatusBarColor(this, ContextCompat.getColor(this,R.color.main_theme_color))
    }

    override fun createPresenter() = AppreciatePresenter(this)

    override fun getLayoutId() = R.layout.activity_appreciate

    override fun setLogic() {
    }

    override fun bindEvent() {
    }
}
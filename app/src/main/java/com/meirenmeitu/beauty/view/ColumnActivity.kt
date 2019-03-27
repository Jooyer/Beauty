package com.meirenmeitu.beauty.view

import androidx.core.content.ContextCompat
import com.meirenmeitu.beauty.R
import com.meirenmeitu.beauty.presenter.ColumnPresenter
import com.meirenmeitu.library.utils.StatusBarUtil
import com.meirenmeitu.ui.mvp.BaseActivity

/**
 * 分类管理
 */
class ColumnActivity : BaseActivity<ColumnPresenter>(){
    override fun needUseImmersive()= true

    override fun requestWindowFeature() {
        StatusBarUtil.setStatusBarColor(this, ContextCompat.getColor(this,R.color.main_theme_color))
    }

    override fun createPresenter() = ColumnPresenter(this)
    override fun getLayoutId() = R.layout.activity_column

    override fun setLogic() {
    }

    override fun bindEvent() {
    }
}
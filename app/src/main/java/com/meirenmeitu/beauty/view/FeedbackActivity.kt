package com.meirenmeitu.beauty.view

import androidx.core.content.ContextCompat
import com.meirenmeitu.base.utils.SelectorFactory
import com.meirenmeitu.beauty.R
import com.meirenmeitu.beauty.presenter.FeedbackPresenter
import com.meirenmeitu.library.utils.DensityUtils
import com.meirenmeitu.library.utils.KeyBoardUtil
import com.meirenmeitu.library.utils.StatusBarUtil
import com.meirenmeitu.ui.mvp.BaseActivity
import kotlinx.android.synthetic.main.activity_feedback.*

/**
 * 意见反馈
 */
class FeedbackActivity : BaseActivity<FeedbackPresenter>(){
    override fun needUseImmersive()= true

    override fun requestWindowFeature() {
        StatusBarUtil.setStatusBarColor(this, ContextCompat.getColor(this,R.color.main_theme_color))
    }

    override fun createPresenter() = FeedbackPresenter(this)
    override fun getLayoutId() = R.layout.activity_feedback

    override fun setLogic() {
        btn_submit_feedback.background = SelectorFactory.newShapeSelector()
            .setDefaultBgColor(ContextCompat.getColor(this, R.color.color_666666))
            .setPressedBgColor(ContextCompat.getColor(this, R.color.main_theme_color))
            .setCornerRadius(DensityUtils.dpToPx(5))
            .create()
    }

    override fun onResume() {
        super.onResume()
        KeyBoardUtil.openKeyboard(this, et_content_feedback)
    }

    override fun bindEvent() {
    }
}
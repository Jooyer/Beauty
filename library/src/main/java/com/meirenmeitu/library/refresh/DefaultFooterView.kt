package com.meirenmeitu.library.refresh

import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Color
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import com.meirenmeitu.library.R
import com.meirenmeitu.library.utils.DensityUtils

/**
 * Desc: 默认的加载视图
 * Author: Jooyer
 * Date: 2019-08-26
 * Time: 17:40
 */
class DefaultFooterView(context: Context) : FooterViewWrapper(context) {

    private lateinit var mIvIcon: ImageView
    private lateinit var mTvTips: TextView
    private lateinit var mProgressBar: ProgressBar
    private var mContent: LinearLayout = LinearLayout(getContext())

    init {
        mContent.gravity = Gravity.CENTER
        mContent.orientation = LinearLayout.HORIZONTAL
        val lp = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        addView(mContent, lp)
        initContentView()
        setBackgroundColor(Color.WHITE)
    }


    private fun initContentView() {
        mContent.removeAllViews()
        val view = LayoutInflater.from(context).inflate(R.layout.footer_default, this, false)
        mIvIcon = view.findViewById(R.id.iv_tip)
        mTvTips = view.findViewById(R.id.tv_tip)
        mProgressBar = view.findViewById(R.id.pb_loading)
        mContent.addView(view)
        setState(IFooterWrapper.STATE.START)
    }

    override fun getOriginalHeight() = DensityUtils.dpToPx(48)

    override fun onRefreshBegin(targetView: View) {
        RefreshAnimUtil.startLoading(this, targetView, getTargetHandler(), getOriginalHeight(), null)
    }

    override fun onPrepare(targetView: View, state: Int) {
        when (state) {
            2 -> RefreshAnimUtil.startFinishLoad(this, targetView, getTargetHandler(), 0, mAnimatorListener)
            1 -> RefreshAnimUtil.startLoading(this, targetView, getTargetHandler(), 0, null)
            0 -> {
                val params = layoutParams
                params.height = 0
                layoutParams = params
            }
            else -> RefreshAnimUtil.setNoMoreAnimator(this,targetView,getTargetHandler())
        }
    }

    override fun onPulling(dis: Float, targetView: View) {
        val params = layoutParams
        if (dis > 0) {
            if (mState === IFooterWrapper.STATE.REFRESH) {
                params.height += 30
                if (params.height > getOriginalHeight()) params.height = getOriginalHeight()
            } else {
                params.height += dis.toInt()
            }
        } else if (dis < 0) {
            params.height += dis.toInt()
            if (params.height < 0) {
                params.height = 0
            }
        }
        layoutParams = params
        getTargetHandler().handleTarget(targetView, params.height.toFloat())
    }

    private fun getStateTips(state: IFooterWrapper.STATE): String {
        return when {
            state === IFooterWrapper.STATE.REFRESH -> context.getString(R.string.loading_tips)
            state === IFooterWrapper.STATE.START -> context.getString(R.string.normal_tips_2)
            state === IFooterWrapper.STATE.PULL -> context.getString(R.string.pulling_tips_2)
            state === IFooterWrapper.STATE.LOADING -> context.getString(R.string.loading)
            state === IFooterWrapper.STATE.COMPLETE -> context.getString(R.string.complete_tips)
            else -> "没有更多数据"
        }
    }


    override fun setStartState() {
        mIvIcon.visibility = View.VISIBLE
        mProgressBar.visibility = View.GONE
        if (mState === IFooterWrapper.STATE.PULL) {
            ObjectAnimator.ofFloat(mIvIcon, "rotation", 180F, 0F).start()
        }
        mTvTips.text = getStateTips(IFooterWrapper.STATE.START)
    }

    override fun setPullState() {
        mIvIcon.visibility = View.VISIBLE
        mProgressBar.visibility = View.GONE
        if (mState === IFooterWrapper.STATE.START) {
            ObjectAnimator.ofFloat(mIvIcon, "rotation", 0F, 180F).start()
        }
        mTvTips.text = getStateTips(IFooterWrapper.STATE.PULL)
    }

    override fun setRefreshState() {
        mIvIcon.visibility = View.GONE
        mProgressBar.visibility = View.VISIBLE
        mTvTips.text = getStateTips(IFooterWrapper.STATE.REFRESH)
    }

    override fun setCompleteState() {
        mIvIcon.visibility = View.GONE
        mProgressBar.visibility = View.GONE
        mTvTips.text = getStateTips(IFooterWrapper.STATE.COMPLETE)
        mIvIcon.rotation = 0f
    }

    override fun setLoading() {
        mIvIcon.visibility = View.GONE
        mProgressBar.visibility = View.GONE
        mTvTips.text = getStateTips(IFooterWrapper.STATE.LOADING)
    }

    override fun setNoMoreData() {
        mIvIcon.visibility = View.GONE
        mProgressBar.visibility = View.GONE
        mTvTips.text = getStateTips(IFooterWrapper.STATE.NOTHING)
        mIvIcon.rotation = 0f
    }

    override fun onNoMoreData(targetView: View) {
        RefreshAnimUtil.setNoMoreAnimator(this, targetView, getTargetHandler())
    }

}
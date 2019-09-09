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
 * Desc: 默认的刷新视图
 * Author: Jooyer
 * Date: 2019-08-26
 * Time: 17:15
 */
class DefaultHeaderView(context: Context) : HeaderViewWrapper(context) {

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
        val view = LayoutInflater.from(context).inflate(R.layout.header_default, this, false)
        mIvIcon = view.findViewById(R.id.iv_tip)
        mTvTips = view.findViewById(R.id.tv_tip)
        mProgressBar = view.findViewById(R.id.pb_refreshing)
        mContent.addView(view)
        setState(IHeaderWrapper.STATE.START)
    }

    // 重写这个,可以保证 刷新头一直贴着列表的顶部
    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        mContent.layout(0, height - mContent.measuredHeight, width, height)
    }

    // 根据布局高度填写
    override fun getOriginalHeight() = DensityUtils.dpToPx(48)

    override fun onRefreshBegin(targetView: View) {
        RefreshAnimUtil.startRefreshing(this, targetView, getTargetHandler(), getOriginalHeight(), null)
    }

    override fun onAutoRefresh(targetView: View) {
        RefreshAnimUtil.startAutoRefreshing(this, targetView, getTargetHandler(), getOriginalHeight(), null)
    }

    override fun onPrepare(targetView: View, state: Int) {
        if (2 == state) {
            RefreshAnimUtil.startFinishRefresh(this, targetView, getTargetHandler(), 0, mAnimatorListener)
        } else {
            RefreshAnimUtil.startRefreshing(this, targetView, getTargetHandler(), 0, null)
        }
    }

    override fun onPulling(dis: Float, targetView: View) {
        val params = layoutParams
        if (dis > 0) {
            if (mState === IHeaderWrapper.STATE.REFRESH) {
                params.height += dis.toInt()
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

    override fun setStartState() {
        mIvIcon.visibility = View.VISIBLE
        mProgressBar.visibility = View.GONE
        if (mState === IHeaderWrapper.STATE.PULL) {
            ObjectAnimator.ofFloat(mIvIcon, "rotation", 180F, 0F).start()
        }
        mTvTips.text = getStateTips(IHeaderWrapper.STATE.START)
    }

    override fun setPullState() {
        mIvIcon.visibility = View.VISIBLE
        mProgressBar.visibility = View.GONE
        if (mState === IHeaderWrapper.STATE.START) {
            ObjectAnimator.ofFloat(mIvIcon, "rotation", 0F, 180F).start()
        }
        mTvTips.text = getStateTips(IHeaderWrapper.STATE.PULL)
    }

    override fun setRefreshState() {
        mIvIcon.visibility = View.GONE
        mProgressBar.visibility = View.VISIBLE
        mTvTips.text = getStateTips(IHeaderWrapper.STATE.REFRESH)
    }

    override fun setLoading() {
        mIvIcon.visibility = View.GONE
        mProgressBar.visibility = View.GONE
        mTvTips.text = getStateTips(IHeaderWrapper.STATE.LOADING)
    }

    override fun setCompleteState() {
        mIvIcon.visibility = View.GONE
        mProgressBar.visibility = View.GONE
        mTvTips.text = getStateTips(IHeaderWrapper.STATE.COMPLETE)
        mIvIcon.rotation = 0f
    }


    private fun getStateTips(state: IHeaderWrapper.STATE): String {
        return when {
            state === IHeaderWrapper.STATE.REFRESH -> context.getString(R.string.loading_tips)
            state === IHeaderWrapper.STATE.START -> context.getString(R.string.normal_tips)
            state === IHeaderWrapper.STATE.PULL -> context.getString(R.string.pulling_tips)
            state === IHeaderWrapper.STATE.LOADING -> context.getString(R.string.loading)
            state === IHeaderWrapper.STATE.COMPLETE -> context.getString(R.string.complete_tips)
            else -> ""
        }
    }
}
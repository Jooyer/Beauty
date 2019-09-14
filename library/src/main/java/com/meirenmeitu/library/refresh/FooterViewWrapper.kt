package com.meirenmeitu.library.refresh

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Context
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout

/**
 * Desc: 加载 View 的父类
 * Author: Jooyer
 * Date: 2019-08-26
 * Time: 17:37
 */
abstract class FooterViewWrapper(context: Context) : FrameLayout(context), IFooterWrapper {

    var mState = IFooterWrapper.STATE.START

    val mAnimatorListener = object : AnimatorListenerAdapter() {
        override fun onAnimationEnd(animation: Animator) {
            updateState(IFooterWrapper.STATE.START)
            RefreshAnimUtil.mAnimRunning = false
        }
    }

    override fun getState() = mState

    override fun setState(state: IFooterWrapper.STATE) {
        mState = state
    }

    override fun updateState(state: IFooterWrapper.STATE) {
        when (state) {
            IFooterWrapper.STATE.START -> {
                if (IFooterWrapper.STATE.START != mState) {
                    setStartState()
                    mState = IFooterWrapper.STATE.START
                }
            }
            IFooterWrapper.STATE.PULL -> {
                if (IFooterWrapper.STATE.PULL != mState) {
                    setPullState()
                    mState = IFooterWrapper.STATE.PULL
                }
            }
            IFooterWrapper.STATE.REFRESH -> {
                if (IFooterWrapper.STATE.REFRESH != mState) {
                    setRefreshState()
                    mState = IFooterWrapper.STATE.REFRESH
                }
            }
            IFooterWrapper.STATE.COMPLETE -> {
                if (IFooterWrapper.STATE.COMPLETE != mState) {
                    setCompleteState()
                    mState = IFooterWrapper.STATE.COMPLETE
                }
            }
            IFooterWrapper.STATE.LOADING -> { // 正在加载中
                if (IFooterWrapper.STATE.LOADING != mState) {
                    setLoading()
                    mState = IFooterWrapper.STATE.LOADING
                }
            }
            else -> {
                if (IFooterWrapper.STATE.NOTHING != mState) {
                    setNoMoreData()
                    mState = IFooterWrapper.STATE.NOTHING
                }
            }
        }
    }

    override fun getFooterView() = this

    override fun canTargetScroll() = height <= 0

    override fun addToRefreshLayout(layout: JRefreshLayout) {
        val footerParams = LayoutParams(LayoutParams.MATCH_PARENT, 0)
        footerParams.gravity = Gravity.BOTTOM
        layout.addView(this, footerParams)
    }

    private val mTargetHandler =
        object : TargetHandler {
            override fun handleTarget(targetView: View, dis: Float) {
                targetView.translationY = -dis
            }
        }

    // HeaderView 和 FooterView 不同是 dis 前面的正负号
    override fun getTargetHandler() = mTargetHandler

    abstract fun setStartState()

    abstract fun setPullState()

    abstract fun setRefreshState()

    abstract fun setCompleteState()

    /**
     * 如果正在刷新,则加载更多不能触发的,反之一样
     */
    abstract fun setLoading()

    abstract fun setNoMoreData()


}
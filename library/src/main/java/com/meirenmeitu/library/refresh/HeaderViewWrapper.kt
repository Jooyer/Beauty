package com.meirenmeitu.library.refresh

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Context
import android.view.View
import android.widget.FrameLayout

/**
 * Desc: 刷新 View 的父类
 * Author: Jooyer
 * Date: 2019-08-26
 * Time: 17:02
 */
abstract class HeaderViewWrapper(context: Context) : FrameLayout(context), IHeaderWrapper {

    var mState = IHeaderWrapper.STATE.START

    val mAnimatorListener = object : AnimatorListenerAdapter() {
        override fun onAnimationEnd(animation: Animator) {
            updateState(IHeaderWrapper.STATE.START)
            RefreshAnimUtil.mAnimRunning = false
        }
    }

    override fun getState() = mState

    override fun setState(state: IHeaderWrapper.STATE) {
        mState = state
    }

    override fun updateState(state: IHeaderWrapper.STATE) {
        when (state) {
            IHeaderWrapper.STATE.START -> {
                if (IHeaderWrapper.STATE.START != mState) {
                    setStartState()
                    mState = IHeaderWrapper.STATE.START
                }
            }
            IHeaderWrapper.STATE.PULL -> {
                if (IHeaderWrapper.STATE.PULL != mState) {
                    setPullState()
                    mState = IHeaderWrapper.STATE.PULL
                }
            }
            IHeaderWrapper.STATE.REFRESH -> {
                if (IHeaderWrapper.STATE.REFRESH != mState) {
                    setRefreshState()
                    mState = IHeaderWrapper.STATE.REFRESH
                }
            }
            IHeaderWrapper.STATE.COMPLETE -> {
                if (IHeaderWrapper.STATE.COMPLETE != mState) {
                    setCompleteState()
                    mState = IHeaderWrapper.STATE.COMPLETE
                }
            }
            else -> { // 正在加载中
                if (IHeaderWrapper.STATE.LOADING != mState) {
                    setLoading()
                    mState = IHeaderWrapper.STATE.LOADING
                }
            }
        }
    }

    override fun getHeaderView(): View {
        return this
    }

    override fun canTargetScroll() = height <= 0

    override fun addToRefreshLayout(layout: JRefreshLayout) {
        val params = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        layout.addView(this, params)
        params.height = 0
        layoutParams = params
    }


    private val mTargetHandler =object :TargetHandler {
        override fun handleTarget(targetView: View, dis: Float) {
            targetView.translationY = dis
        }
    }
    // HeaderView 和 FooterView 不同是 dis 前面的正负号
    override fun getTargetHandler() = mTargetHandler


    abstract fun setStartState()

    abstract fun setPullState()

    abstract fun setRefreshState()

    /**
     * 如果正在刷新,则加载更多不能触发的,反之一样
     */
    abstract fun setLoading()

    abstract fun setCompleteState()

}
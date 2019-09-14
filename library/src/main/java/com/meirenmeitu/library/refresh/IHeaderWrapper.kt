package com.meirenmeitu.library.refresh

import android.view.View

/**
 * Desc: 刷新接口
 * Author: Jooyer
 * Date: 2019-08-26
 * Time: 17:00
 */
interface IHeaderWrapper {


    enum class STATE {
        // 加载/刷新
        REFRESH,
        // 下拉刷新
        START,
        // 松开刷新
        PULL,
        COMPLETE,
        // 如果正在刷新,则加载更多不能触发的,反之一样
        LOADING,
        // 没有更多数据
        NOTHING
    }

    fun getState(): STATE

    fun setState(state: STATE)

    fun updateState(state: STATE)

    fun getHeaderView(): View

    fun getOriginalHeight(): Int

    fun canTargetScroll(): Boolean

    fun onRefreshBegin(targetView: View)

    fun onAutoRefresh(targetView: View)

    /**
     * 加载(刷新)完成/未达加载(刷新)标准而重置
     * @param state --> 0 重置, 1 加载中, 2 加载完成,
     */
    fun onPrepare(targetView: View,state: Int)

    fun onPulling(dis: Float, targetView: View)

    fun addToRefreshLayout(layout: JRefreshLayout)

    fun getTargetHandler(): TargetHandler

}
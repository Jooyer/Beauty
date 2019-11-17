package com.meirenmeitu.library.refresh

/**
 * Desc: 提供刷新头部控件
 * Author: Jooyer
 * Date: 2019-08-06
 * Time: 15:38
 */
interface IHeaderWrapper {

    /**
     * 下拉中
     */
    fun onPullDown(scrollY: Int, headerHeight: Int)

    /**
     * 释放即可刷新
     */
    fun onPullDownAndReleasable(scrollY: Int, headerHeight: Int)

    /**
     * 准备刷新
     */
    fun onRefreshReady(scrollY: Int, headerHeight: Int)

    /**
     * 刷新中
     */
    fun onRefreshing(scrollY: Int, headerHeight: Int)

    /**
     * 刷新完成
     */
    fun onRefreshComplete(scrollY: Int, headerHeight: Int, isRefreshSuccess: Boolean)

    /**
     * 取消刷新
     */
    fun onRefreshCancel(scrollY: Int, headerHeight: Int)

    /**
     * 获取刷视图高度
     */
    fun getRefreshHeight(): Int

}
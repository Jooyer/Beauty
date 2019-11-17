package com.meirenmeitu.library.refresh

/**
 * Desc: 提供上拉加载控件
 * Author: Jooyer
 * Date: 2019-08-06
 * Time: 15:38
 */
interface IFooterWrapper {

    /**
     * 上拉中
     */
    fun onPullUp(scrollY: Int)

    /**
     * 释放即可加载
     */
    fun onPullUpAndReleasable(scrollY: Int)

    /**
     * 准备加载
     */
    fun onLoadReady(scrollY: Int)

    /**
     * 加载中
     */
    fun onLoading(scrollY: Int)

    /**
     * 刷新完成
     */
    fun onLoadComplete(scrollY: Int, isLoadSuccess: Boolean)

    /**
     * 加载取消
     */
    fun onLoadCancel(scrollY: Int)

    /**
     * 没有数据
     */
    fun onNoMore()


    /**
     * 获取加载视图高度
     */
    fun getLoadHeight(): Int

}
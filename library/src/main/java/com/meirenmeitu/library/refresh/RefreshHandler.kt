package com.meirenmeitu.library.refresh

/**
 * @author chqiu
 */
interface RefreshHandler {
    fun onRefresh(refresh: JRefreshLayout)

    fun onLoadMore(refresh: JRefreshLayout)
}

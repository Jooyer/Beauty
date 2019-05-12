package com.meirenmeitu.net.response

/**
 * Desc:
 * Author: Jooyer
 * Date: 2019-05-11
 * Time: 19:57
 */
data class PageInfo<T>(var total: Long, var page: Long, var size: Long, var list: T)

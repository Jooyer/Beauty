package com.meirenmeitu.net.response

import androidx.annotation.Keep

/**
 * Desc:
 * Author: Jooyer
 * Date: 2019-05-11
 * Time: 19:57
 */
@Keep
data class PageInfo<T>(var total: Long, var page: Long, var size: Long, var list: T)

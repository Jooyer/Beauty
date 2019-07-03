package com.meirenmeitu.net.response

import androidx.annotation.Keep

/**
 * Desc: 不需要返回值时
 * Author: Jooyer
 * Date: 2019-06-28
 * Time: 14:47
 */
@Keep
data class Response(val code: Int, val msg: String)
package com.meirenmeitu.net.cover

/**
 * Desc: 错误信息封装
 * Author: Jooyer
 * Date: 2019-06-28
 * Time: 12:39
 */
class ApiException(val code: Int,val msg: String) : RuntimeException(msg) {
}
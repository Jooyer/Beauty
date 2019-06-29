package com.meirenmeitu.net.cover

/**
 * Desc: 当提交服务器时使用 @Body,即上传对象时使用此注解,自定义DataConverterFactory将不对其进行处理
 * Author: Jooyer
 * Date: 2019-06-28
 * Time: 10:28
 */
@Target(AnnotationTarget.FUNCTION)
annotation class TypeBean (
    /**
     * 默认 1 --> 表示只排除提交数据时使用, 2 --> 表示返回数据过滤, 3 --> 表示提交和返回都过滤
     */
    val allExpose : Int = 1
)

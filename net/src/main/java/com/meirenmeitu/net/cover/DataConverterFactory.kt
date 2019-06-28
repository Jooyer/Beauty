package com.meirenmeitu.net.cover

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type



/** https://blog.csdn.net/qq_22804827/article/details/61915760
 * Desc: 自定义工厂方法解析注解的数据
 * Author: Jooyer
 * Date: 2018-07-30
 * Time: 9:48
 */

class DataConverterFactory(private val gson: Gson) : Converter.Factory() {

    companion object {
        val gson = GsonBuilder().registerTypeAdapterFactory(NullStringToEmptyAdapterFactory()).create()
        fun create(): DataConverterFactory {
            return DataConverterFactory(gson)
        }
    }

    // //响应
    override fun responseBodyConverter(type: Type, annotations: Array<out Annotation>, retrofit: Retrofit): Converter<ResponseBody, *>? {
        val adapter = gson.getAdapter(TypeToken.get(type))
        println("responseBodyConverter===========${TypeToken.get(type).rawType}")
        var annotation: TypeData? = null
        annotations.forEach {
            when (it){
                is TypeData ->{
                    annotation = it
                }
            }
        }

        return when (annotation){
            null -> super.responseBodyConverter(type, annotations, retrofit)
            else -> DataResponseBodyConverter(adapter,gson,annotation!!)
        }

    }

    override fun requestBodyConverter(type: Type, annotations: Array<out Annotation>,
                                      methodAnnotations: Array<out Annotation>, retrofit: Retrofit): Converter<*, RequestBody>? {

        var annotation: TypeBean? = null
        annotations.forEach {
            when (it){
                is TypeBean ->{
                    annotation = it
                }
            }
        }

        return when (annotation){
            null -> super.requestBodyConverter(type, annotations, methodAnnotations, retrofit)
            else -> {
                val adapter = gson.getAdapter(TypeToken.get(type))
                 DataRequestBodyConverter(adapter,gson)
            }
        }
    }
    
}
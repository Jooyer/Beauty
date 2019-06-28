package com.meirenmeitu.net.cover

import com.google.gson.Gson
import com.google.gson.TypeAdapter
import okhttp3.MediaType
import okhttp3.RequestBody
import okio.Buffer
import retrofit2.Converter
import java.io.OutputStreamWriter
import java.nio.charset.Charset


/** https://blog.csdn.net/jdsjlzx/article/details/51860663
 * https://blog.csdn.net/qfanmingyiq/article/details/53409068
 * https://www.jianshu.com/p/25525e8180e6 (拦截器方式)
 * Desc: 可用于加密发送等
 * Author: Jooyer
 * Date: 2018-07-30
 * Time: 10:28
 */
class DataRequestBodyConverter<T>(private val adapter: TypeAdapter<T>, private val gson: Gson) :
    Converter<T, RequestBody> {

    private val MEDIA_TYPE = MediaType.parse("application/json; charset=UTF-8")
    private val UTF_8 = Charset.forName("UTF-8")

    override fun convert(value: T): RequestBody {
        val buffer = Buffer()
        val writer = OutputStreamWriter(buffer.outputStream(), UTF_8)
        val jsonWriter = gson.newJsonWriter(writer)
        adapter.write(jsonWriter, value)
        jsonWriter.flush()
        jsonWriter.close()
        return RequestBody.create(MEDIA_TYPE, buffer.readByteString())
    }
}
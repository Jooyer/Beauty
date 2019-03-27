package com.meirenmeitu.net.cover;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import okhttp3.ResponseBody;
import retrofit2.Converter;

/** https://www.jianshu.com/p/1a55eda757fe ---> 参考
 * Desc:
 * Author: Jooyer
 * Date: 2018-11-11
 * Time: 22:22
 */
public class JsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {
    private static final String TAG = JsonResponseBodyConverter.class.getSimpleName();
    private final Gson mGson;//gson对象
    private final TypeAdapter<T> adapter;

    /**
     * 构造器
     */
    public JsonResponseBodyConverter(Gson gson, TypeAdapter<T> adapter) {
        this.mGson = gson;
        this.adapter = adapter;
    }


    @Override
    public T convert(ResponseBody responseBody) throws IOException {

        // 如果返回数据有加密,需要用这个
//        byte[] bytes = responseBody.bytes();
//        //对字节数组进行解密操作
//        String decryptString = RSAUtil.decryptByPublicKeyForSpilt(bytes);
//
//        //对解密的字符串进行处理
//        int position = decryptString.lastIndexOf("}");
//        String jsonString = decryptString.substring(0,position+1);
//
//        Log.i(TAG, "需要解密的服务器数据字节数组：" + bytes.toString());
//        Log.i(TAG, "解密后的服务器数据字符串：" + decryptString);
//        Log.i(TAG, "解密后的服务器数据字符串处理为json：" + jsonString);

        //这部分代码参考GsonConverterFactory中GsonResponseBodyConverter<T>的源码对json的处理
        Reader reader = new StringReader(responseBody.string());
        JsonReader jsonReader = mGson.newJsonReader(reader);
        try {
            return adapter.read(jsonReader);
        } finally {
            reader.close();
            jsonReader.close();
        }
    }


}

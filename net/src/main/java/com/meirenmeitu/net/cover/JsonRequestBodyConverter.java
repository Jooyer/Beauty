package com.meirenmeitu.net.cover;

/**
 * Desc: 对请求时加密操作
 * Author: Jooyer
 * Date: 2018-11-11
 * Time: 22:19
 */
public class JsonRequestBodyConverter<T>  {

//        implements Converter<T, RequestBody> {
//    private static final String TAG = JsonRequestBodyConverter.class.getSimpleName();
//    private static final MediaType MEDIA_TYPE = MediaType.parse("application/json; charset=UTF-8");
//    private final Gson gson;
//    private final TypeAdapter<T> adapter;
//
//    /**
//     * 构造器
//     */
//    public JsonRequestBodyConverter(Gson gson, TypeAdapter<T> adapter) {
//        this.gson = gson;
//        this.adapter = adapter;
//    }
//
//    @Override
//    public RequestBody convert(T value) throws IOException {
//
//        //这里需要，特别注意的是，request是将T转换成json数据。
//        //你要在T转换成json之后再做加密。
//        //再将数据post给服务器，同时要注意，你的T到底指的那个对象
//
//        //加密操作，返回字节数组
//        byte[] encrypt = RSAUtil.encryptByPublicKeyForSpilt(value.toString());
//
//        Log.i(TAG, "request中传递的json数据：" + value.toString()); //打印：加密前的json字符串
//        Log.i(TAG, "加密后的字节数组：" + encrypt.toString());//打印：字节数组
//
//        //传入字节数组，创建RequestBody 对象
//        return RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),encrypt);
//    }

}


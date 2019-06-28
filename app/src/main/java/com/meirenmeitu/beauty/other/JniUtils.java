package com.meirenmeitu.beauty.other;

/** 参考:  https://blog.csdn.net/leifengpeng/article/details/52681196
 * Created by Jooyer on 2018/6/19
 */

public class JniUtils {
    static {
        //加载so库的时候，需要掐头去尾，去掉lib和.so
        System.loadLibrary("c++_shared");
        System.loadLibrary("native-lib");

    }

    public static native void judgeSHA1Signature();
    public static native void judgeMD5Signature();
    public static native String getSHA1Signature();
    public static native String getMD5Signature();
}

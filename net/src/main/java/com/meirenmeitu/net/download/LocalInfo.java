package com.meirenmeitu.net.download;

/**
 * Created by Jooyer on 2018/6/14
 *  保存 APP 版本信息
 */

public class LocalInfo {

    private int versionCode;
    private String versionName;

    public LocalInfo(int versionCode, String versionName) {
        this.versionCode = versionCode;
        this.versionName = versionName;
    }

    public int getVersionCode() {
        return versionCode;
    }

    public String getVersionName() {
        return versionName;
    }


}

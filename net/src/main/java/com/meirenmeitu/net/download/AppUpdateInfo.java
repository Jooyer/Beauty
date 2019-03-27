package com.meirenmeitu.net.download;

/**
 * Desc:
 * Author: Jooyer
 * Date: 2018-11-25
 * Time: 12:29
 */
public class AppUpdateInfo {

    // 下载地址
    private String downloadUrl;
    // 版本号 (1.0.0)
    private String versionCode;
    // 更新具体内容(可以使用2个@@分割)
    private String updateInfo;
    // 文件MD5信息
    private String  fileSign;
    // 更新大小
    private int fileSize;

    // 增量还是全量(0 --> 增量, 1 --> 全量)
    private int increment;

    // 是否强制更新  (0 --> 不强制, 1 --> 强制)
    private int forceUpdate;

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public String getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(String versionCode) {
        this.versionCode = versionCode;
    }

    public String getUpdateInfo() {
        return updateInfo;
    }

    public void setUpdateInfo(String updateInfo) {
        this.updateInfo = updateInfo;
    }

    public String getFileSign() {
        return fileSign;
    }

    public void setFileSign(String fileSign) {
        this.fileSign = fileSign;
    }

    public int getFileSize() {
        return fileSize;
    }

    public void setFileSize(int fileSize) {
        this.fileSize = fileSize;
    }

    public int getIncrement() {
        return increment;
    }

    public void setIncrement(int increment) {
        this.increment = increment;
    }

    public int getForceUpdate() {
        return forceUpdate;
    }

    public void setForceUpdate(int forceUpdate) {
        this.forceUpdate = forceUpdate;
    }

    @Override
    public String toString() {
        return "AppUpdateInfo{" +
                "downloadUrl='" + downloadUrl + '\'' +
                ", versionCode='" + versionCode + '\'' +
                ", updateInfo='" + updateInfo + '\'' +
                ", fileSign='" + fileSign + '\'' +
                ", fileSize=" + fileSize +
                ", increment=" + increment +
                ", forceUpdate=" + forceUpdate +
                '}';
    }
}

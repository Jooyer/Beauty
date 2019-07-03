package com.meirenmeitu.net.download;

import androidx.annotation.Keep;

import java.io.Serializable;

/**
 * Created by Jooyer on 2018/6/14
 * ${DESCRIPTION}
 */
@Keep
public class FileInfo implements Serializable {
    // 文件ID
    private int id;
    // 下载地址
    private String url;
    // 文件名称
    private String fileName;
    // 文件总大小
    private int length;
    // 文件已下载大小
    private int finished;

    public FileInfo(int id, String url, String fileName, int length, int finished) {
        this.id = id;
        this.url = url;
        this.fileName = fileName;
        this.length = length;
        this.finished = finished;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getFinished() {
        return finished;
    }

    public void setFinished(int finished) {
        this.finished = finished;
    }
}

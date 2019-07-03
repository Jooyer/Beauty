package com.meirenmeitu.net.download;

import androidx.annotation.Keep;

/**
 * Created by Jooyer on 2018/6/14
 *
 */
@Keep
public class ThreadInfo {
    // 线程 ID
    private int id;
    // 下载地址
    private String url;
    // 线程开始下载的位置
    private int start;
    // 线程结束下载的位置
    private int end;
    // 线程下载了多少
    private int finished;

    public ThreadInfo() {
    }

    public ThreadInfo(int id, String url,  int start, int end, int finished) {
        this.id = id;
        this.url = url;
        this.start = start;
        this.end = end;
        this.finished = finished;
    }

    public int getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }


    public int getStart() {
        return start;
    }

    public int getEnd() {
        return end;
    }

    public int getFinished() {
        return finished;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUrl(String url) {
        this.url = url;
    }



    public void setStart(int start) {
        this.start = start;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public void setFinished(int finished) {
        this.finished = finished;
    }
}

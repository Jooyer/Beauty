package com.meirenmeitu.net.download;

import java.util.List;

/**
 * Created by Jooyer on 2018/6/14
 *
 */

public interface ThreadInfoDao {

    // 插入
    public void insertThread(ThreadInfo info);
    // 刪除
    public void deleteThread(String url);
    // 更新
    public void updateThread(String url, int thread_id, int finished);
    // 查詢
    public List<ThreadInfo> queryThreads(String url);
    // 判断线程是否存在
    public boolean isExists(String url, int threadId);

    public void close();


}

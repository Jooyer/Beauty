package com.meirenmeitu.net.download;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Jooyer on 2018/6/14
 */

public class DownloadTask {
    public static final String DOWNLOAD_PATH = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
    private Context mComtext = null;
    private FileInfo mFileInfo = null;
    private ThreadInfoDao mDao = null;
    private int mFinished = 0;
    public boolean mIsPause = false;
    private int mThreadCount = 1;
    private List<DownloadThread> mThreads = null;
    public static ExecutorService sExecutorService = Executors.newFixedThreadPool(5);

    public DownloadTask(Context context, FileInfo fileInfo, int threadCount) {
        mComtext = context;
        mFileInfo = fileInfo;
        mThreadCount = threadCount;
        mDao = new ThreadInfoDaoImpl(context);
    }


    public void close(){
        mDao.close();
    }

    public void download() {
        // 从数据库中获取下载的信息
        List<ThreadInfo> list = mDao.queryThreads(mFileInfo.getUrl());
        if (list.size() == 0) { // 数据库没有此下载信息
            int length = mFileInfo.getLength();
            int block = length / mThreadCount;
            Log.i("DownloadTask","download==============length: " + length
            + " ========block: " + block);
            for (int i = 0; i < mThreadCount; i++) {
                // 划分每一个线程开始下载和结束下载的位置
                int start = block * i;
                int end = block * (i + 1) - 1; // 长度是从 0 开始的
                if (i == mThreadCount - 1) {
                    end = length - 1; //最后一个除不尽，则用文件的总大小填进去
                }

                ThreadInfo info = new ThreadInfo(i, mFileInfo.getUrl(),
                        start, end, 0);
                list.add(info);
            }
        }

        mThreads = new ArrayList<DownloadThread>();
        for (ThreadInfo info : list) {
            DownloadThread thread = new DownloadThread(info);
            // 使用线程池开始下载
            sExecutorService.execute(thread);
            mThreads.add(thread);
            // 数据库不存在则加入
            if (!mDao.isExists(info.getUrl(),info.getId())) {
                mDao.insertThread(info);
            }
        }
    }


    class DownloadThread extends Thread {
        private ThreadInfo threadInfo = null;
        // 标识线程是否执行完毕
        public boolean isFinished = false;

        public DownloadThread(ThreadInfo threadInfo) {
            this.threadInfo = threadInfo;
        }

        @Override
        public void run() {
            // super.run();
            Log.i("DownloadThread","run==============");
            HttpURLConnection conn = null;
            RandomAccessFile raf = null;
            InputStream is = null;
            try {
                URL url = new URL(mFileInfo.getUrl());
                conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(5 * 1000);
                conn.setRequestMethod("GET");

                int start = threadInfo.getStart() + threadInfo.getFinished();
                Log.i("DownloadThread","run==============start: " + start
                + " ==============end: " + threadInfo.getEnd());
                // 設置下載文件開始到結束的位置
                conn.setRequestProperty("Range", "bytes=" + start + "-" + threadInfo.getEnd());
                File file = new File(DOWNLOAD_PATH, mFileInfo.getFileName());
                raf = new RandomAccessFile(file, "rwd");
                raf.seek(start);
                mFinished += threadInfo.getFinished();
                Intent intent = new Intent();
                intent.setAction(DownloadService.ACTION_UPDATE);
                int code = conn.getResponseCode();
                Log.i("DownloadThread","run==============code: " + code);
                if (code == HttpURLConnection.HTTP_OK || code == HttpURLConnection.HTTP_PARTIAL) {
                    is = conn.getInputStream();
                    byte[] bt = new byte[1024];
                    int len = -1;
                    // 定义UI刷新时间
                    long time = System.currentTimeMillis();
                    while ((len = is.read(bt)) != -1) {
                        raf.write(bt, 0, len);
                        // 累计整个文件完成进度
                        mFinished += len;
                        // 累加每个线程完成的进度
                        threadInfo.setFinished(threadInfo.getFinished() + len);
                        // 設置爲500毫米更新一次
                        if (System.currentTimeMillis() - time > 1000) {
                            time = System.currentTimeMillis();
                            // 发送已完成多少
                            intent.putExtra("finished", mFinished * 100 / mFileInfo.getLength());
                            // 表示正在下载文件的id
                            intent.putExtra("id", mFileInfo.getId());
                            Log.i("DownloadTask", "=========== "+mFinished * 100 / mFileInfo.getLength() + "");
                            // 發送廣播給Activity
                            mComtext.sendBroadcast(intent);
                        }
                        if (mIsPause) {
                            mDao.updateThread(threadInfo.getUrl(), threadInfo.getId(), threadInfo.getFinished());
                            return;
                        }
                    }
                }
                // 标识线程是否执行完毕
                isFinished = true;
                // 判断是否所有线程都执行完毕
                checkAllFinished();

            } catch (Exception e) {
                e.printStackTrace();
                Log.i("DownloadThread","Exception=============="+e.getMessage());
            } finally {
                if (conn != null) {
                    conn.disconnect();
                }
                try {
                    if (is != null) {
                        is.close();
                    }
                    if (raf != null) {
                        raf.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }


    public synchronized void checkAllFinished() {
        boolean allFinished = true;
        for (DownloadThread thread : mThreads) {
            if (!thread.isFinished) {
                allFinished = false;
                break;
            }
        }
        if (allFinished) {
            // 下載完成后，刪除數據庫信息
            mDao.deleteThread(mFileInfo.getUrl());
            // 通知UI哪个线程完成下载
            Intent intent = new Intent(DownloadService.ACTION_FINISHED);
            intent.putExtra(DownloadService.FILE_INFO, mFileInfo);
            mComtext.sendBroadcast(intent);

        }
    }

}

package com.meirenmeitu.net.download;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import androidx.annotation.Nullable;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Map;

/** https://www.jianshu.com/p/85913ed97af5 (使用系统  DownloadManager  )
 * Created by Jooyer on 2018/6/14
 */

public class DownloadService extends Service {

    public static final String ACTION_START = "ACTION_START";
    public static final String ACTION_STOP = "ACTION_STOP";
    public static final String ACTION_UPDATE = "ACTION_UPDATE";
    public static final String ACTION_FINISHED = "ACTION_FINISHED";
    public static final String FILE_INFO = "FILE_INFO";
    private static final int MSG_INIT = 1;

    private Map<Integer, DownloadTask> mTasks = new LinkedHashMap<Integer, DownloadTask>();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("DownloadService", "onCreate==============");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (ACTION_START.equals(intent.getAction())) {
            FileInfo fileInfo = (FileInfo) intent.getSerializableExtra(FILE_INFO);
            Log.i("DownloadService", "START===========" + fileInfo.getLength());
            InitThread initThread = new InitThread(fileInfo);
            DownloadTask.sExecutorService.execute(initThread);
        } else if (ACTION_STOP.equals(intent.getAction())) {
            FileInfo fileInfo = (FileInfo) intent.getSerializableExtra(FILE_INFO);
            Log.i("DownloadService", "STOP===========" + (null == fileInfo));
            DownloadTask task = mTasks.get(fileInfo.getId());
            if (task != null) {
                // 停止下载任务
                task.mIsPause = true;
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    // 從InitThread綫程中獲取FileInfo信息，然後開始下載任務
    private DownHandler mHandler = new DownHandler(this);
//    Handler mHandler = new Handler() {
//        public void handleMessage(Message msg) {
//            switch (msg.what) {
//                case MSG_INIT:
//                    FileInfo fileInfo = (FileInfo) msg.obj;
//                    Log.i("DownloadService", "INIT===========" + fileInfo.getLength());
//                    // 獲取FileInfo對象，開始下載任務
//                    DownloadTask task = new DownloadTask(DownloadService.this, fileInfo, 3);
//                    task.download();
//                    // 把下载任务添加到集合中
//                    mTasks.put(fileInfo.getId(), task);
//                    // 发送启动下载的通知
//                    Intent intent = new Intent(ACTION_START);
//                    intent.putExtra(DownloadService.FILE_INFO, fileInfo);
//                    sendBroadcast(intent);
//                    break;
//            }
//        }
//    };

    static class DownHandler extends Handler {
        WeakReference<DownloadService> mServiceWeakReference;

        DownHandler(DownloadService service) {
            mServiceWeakReference = new WeakReference<DownloadService>(service);
        }

        @Override
        public void handleMessage(Message msg) {
            DownloadService service = mServiceWeakReference.get();
            if (null != service) {
                switch (msg.what) {
                    case MSG_INIT:
                        FileInfo fileInfo = (FileInfo) msg.obj;
                        Log.i("DownloadService", "INIT===========" + fileInfo.getLength());
                        // 獲取FileInfo對象，開始下載任務
                        DownloadTask task = new DownloadTask(service, fileInfo, 3);
                        task.download();
                        // 把下载任务添加到集合中
                        service.mTasks.put(fileInfo.getId(), task);
                        // 发送启动下载的通知
                        Intent intent = new Intent(ACTION_START);
                        intent.putExtra(DownloadService.FILE_INFO, fileInfo);
                        service.sendBroadcast(intent);
                        break;
                }
            }
        }
    }

    // 初始化下載綫程，獲得下載文件的信息
    class InitThread extends Thread {
        private FileInfo mFileInfo = null;

        public InitThread(FileInfo mFileInfo) {
            super();
            this.mFileInfo = mFileInfo;
        }

        @Override
        public void run() {
            HttpURLConnection conn = null;
            RandomAccessFile raf = null;
            try {
                URL url = new URL(mFileInfo.getUrl());
                conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(5 * 1000);
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Accept-Encoding", "identity");   // 加上这句话解决问题 返回 length = 0
                conn.connect();
                int code = conn.getResponseCode();
                int length = -1;
                if (code == HttpURLConnection.HTTP_OK) {
                    length = conn.getContentLength();
                }
                //如果文件长度为小于0，表示获取文件失败，直接返回
                if (length <= 0) {
                    return;
                }
                // 判斷文件路徑是否存在，不存在這創建
                File dir = new File(DownloadTask.DOWNLOAD_PATH);
                if (!dir.exists()) {
                    dir.mkdir();
                }
                // 創建本地文件
                File file = new File(dir, mFileInfo.getFileName());
                raf = new RandomAccessFile(file, "rwd");
                raf.setLength(length);
                // 設置文件長度
                mFileInfo.setLength(length);
                // 將FileInfo對象傳遞給Handler
                Message msg = Message.obtain();
                msg.obj = mFileInfo;
                msg.what = MSG_INIT;
                mHandler.sendMessage(msg);
//				msg.setTarget(mHandler);
            } catch (Exception e) {
                Log.i("InitThread", "Exception==============" + e.getMessage());
                e.printStackTrace();
            } finally {
                if (conn != null) {
                    conn.disconnect();
                }
                try {
                    if (raf != null) {
                        raf.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            super.run();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (!mTasks.isEmpty()) {
            for (Map.Entry<Integer, DownloadTask> entry : mTasks.entrySet()) {
                entry.getValue().close();
            }
        }
    }
}

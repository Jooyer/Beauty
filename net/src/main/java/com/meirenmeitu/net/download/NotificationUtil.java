package com.meirenmeitu.net.download;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.widget.RemoteViews;
import androidx.core.app.NotificationCompat;
import com.meirenmeitu.net.R;

import java.io.File;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by Jooyer on 2018/6/14
 * ${DESCRIPTION}
 */

public class NotificationUtil {

    private Context mContext;
    private NotificationManager mNotificationManager = null;
    private Map<Integer, Notification> mNotifications = null;
    private NotificationChannel mNotificationChannel;


    private Notification.Builder mBuilder;
    private   NotificationCompat.Builder mCompatBuilder;
    public static final int notificationId = 0x1234;

    public void show(int progress, File file) {
        if (Build.VERSION.SDK_INT >= 26) {
            mBuilder.setProgress(100, progress, false);
            mBuilder.setContentIntent(progress >= 100 ? getContentIntent(file) :
                    PendingIntent.getActivity(mContext, 0, new Intent(),
                            PendingIntent.FLAG_UPDATE_CURRENT));
            Notification notification = mBuilder.build();
            mNotificationManager.notify(notificationId, notification);
        } else {
            mCompatBuilder.setProgress(100, progress, false);
            mCompatBuilder.setContentIntent(progress >= 100 ? getContentIntent(file) :
                    PendingIntent.getActivity(mContext, 0, new Intent(),
                            PendingIntent.FLAG_UPDATE_CURRENT));
            Notification notification = mCompatBuilder.build();
            mNotificationManager.notify(Integer.MAX_VALUE, notification);
        }
    }

    public void show(FileInfo info) {
        if (Build.VERSION.SDK_INT >= 26) {
            if (mNotificationChannel == null) {
                //创建 通知通道  channelid和channelname是必须的（自己命名就好）
                mNotificationChannel = new NotificationChannel("cn_molue_reader",
                        "cn_molue_reader_download", NotificationManager.IMPORTANCE_HIGH);
                mNotificationChannel.enableLights(true);//是否在桌面icon右上角展示小红点
                mNotificationChannel.setLightColor(Color.GREEN);//小红点颜色
                mNotificationChannel.setShowBadge(true); //是否在久按桌面图标时显示此渠道的通知
                mNotificationManager.createNotificationChannel(mNotificationChannel);
            }

            mBuilder = new Notification.Builder(mContext, "1");
            mBuilder.setOnlyAlertOnce(true);
            mBuilder.setSmallIcon(R.drawable.logo)
                    .setContentText("正在下载新版本，请稍后...")
                    .setAutoCancel(true);
            mBuilder.setProgress(100, 0, false);
            Notification notification = mBuilder.build();
            mNotificationManager.notify(info.getId(), notification);
        } else {
            mCompatBuilder = new NotificationCompat
                    .Builder(mContext, "cn_molue_reader");
            mCompatBuilder.setSmallIcon(R.drawable.logo)
                    .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.logo))
                    .setContentTitle(mContext.getResources().getString(R.string.app_name));
            mCompatBuilder.setProgress(100, 0, false);
            mCompatBuilder.setAutoCancel(true);
            mCompatBuilder.setWhen(System.currentTimeMillis());

            Notification notification = mCompatBuilder.build();
            mNotificationManager.notify(Integer.MAX_VALUE, notification);
        }
    }

    /**
     * 进入安装
     *
     * @return
     */
    private PendingIntent getContentIntent(File file) {

        mNotificationManager.cancelAll();

        //移除通知栏
        if (Build.VERSION.SDK_INT >= 26) {
            mNotificationManager.deleteNotificationChannel("cn_molue_reader");
        } else {
            mNotificationManager.cancel(Integer.MAX_VALUE);
        }

//        Intent install = new Intent("android.intent.action.VIEW");
//        install.addCategory("android.intent.category.DEFAULT");
        Intent install = new Intent();

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            install.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            install.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); //添加这一句表示对目标应用临时授权该Uri所代表的文件
//            // context.getPackageName() + ".fileProvider"  是配置中的authorities
//            Uri contentUri = FileProvider.getUriForFile(mContext, mContext.getPackageName()
//                    + ".fileProvider", file);
//            install.setDataAndType(contentUri, "application/vnd.android.package-archive");
//        } else {
//            install.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
//        }

        //跳转到系统的安装应用页面
        PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0, install, PendingIntent.FLAG_CANCEL_CURRENT);
//        mContext.startActivity(install);
        return pendingIntent;
    }


    public NotificationUtil(Context context) {
        this.mContext = context;
        // 获得系统通知管理者
        mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        // 创建通知的集合
        mNotifications = new HashMap<Integer, Notification>();
    }

    /**
     * 显示通知栏
     *
     * @param fileInfo
     */
    public void showNotification(FileInfo fileInfo, int icon) {
        // 判断通知是否已经显示
        if (!mNotifications.containsKey(fileInfo.getId())) {
            Notification notification = new Notification();
            notification.tickerText = fileInfo.getFileName() + "开始下载";
            notification.when = System.currentTimeMillis();
            notification.icon = icon;
            notification.flags = Notification.FLAG_AUTO_CANCEL;

            // 点击通知之后的意图
//            Intent intent = new Intent(mContext, activity);
//            PendingIntent pd = PendingIntent.getActivity(mContext, 0, intent, 0);
//            download_app.contentIntent = pd;

            // 设置远程试图RemoteViews对象
            RemoteViews remoteViews = new RemoteViews(mContext.getPackageName(), R.layout.download_app);
            // 控制远程试图，设置开始点击事件
            Intent intentStart = new Intent(mContext, DownloadService.class);
            intentStart.setAction(DownloadService.ACTION_START);
            intentStart.putExtra(DownloadService.FILE_INFO, fileInfo);
            PendingIntent piStart = PendingIntent.getService(mContext, 0, intentStart, 0);
            remoteViews.setOnClickPendingIntent(R.id.start_button, piStart);

            // 控制远程试图，设置结束点击事件
            Intent intentStop = new Intent(mContext, DownloadService.class);
            intentStop.setAction(DownloadService.ACTION_STOP);
            intentStop.putExtra(DownloadService.FILE_INFO, fileInfo);
            PendingIntent piStop = PendingIntent.getService(mContext, 0, intentStop, 0);
            remoteViews.setOnClickPendingIntent(R.id.stop_button, piStop);
            // 设置TextView中文件的名字
            remoteViews.setTextViewText(R.id.file_textview, fileInfo.getFileName());
            // 设置Notification的视图
            notification.contentView = remoteViews;
            // 发出Notification通知
            mNotificationManager.notify(fileInfo.getId(), notification);
            // 把Notification添加到集合中
            mNotifications.put(fileInfo.getId(), notification);
        }
    }

    /**
     * 取消通知栏通知
     */
    public void cancelNotification(int id) {
        updataNotification(id, 100);
        mNotificationManager.cancel(id);
        mNotifications.remove(id);
    }

    /**
     * 更新通知栏进度条
     *
     * @param id       获取Notification的id
     * @param progress 获取的进度
     */
    public void updataNotification(int id, int progress) {
        Notification notification = mNotifications.get(id);
        if (notification != null) {
            // 修改进度条进度
            notification.contentView.setProgressBar(R.id.progressBar2, 100, progress, false);
            mNotificationManager.notify(id, notification);
        }
    }

}

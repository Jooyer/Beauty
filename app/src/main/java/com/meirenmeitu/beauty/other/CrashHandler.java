package com.meirenmeitu.beauty.other;

import android.content.Context;
import android.content.Intent;
import android.os.Looper;
import android.widget.Toast;
import com.meirenmeitu.beauty.view.MainActivity;

/**https://www.jianshu.com/p/fb28a5322d8a ( https://github.com/shenhuniurou/BlogDemos )
 * Desc:  App 异常时重启
 * Author: Jooyer
 * Date: 2018-11-28
 * Time: 23:05
 */
public class CrashHandler implements Thread.UncaughtExceptionHandler {

    private Context context;

    public CrashHandler(Context context) {
        this.context = context;
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        showToast(t);
    }

    /**
     * 操作
     *
     * @param thread
     */
    private void showToast(Thread thread) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                Toast.makeText(context, "程序异常，重新启动", Toast.LENGTH_LONG).show();
                Looper.loop();
            }
        }).start();

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        restartApp();
    }

    /**
     * 重启应用
     * https://blog.csdn.net/youmangu/article/details/81118609 --> 重启两种方式
     */
    private void restartApp() {
        Intent intent = new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
        android.os.Process.killProcess(android.os.Process.myPid());//再此之前可以做些退出等操作
    }
}


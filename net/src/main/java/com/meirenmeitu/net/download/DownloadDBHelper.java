package com.meirenmeitu.net.download;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Jooyer on 2018/6/14
 *
 */

public class DownloadDBHelper extends SQLiteOpenHelper {
    private final static String DOWN_DB_NAME = "download.db";

    private static final String SQL_CREATE = "create table thread_info(_id integer primary key autoincrement, "
            + "thread_id integer, url text, start integer, ended integer, finished integer)";
    private static final String SQL_DROP = "drop table if exists thread_info";

    private volatile static DownloadDBHelper INSTANCE;
    private DownloadDBHelper(Context context) {
        super(context, DOWN_DB_NAME, null, 1);
    }

    public static DownloadDBHelper getInstance(Context context) {
        if (null == INSTANCE) {
            synchronized (DownloadDBHelper.class) {
                if (null == INSTANCE) {
                    INSTANCE = new DownloadDBHelper(context);
                }
            }
        }
        return INSTANCE;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DROP);
        db.execSQL(SQL_CREATE);
    }
}

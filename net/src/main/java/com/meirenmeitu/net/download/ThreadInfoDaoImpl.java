package com.meirenmeitu.net.download;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jooyer on 2018/6/14
 */

public class ThreadInfoDaoImpl implements ThreadInfoDao {
    private final static String DOWN_DB_TABLE_NAME = "thread_info";
    private DownloadDBHelper mDBHelper;

    public ThreadInfoDaoImpl(Context context) {
        mDBHelper = DownloadDBHelper.getInstance(context);
    }

    @Override
    public void insertThread(ThreadInfo info) {
        SQLiteDatabase db = mDBHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("thread_id", info.getId());
        values.put("url", info.getUrl());
        values.put("start", info.getStart());
        values.put("ended", info.getEnd());
        values.put("finished", info.getFinished());
        db.insert(DOWN_DB_TABLE_NAME, null, values);
        mDBHelper.close();
    }

    @Override
    public void deleteThread(String url) {
        mDBHelper.getWritableDatabase().delete(DOWN_DB_TABLE_NAME,
                "url = ?", new String[]{url});
        mDBHelper.close();
    }

    @Override
    public void updateThread(String url, int thread_id, int finished) {
        SQLiteDatabase db = mDBHelper.getWritableDatabase();
        db.execSQL("update " + DOWN_DB_TABLE_NAME + " set finished = ? where url = ? and thread_id = ?"
                , new Object[]{finished, url, thread_id});
    }

    private Cursor allCursor = null;
    @Override
    public List<ThreadInfo> queryThreads(String url) {
        SQLiteDatabase db = mDBHelper.getWritableDatabase();
        List<ThreadInfo> list = new ArrayList<>();
         allCursor = db.query(DOWN_DB_TABLE_NAME, null, "url = ?",
                new String[]{url}, null, null, null);

        while (allCursor.moveToNext()) {
            ThreadInfo info = new ThreadInfo();
            info.setId(allCursor.getInt(allCursor.getColumnIndex("thread_id")));
            info.setUrl(allCursor.getString(allCursor.getColumnIndex("url")));
            info.setStart(allCursor.getInt(allCursor.getColumnIndex("start")));
            info.setEnd(allCursor.getInt(allCursor.getColumnIndex("ended")));
            info.setFinished(allCursor.getInt(allCursor.getColumnIndex("finished")));
            list.add(info);
        }
        mDBHelper.close();
        return list;
    }

    private Cursor mCursor;
    @Override
    public boolean isExists(String url, int threadId) {
        SQLiteDatabase db = mDBHelper.getWritableDatabase();
        mCursor = db.query(DOWN_DB_TABLE_NAME, null, "url = ? and thread_id = ?",
                new String[]{url, threadId + ""}, null, null, null);
        boolean exist = mCursor.moveToNext();
        mDBHelper.close();
        return exist;
    }

    public void close(){
        if (null != allCursor && !allCursor.isClosed()){
            allCursor.close();
        }

        if (null != mCursor && !mCursor.isClosed()){
            mCursor.close();
        }
    }


}

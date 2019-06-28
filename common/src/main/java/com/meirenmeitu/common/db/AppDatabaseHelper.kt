package com.meirenmeitu.common.db

import android.content.Context
import androidx.room.Room

/**  https://www.jianshu.com/p/72c8efc3ad87  --> api 简介
 * Desc: 数据库操作类
 * Author: Jooyer
 * Date: 2018-11-07
 * Time: 14:54
 */
class AppDatabaseHelper private constructor(context: Context) {

    companion object {
        @Volatile
        private var INSTANCE: AppDatabaseHelper? = null

        fun getInstance(context: Context): AppDatabaseHelper {
            if (null == INSTANCE) {
                synchronized(AppDatabaseHelper::class) {
                    if (null == INSTANCE) {
                        INSTANCE = AppDatabaseHelper(context)
                    }
                }
            }
            return INSTANCE!!
        }
    }

    private val dataBase = Room.databaseBuilder(context, AppDatabase::class.java, "molue.db")
            .allowMainThreadQueries()  // Room不允许在主线程中访问数据库，除非在 build 的时候使用allowMainThreadQueries()方法
//            .addMigrations() // TODO
            .build()

    fun database(): AppDatabase {
        return dataBase
    }

}
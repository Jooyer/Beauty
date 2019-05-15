package com.meirenmeitu.common.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.meirenmeitu.common.bean.NotifyInfo
import com.meirenmeitu.common.bean.UserInfoBean

/** 如果需要 Room 存数组或者集合等, 可以自定义 Converters,请看 @link{ Converters.kt }
 * Desc: 数据库
 * Author: Jooyer
 * Date: 2018-11-07
 * Time: 14:43
 */
@Database(entities = [ UserInfoBean::class, NotifyInfo::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getUserInfoDao(): UserInfoDao

    abstract fun getNotifyInfoDao(): NotifyInfoDao

}
package com.meirenmeitu.common.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.meirenmeitu.common.bean.NotifyInfo

/**
 * Desc:
 * Author: Jooyer
 * Date: 2018-11-25
 * Time: 22:46
 */
@Dao
interface NotifyInfoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(notifyInfo: NotifyInfo): Long

    @Query("UPDATE notifyInfo SET notify_state = :notifyState WHERE id = :notifyId")
    fun updateNotifyId(notifyId: String, notifyState: Int)

}
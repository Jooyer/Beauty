package com.meirenmeitu.common.bean

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


/**
 * Desc:
 * Author: Jooyer
 * Date: 2018-11-25
 * Time: 22:33
 */
@Entity(tableName = "notifyInfo")
data class NotifyInfo(

        @PrimaryKey(autoGenerate = true)
        @ColumnInfo(name = "id")
        var id: Int = 0,
        @ColumnInfo(name = "notify_icon")
        var notifyIcon: String = "",
        // 标题
        @ColumnInfo(name = "notify_title")
        var notifyTitle: String = "",
        // 内容
        @ColumnInfo(name = "notify_content")
        var notifyContent: String = "",
        // 0 --> 未读, 1 --> 已读
        @ColumnInfo(name = "notify_state")
        var notifyState: Int = 0,
        @ColumnInfo(name = "update_time")
        var updateTime: String = ""

)

package com.meirenmeitu.common.other

import androidx.room.TypeConverter
import java.util.*

/** https://www.jianshu.com/p/d5007e377ffb  -->  因为sql中不能插入数组这样类型数据，就需要使用到了类型转换器了
 * Desc: Room 中 类型转换器
 * Author: Jooyer
 * Date: 2018-11-07
 * Time: 12:42
 */
class Converters {

    @TypeConverter
    fun fromTimestamp(value: Long = 0): Date {
        return if (0L == value) Date() else Date(value)
    }

    @TypeConverter
    fun dateToTimeStamp(date: Date?): Long {
        return date?.time ?: 0
    }

}
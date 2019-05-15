package com.meirenmeitu.common.bean

import androidx.room.*
import com.meirenmeitu.common.other.Converters
import com.meirenmeitu.library.utils.TimeUtil


/**
 * Desc: 用户信息
 * Author: Jooyer
 * Date: 2018-11-09
 * Time: 18:28
 */
@Entity(tableName = "userInfo")
@TypeConverters(Converters::class)
class UserInfoBean {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int = 0

    @ColumnInfo(name = "user_id")
    var userId: String = ""

    @ColumnInfo(name = "user_name")
    var userName: String = ""

    @ColumnInfo(name = "user_mobile")
    var userMobile: String = ""

    @ColumnInfo(name = "user_gender")
    var userGender: Int = -1

    @ColumnInfo(name = "need_update")
    var needUpdate: Int = 0 // 0 --> 不需要, 1 --> 需要

    @ColumnInfo(name = "user_token")
    var userToken: String = ""

    // 记录当前天 (如 3月1日  那么 curDay = 1,3月29日 则 curDay = 29 ),辅助计算每天阅读时长
    @ColumnInfo(name = "cur_day")
    var curDay: Int = 0

    // 每天阅读时长
    @ColumnInfo(name = "read_time")
    var readTime: Int = 0

    // 是否签到
    @ColumnInfo(name = "cur_sign")
    var curSign: Int = 0 // 0 --> 今儿未签到, 1 --> 今儿已签到

    // 获取下一天零点时间
    @ColumnInfo(name = "next_time")
    var nextTime: Long = TimeUtil.getNextDayUnicodeTime()

    // 名言
    @ColumnInfo(name = "well_known")
    var wellKnown: String = ""
    // 嵌套类 ( https://www.jianshu.com/p/7354d5048597  ||  https://blog.csdn.net/wuyuxing24/article/details/80773119 )
    @Embedded
    var signInfo: SignBean? = null

    override fun toString(): String {
        return "UserInfoBean(id=$id, userId='$userId', userName='$userName', userMobile='$userMobile', userGender=$userGender, needUpdate=$needUpdate, userToken='$userToken', curDay=$curDay, readTime=$readTime, curSign=$curSign, nextTime=$nextTime, wellKnown='$wellKnown', signInfo=$signInfo)"
    }


}
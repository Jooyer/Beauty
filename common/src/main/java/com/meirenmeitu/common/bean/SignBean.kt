package com.meirenmeitu.common.bean

import androidx.room.ColumnInfo


/**
 * Desc:
 * Author: Jooyer
 * Date: 2018-11-12
 * Time: 16:25
 */

class SignBean {
//    @PrimaryKey(autoGenerate = true)
//    @ColumnInfo(name = "id")
//    var id: Int = 0
    @ColumnInfo(name = "sign_id")
//    var signId: String = ""
    var signId: Int = 0
    @ColumnInfo(name = "sign_month")
    var signMonth: String = ""
    @ColumnInfo(name = "get_gift")
    var getGift: String = ""
    @ColumnInfo(name = "cur_month")
    var curMonth: Int = 0

    override fun toString(): String {
        return "SignBean(signId='$signId', signMonth='$signMonth', getGift='$getGift', curMonth=$curMonth)"
    }


}

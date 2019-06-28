package com.meirenmeitu.common.db

import androidx.room.*
import com.meirenmeitu.common.bean.UserInfoBean

/**
 * Desc: 用户信息
 * Author: Jooyer
 * Date: 2018-11-09
 * Time: 18:26
 */
@Dao
interface UserInfoDao {

    // 当产生冲突时，默认情况下为OnConflictStrategy.ABORT会导致崩溃，这里设置为OnConflictStrategy.REPLACE，当发生冲突时替换老数据
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(userInfo: UserInfoBean):Long

    @Query("SELECT * FROM userInfo WHERE user_id = :userId")
    fun queryUserInfoByUserId(userId: String): UserInfoBean?

    @Query("SELECT * FROM userInfo WHERE user_mobile = :userMobile")
    fun queryUserInfoByUserMobile(userMobile: String): UserInfoBean?

    @Query("SELECT * FROM userInfo WHERE user_token = :userToken")
    fun queryUserInfoByUserToken(userToken: String): UserInfoBean?

    @Update
    fun update(userInfo: UserInfoBean)

    @Query("UPDATE userInfo SET need_update = :needUpdate WHERE user_id = :userId")
    fun updateByUserId(userId: String, needUpdate:Int)

    // 删除某一个
    @Delete
    fun delete(userInfo: UserInfoBean)

    //根据名字删除
    @Query("DELETE  FROM userInfo where user_id=:userId")
    fun deleteByUserId(userId: String)

    //删全部
    @Query("DELETE FROM userInfo")
    fun deleteAll()


}
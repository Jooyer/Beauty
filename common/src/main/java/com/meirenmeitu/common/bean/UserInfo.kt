package com.meirenmeitu.common.bean


/**
 * Desc:
 * Author: Jooyer
 * Date: 2019-06-24
 * Time: 16:48
 */
class UserInfo {
    var id: Int = 0
    var userId: String = ""
    var token: String = ""
    var userAvatar: Int = 0
    var userName: String = ""
    var userMobile: String = ""
    var organization: String = ""
    var cityName: String = ""
    var academicDegree: String = ""
    var profession: String = ""
    var coinCount: Int = 0
    var openWorkState: Int = 0
    var profileState: Int = 0
    override fun toString(): String {
        return "UserInfo(id=$id, userId='$userId', token='$token', userAvatar=$userAvatar, userName='$userName', userMobile='$userMobile', organization='$organization', cityName='$cityName', academicDegree='$academicDegree', profession='$profession', coinCount=$coinCount, openWorkState=$openWorkState, profileState=$profileState)"
    }


}
/*


/**
 * 用户ID,也是IM账号
 */
private val userId: String? = null
/**
 * 用户名
 */
private val userName: String? = null
/**
 * 用户手机号
 */
private val userMobile: String? = null
/**
 * 科研机构
 */
private val organization: String? = null
/**
 * 所在城市
 */
private val cityName: String? = null
/**
 * 学位
 */
private val academicDegree: String? = null
/**
 * 专业
 */
private val profession: String? = null
/**
 * 金币数量
 */
private val coinCount: Int = 0
/**
 * 是否开启咨询
 * 1 --> 开启, 2 --> 关闭
 */
private val openWorkState: Int = 0
 /**
* 登录后资料是否完善
* 1 --> 未完善, 2 --> 完善
*/
private int profileState;


 */

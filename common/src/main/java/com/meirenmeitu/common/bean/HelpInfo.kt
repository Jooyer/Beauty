package com.meirenmeitu.common.bean

import com.google.gson.annotations.Expose

/** @Expose 在对象中把字段用@Expose 注解，这样其他没有用此注解的字段通通都会过滤掉
 * Desc: 求助相关
 * Author: Jooyer
 * Date: 2019-06-27
 * Time: 18:45
 */
class HelpInfo {
    /**
     * 求助ID
     */
    var helpId: String = ""
    /**
     * 区分求助类型的,从1开始,1(化学试剂),2(生物制剂),3(细胞),4(动物),5(其他)
     */
    @Expose
    var helpType: Int = 0
    /**
     * 封面图片类型,从1开始,和上面对应
     */
    var helpCover: Int = 0
    /**
     * 求助标题
     */
    var helpTitle: String = "" //TODO 标题就是中文名/英文名
    /**
     * 中文名
     */
    @Expose
    var cnName: String = ""
    /**
     * 英文名
     */
    @Expose
    var engName: String = ""
    /**
     * cas号
     */
    @Expose
    var casCode: String = ""
    /**
     * 重量
     */
    @Expose
    var weight: Int = 0
    /**
     * 种属
     */
    @Expose
    var species: Int = 0
    /**
     * 收货地址
     */
    @Expose
    var address: String = ""
    /**
     * 科研机构
     */
    @Expose
    var organization: String = ""
    /**
     * 取货方式,三种方式,默认1
     */
    @Expose
    var gotFunc: Int = 0
    /**
     * 备注
     */
    @Expose
    var remark: String = ""
    /**
     * 金币数量
     */
    @Expose
    var coinCount: Int = 0

    // TODO 以下三个实际上需要上传的,目前登录未完成,没有数据
    /**
     * 用户ID,也是IM账号
     */
    @Expose
    var userId: String = ""
    /**
     * 用户名
     */
    @Expose
    var userName: String = ""
    /**
     * 用户头像
     */
    @Expose
    var userAvatar: Int = 0

    var createTime: String = ""

}

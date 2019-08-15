package com.meirenmeitu.beauty.model

import com.meirenmeitu.beauty.net.AppService
import com.meirenmeitu.common.bean.HelpInfo
import com.meirenmeitu.common.bean.UserInfo
import com.meirenmeitu.common.other.RxObserver
import com.meirenmeitu.net.response.Response
import com.meirenmeitu.net.retrofit.RxRetrofit
import com.meirenmeitu.net.utils.CallBack
import com.meirenmeitu.ui.mvp.BaseModel

/**
 * @Project 晓可骑士.
 * @Author 刘一召
 * @Date  2019/2/12
 * @Time 10:51
 * @Desc
 * @Copyright 2018 www.scshuimukeji.cn Inc. All rights reserved.
 * 注意: 本内容仅限于四川水木科技有限公司内部传阅，禁止外泄以及用于其他的商业目的
 */
class ADModel() : BaseModel() {

    fun logins(phone: String, code: String, callback: CallBack<UserInfo>) {
        RxRetrofit.instance.getApi(AppService::class.java)
            .logins(phone, code)
            .compose(RxRetrofit.instance.maybeTransformer())
            .subscribe({callback.callback(it)},{callback.callError(203,"")})

    }

    fun publishHelp(info: HelpInfo, callback: RxObserver<Response>) {
        RxRetrofit.instance.getApi(AppService::class.java)
            .publishHelp(info)
            .compose(RxRetrofit.instance.maybeTransformer())
            .subscribe(callback)
    }

}
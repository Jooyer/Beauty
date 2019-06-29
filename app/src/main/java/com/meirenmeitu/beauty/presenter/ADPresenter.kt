package com.meirenmeitu.beauty.presenter

import com.meirenmeitu.beauty.model.ADModel
import com.meirenmeitu.beauty.view.ADActivity
import com.meirenmeitu.common.bean.HelpInfo
import com.meirenmeitu.common.bean.UserInfo
import com.meirenmeitu.net.cover.RxObserver
import com.meirenmeitu.net.response.Response
import com.meirenmeitu.net.utils.CallBack
import com.meirenmeitu.ui.mvp.BasePresenter
import io.reactivex.disposables.Disposable


/**
 * Desc:
 * Author: Jooyer
 * Date: 2018-08-19
 * Time: 22:57
 */
class ADPresenter(view: ADActivity) : BasePresenter<ADActivity, ADModel>(view,ADModel()) {

    fun logins(phone: String, code: String){
        mModel.logins(phone,code,object : CallBack<UserInfo> {
            override fun callback(data: UserInfo) {
                println("============ ${data.toString()}")
            }

        })
    }


    fun publishHelp( info: HelpInfo) {
        mModel.publishHelp(info,object : RxObserver<Response>(){
            override fun error(code: Int, msg: String) {
                println("error============ $msg")
            }

            override fun getDisposable(d: Disposable) {

            }

            override fun success(t: Response) {
                println("success============ ${t.toString()}")
            }
        })

    }

}
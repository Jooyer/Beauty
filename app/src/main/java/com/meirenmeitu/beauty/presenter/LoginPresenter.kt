package com.meirenmeitu.beauty.presenter

import com.meirenmeitu.beauty.model.LoginModel
import com.meirenmeitu.beauty.view.LoginActivity
import com.meirenmeitu.ui.mvp.BasePresenter


/**
 * Desc:
 * Author: Jooyer
 * Date: 2018-09-26
 * Time: 17:25
 */
class LoginPresenter(view: LoginActivity) : BasePresenter<LoginActivity, LoginModel>(view, LoginModel()) {

    fun login(mobile: String, code: String) {

    }


}
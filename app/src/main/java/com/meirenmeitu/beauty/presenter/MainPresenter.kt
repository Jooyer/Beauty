package com.meirenmeitu.beauty.presenter

import com.meirenmeitu.beauty.model.MainModel
import com.meirenmeitu.beauty.view.MainActivity
import com.meirenmeitu.ui.mvp.BasePresenter

/**
 * @Project 晓可骑士.
 * @Author 刘一召
 * @Date  2019/2/11
 * @Time 17:50
 * @Desc
 * @Copyright 2018 www.scshuimukeji.cn Inc. All rights reserved.
 * 注意: 本内容仅限于四川水木科技有限公司内部传阅，禁止外泄以及用于其他的商业目的
 */
class MainPresenter(view: MainActivity) : BasePresenter<MainActivity, MainModel>(view, MainModel()) {

}
package com.meirenmeitu.beauty.model

import com.meirenmeitu.beauty.bean.ImageBean
import com.meirenmeitu.beauty.net.AppService
import com.meirenmeitu.net.retrofit.RxRetrofit
import com.meirenmeitu.net.utils.CallBack
import com.meirenmeitu.ui.mvp.BaseModel
import org.jetbrains.annotations.NotNull

/**
 * @Project 晓可骑士.
 * @Author 刘一召
 * @Date  2019/2/12
 * @Time 10:51
 * @Desc
 * @Copyright 2018 www.scshuimukeji.cn Inc. All rights reserved.
 * 注意: 本内容仅限于四川水木科技有限公司内部传阅，禁止外泄以及用于其他的商业目的
 */
class CategoryModel : BaseModel() {

    fun getImages(@NotNull tag: String, type: Int, callback: CallBack<List<ImageBean>>) {
        mRequestList[tag] = RxRetrofit.instance.getApi(AppService::class.java)
            .getImages(type)
            .compose(RxRetrofit.instance.maybeTransformer())
            .subscribe({ callback.callback(it) },
                { callback.callError(msg = it.message ?: "") })

    }

}
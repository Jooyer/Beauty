package com.meirenmeitu.beauty.presenter

import androidx.lifecycle.MutableLiveData
import com.meirenmeitu.beauty.bean.ImageBean
import com.meirenmeitu.beauty.model.CategoryModel
import com.meirenmeitu.beauty.view.CategoryFragment
import com.meirenmeitu.net.utils.CallBack
import com.meirenmeitu.ui.mvp.BasePresenter

/**
 * @Project 晓可骑士.
 * @Author 刘一召
 * @Date  2019/2/12
 * @Time 10:48
 * @Desc
 * @Copyright 2018 www.scshuimukeji.cn Inc. All rights reserved.
 * 注意: 本内容仅限于四川水木科技有限公司内部传阅，禁止外泄以及用于其他的商业目的
 */
class CategoryPresenter(view: CategoryFragment) :
    BasePresenter<CategoryFragment, CategoryModel>(view, CategoryModel()) {
    private val GET_SERIES_IMAGE_TAG = "GET_SERIES_IMAGE_TAG"
    private val GET_IMAGE_OF_SERIES_TAG = "GET_IMAGE_OF_SERIES_TAG"
    val mImages = MutableLiveData<List<ImageBean>>()
    val mImage = MutableLiveData<ImageBean>()

    fun getSeries(type: Int) {
        mModel.getSeries(GET_SERIES_IMAGE_TAG, type, object : CallBack<List<ImageBean>> {
            override fun callback(data: List<ImageBean>) {
                mImages.value = data
            }
        })
    }

    fun getOneImageInSeries(imageId: String, imageCode: String){
        mModel.getOneImageInSeries(GET_IMAGE_OF_SERIES_TAG,imageId,imageCode,object :CallBack<ImageBean>{
            override fun callback(data: ImageBean) {
                mImage.value = data
            }
        })
    }

    override fun onNetDisconnected() {
        mModel.mRequestList[GET_SERIES_IMAGE_TAG]?.let {
            if (!it.isDisposed) {
                it.dispose()
                mView.showError("网络加载失败...")
            }
        }

        mModel.mRequestList[GET_IMAGE_OF_SERIES_TAG]?.let {
            if (!it.isDisposed){
                it.dispose()
            }
        }

    }

}
package com.meirenmeitu.ui.mvp

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner


/**
 * Desc:
 * Author: Jooyer
 * Date: 2018-07-24
 * Time: 13:46
 */
abstract class BasePresenter<V : IBaseView, M : IBaseModel>(view: V,model:M) : IBasePresenter {
    val TAG = BasePresenter::class.java.simpleName
    var mView: V = view
    protected var mModel = model


    // 这个有问题,它会在 Activity/Fragment  的 onCreate() 执行完成后才回到,如果在 onCreate() 使用  mModel 就报空
    override fun onCreate(provider: LifecycleOwner) {
        //  mModel = onCreateModel()
    }

    override fun onResume(provider: LifecycleOwner) {
//        Log.i(TAG, "onResume===========${provider.lifecycle.currentState}")
    }

    override fun onDestroy(provider: LifecycleOwner) {
        mModel.cancelAllRequests()
    }

    override fun onLifecycleChanged(provider: LifecycleOwner, event: Lifecycle.Event) {
//        Log.i(TAG, "onLifecycleChanged===========${provider.lifecycle.currentState}")
    }

}
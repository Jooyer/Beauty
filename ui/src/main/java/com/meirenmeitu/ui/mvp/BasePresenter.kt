package com.meirenmeitu.ui.mvp

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import ccom.meirenmeitu.ui.network.NetStateChangeObserver
import com.meirenmeitu.net.network.NetWorkReceiver
import com.meirenmeitu.net.network.NetworkType


/**
 * Desc:
 * Author: Jooyer
 * Date: 2018-07-24
 * Time: 13:46
 */
abstract class BasePresenter<V : IBaseView, M : IBaseModel>(view: V,model:M) : IBasePresenter, NetStateChangeObserver {
    val TAG = BasePresenter::class.java.simpleName
    var mView: V = view
    protected var mModel = model


    // 这个有问题,它会在 Activity/Fragment  的 onCreate() 执行完成后才回到,如果在 onCreate() 使用  mModel 就报空
    override fun onCreate(provider: LifecycleOwner) {
        //  mModel = onCreateModel()
    }

    override fun onResume(provider: LifecycleOwner) {
        // 添加网络变化观察者
        NetWorkReceiver.INSTANCE.registerObserver(this)
//        Log.i(TAG, "onResume===========${provider.lifecycle.currentState}")
    }

    override fun onPause(provider: LifecycleOwner) {
        // 移除网络变化观察者
        NetWorkReceiver.INSTANCE.unRegisterObserver(this)
//        Log.i(TAG, "onPause===========${provider.lifecycle.currentState}")

    }

    override fun onDestroy(provider: LifecycleOwner) {
        mModel.cancelAllRequests()
    }

    override fun onLifecycleChanged(provider: LifecycleOwner, event: Lifecycle.Event) {
//        Log.i(TAG, "onLifecycleChanged===========${provider.lifecycle.currentState}")
    }

    /**
     * 网络正常
     */
    override fun onNetConnected(info: NetworkType) {
//        println("BasePresenter===============onNetConnected " + info.name)
    }

    /**
     * 无网
     */
    override fun onNetDisconnected() {
//        println("BasePresenter===============onNetDisconnected")
    }

}
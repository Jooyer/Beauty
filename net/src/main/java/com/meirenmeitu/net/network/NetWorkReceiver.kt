package com.meirenmeitu.net.network

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import ccom.meirenmeitu.ui.network.NetStateChangeObserver
import com.meirenmeitu.library.utils.Constants
import com.meirenmeitu.net.retrofit.RxRetrofit
import com.tencent.mmkv.MMKV


/**
 *  新的网络监听方式
 *  https://www.jianshu.com/p/e04aedd27b33
 *  https://www.jianshu.com/p/86d347b2a12b
 *
 * 广播监听
 * http://blog.csdn.net/male09/article/details/70140558
 * https://www.jianshu.com/p/e04aedd27b33
 * 需要权限:
 * <uses-permission android:name="android.permission.INTERNET"/>
 * <uses-permission android:name="android.permission.CHANGE_WIFI_STATE" />
 * <uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
 * <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>
 * Desc: 网络异常的广播
 * Author: Jooyer
 * Date: 2018-08-03
 * Time: 17:29
 */
class NetWorkReceiver : BroadcastReceiver() {

    /**
     * 获取网络类型
     */
    private var mType = NetworkUtil.getNetworkType(RxRetrofit.instance.mContext)

    /**
     * 观察者容器
     */
    private val mObservers = ArrayList<NetStateChangeObserver>()

    companion object {
        val INSTANCE by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            NetWorkReceiver()
        }
    }

    override fun onReceive(context: Context, intent: Intent) {
        if (Constants.CONNECTIVITY_CHANGE == intent.action
            || Constants.WIFI_STATE_CHANGED == intent.action
            || Constants.STATE_CHANGE == intent.action ){
            val networkType = NetworkUtil.getNetworkType(context)
            notifyObservers(networkType)
        }

    }

    /**
     * 注册网络监听广播
     */
    fun registerReceiver(context: Context) {
        val filter = IntentFilter()
        filter.addAction(Constants.CONNECTIVITY_CHANGE)
        filter.addAction(Constants.WIFI_STATE_CHANGED)
        filter.addAction(Constants.STATE_CHANGE)
        context.registerReceiver(INSTANCE, filter)
    }

    /**
     * 注销网络监听广播
     */
    fun unRegisterReceiver(context: Context) {
        context.unregisterReceiver(INSTANCE)
    }

    /**
     * 添加网络监听观察者
     */
    fun registerObserver(observer: NetStateChangeObserver?) {
        if (observer == null) {
            return
        }
        if (!INSTANCE.mObservers.contains(observer)) {
            INSTANCE.mObservers.add(observer)
        }
    }

    /**
     * 移除网络监听观察者
     */
    fun unRegisterObserver(observer: NetStateChangeObserver?) {
        if (observer == null) {
            return
        }
        if (INSTANCE.mObservers == null) {
            return
        }
        INSTANCE.mObservers.remove(observer)
    }

    /**
     * 通知所有观察者,网络变化
     */
    private fun notifyObservers(networkType: NetworkType) {
        if (mType == networkType) {
            MMKV.defaultMMKV().encode(Constants.KEY_NETWORK_STATE,networkType != NetworkType.NETWORK_NO)
            return
        }
        mType = networkType
        if (networkType == NetworkType.NETWORK_NO) {
            MMKV.defaultMMKV().encode(Constants.KEY_NETWORK_STATE,false)
            for (observer in mObservers) {
                observer.onNetDisconnected()
            }
        } else {
            MMKV.defaultMMKV().encode(Constants.KEY_NETWORK_STATE,true)
            for (observer in mObservers) {
                observer.onNetConnected(networkType)
            }
        }
    }
}
package com.meirenmeitu.ui.network

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import ccom.meirenmeitu.ui.network.OnNetWorkListener
import com.meirenmeitu.library.utils.Constants
import com.tencent.mmkv.MMKV

/** http://blog.csdn.net/male09/article/details/70140558
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
class NetWorkReceiver(private val listener: OnNetWorkListener) : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        val cManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager? ?: return

        // 获取所有网络信息
        val allNetWorks = cManager!!.allNetworks
        if (allNetWorks.isNotEmpty()) {
            outside@ allNetWorks.forEach {
                val info = cManager.getNetworkInfo(it)
                if (null != info && info.isConnected) {
                    MMKV.defaultMMKV().encode(Constants.KEY_NETWORK_STATE, true)
                    listener.onAvailable(info)
                    return@outside
                } else {
                    MMKV.defaultMMKV().encode(Constants.KEY_NETWORK_STATE, false)
                    listener.onLost()
                }
            }
        } else {
            MMKV.defaultMMKV().encode(Constants.KEY_NETWORK_STATE, false)
            listener.onLost()
        }
    }
}
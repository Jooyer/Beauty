package com.meirenmeitu.net.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import com.meirenmeitu.library.utils.Constants
import com.tencent.mmkv.MMKV

/**
 * Desc: 判断 网络和Url
 * Author: Jooyer
 * Date: 2018-07-31
 * Time: 11:07
 */
object CommUtil {

    /**
     * 判断网络是否正常连接
     *
     * @return true --> 正常连接
     */
    fun isNetWorkAvailable(): Boolean {

        return  MMKV.defaultMMKV().decodeBool(Constants.KEY_NETWORK_STATE,true)

//        val manager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
//        val info = manager.activeNetworkInfo
//        if (null != info && info.isConnected) {
//            if (info.state == NetworkInfo.State.CONNECTED) {
//                return true
//            }
//        }
//        return false
    }

    /**
     * 读取 BaseUrl
     */
    fun getBaseUrl(netUrl: String): String {
        var url = netUrl
        var head = ""
        var index = url.indexOf("://")
        if (index != -1) {
            head = url.substring(0, index + 3)
            url = url.substring(index + 3)
        }

        index = url.lastIndexOf("/")
        if (index != -1) {
            url = url.substring(0, index + 1)
        }
        return head + url
    }


}
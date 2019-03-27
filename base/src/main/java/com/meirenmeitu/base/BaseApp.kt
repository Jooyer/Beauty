package com.meirenmeitu.base

import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex
import com.tencent.mmkv.MMKV

/**
 * Desc:
 * Author: Jooyer
 * Date: 2018-07-30
 * Time: 18:27
 */
open class BaseApp : Application(){

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
        MMKV.initialize(this)
    }


}
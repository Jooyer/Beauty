package com.meirenmeitu.beauty

import com.meirenmeitu.base.BaseApp
import com.meirenmeitu.library.utils.Constants
import com.meirenmeitu.net.retrofit.RxRetrofit


class App : BaseApp() {

    override fun onCreate() {
        super.onCreate()
        RxRetrofit.instance.init(this, "Molue",
            Constants.BASE_URL, true)
    }

}
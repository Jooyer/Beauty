package com.meirenmeitu.beauty.view

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.view.View
import androidx.core.content.ContextCompat
import com.baidu.mobads.AdSettings
import com.baidu.mobads.InterstitialAd
import com.baidu.mobads.SplashAd
import com.baidu.mobads.SplashAdListener
import com.meirenmeitu.beauty.R
import com.meirenmeitu.beauty.presenter.ADPresenter
import com.meirenmeitu.library.utils.FileUtil
import com.meirenmeitu.library.utils.MD5Utils
import com.meirenmeitu.net.utils.NetUtil
import com.meirenmeitu.ui.mvp.BaseActivity
import com.yanzhenjie.permission.AndPermission
import kotlinx.android.synthetic.main.activity_ad.*


/** https://blog.csdn.net/qq_26971803/article/details/51284982
 *  https://blog.csdn.net/aichilubiantan/article/details/79998484
 *
 * Desc: 广告页
 * Author: Jooyer
 * Date: 2018-08-19
 * Time: 22:57
 */
class ADActivity : BaseActivity<ADPresenter>() {

    private lateinit var mInterAd: InterstitialAd

    // 静态代码块
//    companion object {
//        init {  //加载so库的时候，需要掐头去尾，去掉lib和.so
//            System.loadLibrary("c++_shared")
//            System.loadLibrary("native-lib")
//        }
//    }

    override fun requestWindowFeature() {
//        println("Signature=============SHA1: ${JniUtils.getSHA1Signature()}")
//        println("Signature=============SHA1: ${getAppSHA1Signature()}")
//        println("Signature=============MD5: ${JniUtils.getMD5Signature()}")
//        println("Signature=============MD5: ${getAppMD5Signature()}")
//        println("Signature=============Crc: ${MD5Utils.getAppCrc(packageCodePath)}")
//
//        if (!TextUtils.equals(JniUtils.getMD5Signature(), getAppMD5Signature())) {
//            finish()
//            System.exit(0)
//        }
    }

    override fun useStartAnim() = false

    override fun createPresenter(): ADPresenter {
        return ADPresenter(this)
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_ad
    }

    override fun setLogic() {
        AndPermission.with(this)
            .runtime()
            .permission(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
            .onGranted {
                //                    showAD()  // TODO 临时注释
                // 将分享的 image 保存到 SD卡
                FileUtil.drawableToFile(
                    ContextCompat.getDrawable(this@ADActivity, R.mipmap.ic_share_logo)!!
                            as BitmapDrawable, FileUtil.SHARE_MOLUE
                )

//                    Looper.myQueue().addIdleHandler {
//                        false // false 只回调一次
//                    }
            }.onDenied {
                //TODO 给一个弹窗,然后用户点击设置去设置界面, 回来检查权限是否给了
                val localIntent = Intent()
                localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                localIntent.action = "android.settings.APPLICATION_DETAILS_SETTINGS"
                localIntent.data = Uri.fromParts("package", packageName, null)
                startActivity(localIntent)
            }.start()


//        mInterAd = InterstitialAd(this@ADActivity, adPlaceId)
//        mInterAd.setListener(object : InterstitialAdListener {
//            override fun onAdFailed(p0: String) {
//                println("onAdFailed=============$p0")
//            }
//
//            override fun onAdDismissed() {
//                println("onAdDismissed=============")
//            }
//
//            override fun onAdPresent() {
//                println("onAdPresent=============")
//            }
//
//            override fun onAdClick(p0: InterstitialAd) {
//                println("onAdClick=============")
//            }
//
//            override fun onAdReady() {
//                println("onAdReady=============")
//                mInterAd.showAd(this@ADActivity)
//            }
//
//        })
//
//        mInterAd.loadAd()

    }

    override fun bindEvent() {
        cdv_count_down_ad.setOnClickListener {
            //            mPresenter.logins("18328369558","1234")
//            val helpInfo = HelpInfo()
//            helpInfo.cnName = "哈哈"
//            helpInfo.engName = "cl"
//            helpInfo.casCode = "123"
//            helpInfo.weight = 123
//            helpInfo.address = "武汉"
//            helpInfo.organization = "武汉大学"
//            helpInfo.gotFunc = 1
//            helpInfo.coinCount = 100
//            helpInfo.remark = "备注.."
//
//            helpInfo.userId = "eb7b58d2ebe240a78e7bd51e3cc58bcb"
//            helpInfo.userName = "张三"
//            helpInfo.userAvatar = 1
//
//            mPresenter.publishHelp(helpInfo)
        }
    }


    private fun showAD() {
        println("showAD=============" + NetUtil.isNetWorkAvailable())
        if (NetUtil.isNetWorkAvailable()) { // 网络正常
            println("showAD=============2")
            AdSettings.setSupportHttps(true)
            val adPlaceId = "2058622"
            SplashAd(this@ADActivity, ll_ad_container_ad, AdListener(), adPlaceId, true)
            // TODO 定位功能暂时不用
//        LocationUtil.locate(this)
        } else {
            println("showAD=============3")
            gotoNexts()
        }
    }


    inner class AdListener : SplashAdListener {
        override fun onAdFailed(p0: String) {
            println("onAdFailed=============$p0")
            window.decorView.postDelayed({
                gotoNext()
            }, 1000)
        }

        override fun onAdDismissed() {
            println("onAdDismissed=============")
            gotoNext()
        }

        override fun onAdPresent() {
            println("onAdPresent=============")
            cdv_count_down_ad.visibility = View.VISIBLE
            cdv_count_down_ad.startCountDown(5)
        }

        override fun onAdClick() {
            println("onAdClick=============")
        }


        private fun gotoNext() {
            startActivity(Intent(this@ADActivity, MainActivity::class.java))
            overridePendingTransition(R.anim.act_center_in, R.anim.act_center_out)
            delayFinish(650)
        }
    }

    /**
     * 获取app签名md5值
     */
    @SuppressLint("PackageManagerGetSignatures")
    private fun getAppMD5Signature(): String {
        try {
            val info = packageManager.getPackageInfo(
                packageName,
                PackageManager.GET_SIGNATURES
            )
            val signs = info.signatures
            return MD5Utils.encryptionMD5(signs[0].toByteArray())

        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ""
    }

    /**
     * 获取app签名md5值
     */
    @SuppressLint("PackageManagerGetSignatures")
    private fun getAppSHA1Signature(): String {
        try {
            val info = packageManager.getPackageInfo(
                packageName,
                PackageManager.GET_SIGNATURES
            )
            val signs = info.signatures
            return MD5Utils.encryptionSHA1(signs[0].toByteArray())

        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return ""
    }

    private fun gotoNexts() {
        window.decorView.postDelayed({
            startActivity(Intent(this@ADActivity, MainActivity::class.java))
            overridePendingTransition(R.anim.act_center_in, R.anim.act_center_out)
            delayFinish(650)
        }, 2000)
    }

}
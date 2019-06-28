package com.meirenmeitu.beauty.view

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.app.Activity
import android.text.Editable
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.view.animation.LinearInterpolator
import android.widget.Button
import androidx.appcompat.widget.AppCompatRadioButton
import androidx.core.content.ContextCompat
import com.meirenmeitu.base.define.LimitEditText
import com.meirenmeitu.base.dialog.JAlertDialog
import com.meirenmeitu.base.dialog.OnJAlertDialogCLickListener
import com.meirenmeitu.base.listener.EditTextWatcher
import com.meirenmeitu.base.utils.SelectorFactory
import com.meirenmeitu.beauty.R
import com.meirenmeitu.beauty.presenter.LoginPresenter
import com.meirenmeitu.common.bean.UserInfoBean
import com.meirenmeitu.common.db.AppDatabaseHelper
import com.meirenmeitu.library.rxbind.RxView
import com.meirenmeitu.library.utils.*
import com.meirenmeitu.net.rxbus.RxBus
import com.meirenmeitu.net.rxbus.RxCodeManager
import com.meirenmeitu.net.rxbus.RxMessage
import com.meirenmeitu.ui.mvp.BaseActivity
import com.tencent.mmkv.MMKV
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_login.*
import java.util.concurrent.TimeUnit

/**
 * Desc: 登录
 * Author: Jooyer
 * Date: 2018-09-26
 * Time: 16:43
 */
class LoginActivity : BaseActivity<LoginPresenter>() {

    private var screenHeight = 0//屏幕高度
    private var keyHeight = 0 //软件盘弹起后所占高度
    private val scale = 0.6f //logo缩放比例
    private val timeDown = 60L
    private var disposable: Disposable? = null
    private lateinit var mCompleteInfoDialog: JAlertDialog
    private lateinit var et_nickname_input_login: LimitEditText

    override fun needUseImmersive() = false
    override fun createPresenter(): LoginPresenter {
        StatusBarUtil.transparentStatusBar(this, false)
        return LoginPresenter(this)
    }

    override fun getLayoutId() = R.layout.activity_login

    override fun setLogic() {
        if (isFullScreen(this)) {
            AndroidBug5497Workaround.assistActivity(this)
        }
        screenHeight = this.resources.displayMetrics.heightPixels //获取屏幕高度
        keyHeight = screenHeight / 3//弹起高度为屏幕高度的1/3

        btn_submit_login.background = SelectorFactory.newShapeSelector()
                .setDefaultBgColor(ContextCompat.getColor(this, R.color.color_666666))
                .setPressedBgColor(ContextCompat.getColor(this, R.color.main_theme_color))
                .setCornerRadius(DensityUtils.dpToPx(5))
                .create()

        initDialog()
    }


    @SuppressLint("ClickableViewAccessibility")
    override fun bindEvent() {
        RxView.setOnClickListeners(this, toolbar.iv_left_icon_menu, iv_clean_phone_login,
                iv_clean_code_login, tv_get_code_login, tv_about_molue, btn_submit_login)

        addChangeListener()

        et_mobile_input_login.addTextChangedListener(object : EditTextWatcher() {
            override fun afterTextChanged(s: Editable) {
                if (!TextUtils.isEmpty(s) && iv_clean_phone_login.visibility == View.GONE) {
                    iv_clean_phone_login.visibility = View.VISIBLE
                } else if (TextUtils.isEmpty(s)) {
                    iv_clean_phone_login.visibility = View.GONE
                }
            }
        })

        et_password_input_login.addTextChangedListener(object : EditTextWatcher() {
            override fun afterTextChanged(s: Editable) {
                if (!TextUtils.isEmpty(s) && iv_clean_code_login.visibility == View.GONE) {
                    iv_clean_code_login.visibility = View.VISIBLE
                } else if (TextUtils.isEmpty(s)) {
                    iv_clean_code_login.visibility = View.GONE
                }
                // TODO
            }
        })


        /**
         * 禁止键盘弹起的时候可以滚动
         */
        scrollView.setOnTouchListener { v, event -> true }

    }

    override fun onClick(view: View) {
        when (view) {

            toolbar.iv_left_icon_menu -> {
                finish()
            }
            iv_clean_phone_login -> {

            }
            iv_clean_code_login -> {

            }
            btn_submit_login -> {
                when {
                    et_mobile_input_login.text.isEmpty() -> JSnackBar.Builder().attachView(cl_root_login)
                            .message("请输入正确的手机号码!")
                            .default()
                            .build()
                            .default()
                            .show()
                    et_password_input_login.text.isEmpty() -> JSnackBar.Builder().attachView(cl_root_login)
                            .message("请输入正确的验证码!")
                            .default()
                            .build()
                            .default()
                            .show()
                    else -> mPresenter.login(et_mobile_input_login.text.toString(), et_password_input_login.text.toString())
                }
            }
            tv_get_code_login -> {
                if (MobileUtils.isMobileNumber(et_mobile_input_login.text.toString())) {
                    tv_get_code_login.isEnabled = false
                    // TODO 请求验证码
                } else {
                    JSnackBar.Builder().attachView(cl_root_login)
                            .message("请输入正确的手机号码!")
                            .default()
                            .build()
                            .default()
                            .show()
                }


            }
            tv_about_molue -> { // 关于我们
                // TODO
            }
        }
    }

    fun startCutDown() {
        Observable.interval(0, 1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .take(timeDown + 1)
                .map {
                    timeDown - it
                }.doOnSubscribe {
                    tv_get_code_login.isEnabled = false
                    tv_get_code_login.setTextColor(ContextCompat.getColor(this@LoginActivity, R.color.color_999999))
                }
                .subscribe(object : Observer<Long> {
                    override fun onSubscribe(d: Disposable) {
                        disposable = d
                    }

                    override fun onComplete() {
                        tv_get_code_login.text = getString(R.string.get_code)
                        tv_get_code_login.isEnabled = true
                        tv_get_code_login.setTextColor(ContextCompat.getColor(this@LoginActivity, R.color.main_theme_color))
                    }

                    override fun onNext(t: Long) {
                        tv_get_code_login.text = String.format(getString(R.string.code_replay), t)
                    }

                    override fun onError(e: Throwable) {
                    }

                })
    }

    private fun addChangeListener() {
        scrollView.addOnLayoutChangeListener { v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom ->
            //            println("addChangeListener=================oldBottom: $oldBottom  ---bottom: $bottom ---keyHeight: $keyHeight  ---btn_submit_login.top: ${btn_submit_login.top}")
            /* old是改变前的左上右下坐标点值，没有old的是改变后的左上右下坐标点值
              现在认为只要控件将Activity向上推的高度超过了1/3屏幕高，就认为软键盘弹起*/
            if (oldBottom != 0 && bottom != 0 && oldBottom - bottom > keyHeight) {
                if (btn_submit_login.top - (oldBottom - bottom) > 0) {
                    val dist = (btn_submit_login.top - (oldBottom - bottom)).toFloat()
                    val mAnimatorTranslateY = ObjectAnimator.ofFloat(content,
                            "translationY", 0.0f, -dist)
                    mAnimatorTranslateY.duration = 300
                    mAnimatorTranslateY.interpolator = LinearInterpolator()
                    mAnimatorTranslateY.start()
                    service.visibility = View.INVISIBLE
                }
                zoomIn(logo, (oldBottom - bottom - keyHeight).toFloat())
            } else if (oldBottom != 0 && bottom != 0 && bottom - oldBottom > keyHeight) {
                val mAnimatorTranslateY = ObjectAnimator.ofFloat(content,
                        "translationY", content.translationY, 0F)
                mAnimatorTranslateY.duration = 300
                mAnimatorTranslateY.interpolator = LinearInterpolator()
                mAnimatorTranslateY.start()
                service.visibility = View.VISIBLE
                //键盘收回后，logo恢复原来大小，位置同样回到初始位置
                zoomOut(logo)
            }
        }
    }

    /**
     * 缩小
     * @param view
     */
    private fun zoomIn(view: View, dist: Float) {
        view.pivotY = view.height.toFloat()
        view.pivotX = (view.width / 2).toFloat()
        val mAnimatorSet = AnimatorSet()
        val mAnimatorScaleX = ObjectAnimator.ofFloat(view, "scaleX", 1.0f, scale)
        val mAnimatorScaleY = ObjectAnimator.ofFloat(view, "scaleY", 1.0f, scale)
        val mAnimatorTranslateY = ObjectAnimator.ofFloat(view, "translationY", 0.0f, -dist)

        mAnimatorSet.play(mAnimatorTranslateY).with(mAnimatorScaleX)
        mAnimatorSet.play(mAnimatorScaleX).with(mAnimatorScaleY)
        mAnimatorSet.duration = 300
        mAnimatorSet.start()
    }

    /**
     * 放大
     * @param view
     */
    private fun zoomOut(view: View) {
        view.pivotY = view.height.toFloat()
        view.pivotX = (view.width / 2).toFloat()
        val mAnimatorSet = AnimatorSet()

        val mAnimatorScaleX = ObjectAnimator.ofFloat(view, "scaleX", scale, 1.0f)
        val mAnimatorScaleY = ObjectAnimator.ofFloat(view, "scaleY", scale, 1.0f)
        val mAnimatorTranslateY = ObjectAnimator.ofFloat(view, "translationY", view.translationY, 0F)

        mAnimatorSet.play(mAnimatorTranslateY).with(mAnimatorScaleX)
        mAnimatorSet.play(mAnimatorScaleX).with(mAnimatorScaleY)
        mAnimatorSet.duration = 300
        mAnimatorSet.start()
    }


    private fun isFullScreen(activity: Activity): Boolean {
        return activity.window.attributes.flags and WindowManager.LayoutParams.FLAG_FULLSCREEN == WindowManager.LayoutParams.FLAG_FULLSCREEN
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initDialog() {
        val view = LayoutInflater.from(this).inflate(R.layout.dialog_complete, null)
        et_nickname_input_login = view.findViewById<LimitEditText>(R.id.et_nickname_input_login)
        val rb_female_complete = view.findViewById<AppCompatRadioButton>(R.id.rb_female_complete)
        val rb_male_complete = view.findViewById<AppCompatRadioButton>(R.id.rb_male_complete)
        val btn_submit_complete = view.findViewById<Button>(R.id.btn_submit_complete)

        btn_submit_complete.background = SelectorFactory.newShapeSelector()
                .setDefaultBgColor(ContextCompat.getColor(this, R.color.color_666666))
                .setPressedBgColor(ContextCompat.getColor(this, R.color.main_theme_color))
                .setCornerRadius(DensityUtils.dpToPx(5))
                .create()

        mCompleteInfoDialog = JAlertDialog.Builder(this).setCancelable(false)
                .setAnimation(R.style.SignDialogAnimation)
                .setContentView(view)
                .setHasAnimation(true)
                .setFullWidth()
                .setOnClick(R.id.btn_submit_complete)
                .setOnJAlertDialogCLickListener(object : OnJAlertDialogCLickListener {
                    override fun onClick(view: View, position: Int) {
                        if (et_nickname_input_login.text.toString().isNotEmpty()
                                && (rb_male_complete.isChecked || rb_female_complete.isChecked)) {

                            val userInfo = AppDatabaseHelper.getInstance(this@LoginActivity).database()
                                    .getUserInfoDao().queryUserInfoByUserToken(MMKV.defaultMMKV().decodeString(Constants.APP_REQUEST_TOKEN, ""))

                            if (null != userInfo) {
                                userInfo.userName = et_nickname_input_login.text.toString()
                                userInfo.userGender = if (rb_female_complete.isChecked) 0 else 1
                                MMKV.defaultMMKV().encode(Constants.APP_USER_NAME, userInfo.userName)
                                AppDatabaseHelper.getInstance(this@LoginActivity).database()
                                        .getUserInfoDao().update(userInfo)
//                                println("完成注册 ===============userName: ${userInfo.userName} ->  userGender: ${userInfo.userGender}")
                            }

                            KeyBoardUtil.hideKeyboard(this@LoginActivity, et_nickname_input_login)

                            if (mCompleteInfoDialog.isShowing) {
                                mCompleteInfoDialog.dismiss()
                            }
                            JSnackBar.Builder().attachView(cl_root_login)
                                    .message("登录完成,快去阅读吧!")
                                    .default()
                                    .build()
                                    .default()
                                    .show()
                            // 同步用户信息
                            RxBus.getDefault().send(RxMessage(RxCodeManager.RX_CODE_NEED_SYNC_USER_INFO, 101))
                            delayFinish(2000)
                        }
                    }

                }).create()
    }

     fun showData(data: Any?) {
//        val userInfo = UserInfoBean()
        val info = data as UserInfoBean
//        userInfo.userId = info.userId
//        userInfo.userMobile = info.userMobile
//        userInfo.userToken = info.userToken
        info.needUpdate = 1
        info.nextTime = TimeUtil.getNextDayUnicodeTime()
        MMKV.defaultMMKV().encode(Constants.APP_REQUEST_TOKEN, info.userToken)
        MMKV.defaultMMKV().encode(Constants.APP_USER_ID, info.userId)
        AppDatabaseHelper.getInstance(this).database().getUserInfoDao().insert(info)
        KeyBoardUtil.openKeyboard(this@LoginActivity, et_nickname_input_login)
        if (info.userName.isEmpty() || -1 == info.userGender) {
            mCompleteInfoDialog.show()
            et_nickname_input_login.requestFocus()
            et_nickname_input_login.isFocusable = true
            et_nickname_input_login.isFocusableInTouchMode = true
        } else {
            MMKV.defaultMMKV().encode(Constants.APP_USER_NAME, info.userName)
            KeyBoardUtil.hideKeyboard(this@LoginActivity, et_nickname_input_login)
            JSnackBar.Builder().attachView(cl_root_login)
                    .message("登录完成,快去阅读吧!")
                    .default()
                    .build()
                    .default()
                    .show()
            // 同步用户信息
            RxBus.getDefault().send(RxMessage(RxCodeManager.RX_CODE_NEED_SYNC_USER_INFO, 101))
            delayFinish(2000)
        }

    }

}

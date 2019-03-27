package com.meirenmeitu.library.utils

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText


/** imm.toggleSoftInputFromWindow(0, InputMethodManager.HIDE_NOT_ALWAYS); ---> 如果输入法在窗口上已经显示，则隐藏，反之则显示
 * Desc:
 * Author: Jooyer
 * Date: 2018-09-30
 * Time: 16:21
 */
object KeyBoardUtil {

    /**
     * 显示键盘
     * @param view --> 接受软键盘输入的视图
     */
    fun openKeyboard(activity: Activity, view: EditText) {
        view.postDelayed({
            val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS)
//                imm.showSoftInput(view, 0)
        }, 450)

    }

    /**
     * 隐藏键盘
     * @param view --> 接受软键盘输入的视图
     */
    fun hideKeyboard(activity: Activity, view: View) {
        val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    /**
     *  输入法打开状态
     *  true  --> 打开着
     */
    fun isOpened(activity: Activity): Boolean {
        val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        return imm.isActive
    }





}
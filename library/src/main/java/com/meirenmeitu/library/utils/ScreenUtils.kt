package com.meirenmeitu.library.utils

import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.graphics.Point
import android.graphics.Rect
import android.view.*
import androidx.annotation.NonNull


/**
 * Desc: https://www.cnblogs.com/ldq2016/p/6905429.html
 * Author: Jooyer
 * Date: 2018-09-27
 * Time: 22:02
 */
object ScreenUtils {

    /**
     * 判断虚拟按键栏是否重写
     *
     * @return
     */
    /**
     * 获取是否存在NavigationBar
     * @param context
     * @return
     */
    @Deprecated("由于有反射,故放弃...")
    private fun checkDeviceHasNavigationBar(context: Context): Boolean {
        var hasNavigationBar = false
        val rs = context.resources
        val id = rs.getIdentifier("config_showNavigationBar", "bool", "android")
        if (id > 0) {
            hasNavigationBar = rs.getBoolean(id)
        }
        try {
            val systemPropertiesClass = Class.forName("android.os.SystemProperties")
            val m = systemPropertiesClass.getMethod("get", String::class.java)
            val navBarOverride = m.invoke(systemPropertiesClass, "qemu.hw.mainkeys") as String
            if ("1" == navBarOverride) {
                hasNavigationBar = false
            } else if ("0" == navBarOverride) {
                hasNavigationBar = true
            }
        } catch (e: Exception) {
        }

        return hasNavigationBar
    }


    /**
     *
     * @return  true --> 存在 NavigationBar
     */
    private fun checkDeviceHasNavigationBar(@NonNull context: Activity): Boolean {
        val window = context.window
        val display = window.windowManager.defaultDisplay
        val point = Point()
        display.getRealSize(point)

        val decorView = window.decorView
        val conf = context.resources.configuration
        return if (Configuration.ORIENTATION_LANDSCAPE == conf.orientation) { // 横屏
            val contentView = decorView.findViewById<View>(android.R.id.content)
            point.x != contentView.width
        } else {
            val rect = Rect()
            decorView.getWindowVisibleDisplayFrame(rect)
            rect.bottom != point.y
        }
    }


    /**
     * 获取虚拟功能键高度
     * @param context
     * @return
     */
    //获取虚拟按键的高度
     fun getNavigationBarHeight(context: Activity): Int {
        var result = 0
        if (checkDeviceHasNavigationBar(context)) {
            val res = context.resources
            val resourceId = res.getIdentifier("navigation_bar_height", "dimen", "android")
            if (resourceId > 0) {
                result = res.getDimensionPixelSize(resourceId)
            }
        }
        return result
    }

    fun getRealHeight(context: Activity): Int {
        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = windowManager.defaultDisplay
        val outPoint = Point()
        display.getRealSize(outPoint)
        val navigationBarHeight = getNavigationBarHeight(context)
//        println("getRealHeight=========${isNavigationBarAvailable(context)}  --> navigationBarHeight: $navigationBarHeight")
        return outPoint.y - navigationBarHeight
    }

    fun getRealWidth(context: Context): Int {
        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = windowManager.defaultDisplay
        val outPoint = Point()
        display.getRealSize(outPoint)
        return outPoint.x
    }

    /**
     * 是否有下方虚拟栏
     * @return true --> 有虚拟按键
     */
    private fun isNavigationBarAvailable(context: Activity): Boolean {
        // 菜单键(不是虚拟键,是手机屏幕外的按键)
        val hasMenuKey = ViewConfiguration.get(context).hasPermanentMenuKey()
        // 返回键(不是虚拟键,是手机屏幕外的按键)
        val hasBackKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK)
        // Home键(不是虚拟键,是手机屏幕外的按键)
        val hasHomeKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_HOME)

//        println("isNavigationBarAvailable=========hasMenuKey: $hasMenuKey --> hasBackKey: $hasBackKey --> hasHomeKey: $hasHomeKey")

        return !(hasBackKey && hasHomeKey) || !(hasMenuKey && hasBackKey)
    }

}
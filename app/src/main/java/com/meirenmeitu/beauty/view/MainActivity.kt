package com.meirenmeitu.beauty.view

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.graphics.Point
import android.net.Uri
import android.view.View
import android.widget.ImageView
import androidx.core.view.GravityCompat
import androidx.customview.widget.ViewDragHelper
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.RecyclerView
import com.meirenmeitu.base.adapter.CommonAdapter
import com.meirenmeitu.base.adapter.MultiItemTypeAdapter
import com.meirenmeitu.base.adapter.ViewHolder
import com.meirenmeitu.base.dialog.JAlertDialog
import com.meirenmeitu.beauty.R
import com.meirenmeitu.beauty.bean.LeftDrawerMenu
import com.meirenmeitu.beauty.presenter.MainPresenter
import com.meirenmeitu.library.utils.*
import com.meirenmeitu.net.rxbus.RxBus
import com.meirenmeitu.net.rxbus.RxCodeManager
import com.meirenmeitu.net.rxbus.RxMessage
import com.meirenmeitu.ui.mvp.BaseActivity
import com.tencent.mmkv.MMKV
import com.yanzhenjie.permission.AndPermission
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.left_menu_home.*
import java.io.File
import java.util.*


/**
 * @Project meirenmeitu
 * @Author 刘一召
 * @Date  2019/2/11
 * @Time 17:47
 * @Desc https://www.jianshu.com/p/e87c0086a3ae (Fragment 之间共享)
 *     https://blog.csdn.net/qq402164452/article/details/73611233
 *   https://www.jianshu.com/p/af0905603be7
 *   https://blog.csdn.net/cyuyanshujujiegou/article/details/76514203?utm_source=blogxgwz2 (Fragment -> Activity)
 *
 * 频道管理:
 *  https://www.jianshu.com/p/57324eb516df (腾讯)
 *  https://blog.csdn.net/xiaozhang0414/article/details/80190745 (头条)
 *
 *  https://blog.csdn.net/u012216274/article/details/70237734 (动画实现状态栏的显示和隐藏)
 *
 * 	androidx (https://developer.android.google.cn/jetpack/androidx/migrate)
 * @Copyright 2018 www.scshuimukeji.cn Inc. All rights reserved.
 * http://www.ui.cn/detail/415814.html --> Logo
 * 全面屏适配
 * https://juejin.im/post/5b1930835188257d7541ba33
 * https://blog.csdn.net/xiangzhihong8/article/details/80317682
 *
 * https://www.jianshu.com/p/1e27efb1dcac --> APP 设置
 *
 * https://github.com/luckybilly/PreLoader/blob/master/README-zh-CN.md --> 预加载
 *
 * https://github.com/PrettyUp/SecTest --> SO 加固
 *
 * https://github.com/Tencent/MMKV/wiki/android_tutorial_cn
 *
 * https://juejin.im/post/5bbdca89e51d450e92526a3b --> 统计SDK
 *
 * https://juejin.im/post/5bce688e6fb9a05cf715d1c2 --> 屏幕适配
 *
 * https://github.com/mcxiaoke/packer-ng-plugin --> 打包
 *
 * https://www.diycode.cc/topics/1217 --> 软件著作权
 *
 * https://blog.csdn.net/guiying712/article/details/78474177 --> ViewModel / LiveData / LifeCycle ...
 *
 * https://blog.csdn.net/qq_31370269/article/details/50752211  --> xml 中使用 bitmap
 *
 *  //        val contentResolver = contentResolver
// 注册什么样的 Uri, 在通知时使用相同的, 如 contentResolver.notifyChange(Uri.parse("content://cn.mulue.reader"),null)
//        contentResolver.registerContentObserver(Uri.parse("content://cn.mulue.reader"), false, object : ContentObserver(null) {
//            override fun onChange(selfChange: Boolean) {
//
//            }
//        })
 *
 * 路由
 * https://github.com/ssseasonnn/RxRouter/blob/master/README.ch.md
 * Room
 * https://www.jianshu.com/p/463288377abe
 */
class MainActivity : BaseActivity<MainPresenter>() {
    private var mUpdateDialog: JAlertDialog? = null
    override fun useStartAnim() = false

    override fun createPresenter(): MainPresenter {
        hideStatusBarAndNavigationBar()
        return MainPresenter(this)
    }

    override fun getLayoutId() = R.layout.activity_main

    override fun setLogic() {

        AndPermission.with(this)
            .runtime()
            .permission(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
            .onGranted {
                // TODO 权限请求通过
                showHomeFragment()
                setDrawer()
            }.onDenied {
                //TODO 给一个弹窗,然后用户点击设置去设置界面, 回来检查权限是否给了
                val localIntent = Intent()
                localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                localIntent.action = "android.settings.APPLICATION_DETAILS_SETTINGS"
                localIntent.data = Uri.fromParts("package", packageName, null)
                startActivity(localIntent)
            }.start()
//        setDrawerLeftEdgeSize(this,dl_contain_main,0.1F)
    }

    override fun bindEvent() {

        // 打开抽屉
        mCompositeDisposable.add(RxBus.getDefault().register(
            RxMessage::class.java,
            RxCodeManager.RX_CODE_OPEN_DRAWER_LAYOUT
        ) { dl_contain_main.openDrawer(GravityCompat.START) })
    }

    /**
     *  隐藏状态栏和底部导航栏
     */
    private fun hideStatusBarAndNavigationBar() {
        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LOW_PROFILE or
                    View.SYSTEM_UI_FLAG_FULLSCREEN or
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                    View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
                    View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
    }

    /**
     *  显示状态栏和底部导航栏
     *  View.SYSTEM_UI_FLAG_VISIBLE --> 只使用这个,将显示状态栏和导航栏
     *  View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION --> 一起使用虽然状态栏和导航栏都显示,但是导航栏不占据位置,感觉是浮在表面
     *  View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN --> 这个则正常显示导航栏,状态栏需要透明时很好用(此时状态栏也显示了的)
     */
    private fun showStatusBarAndNavigationBar() {
        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
    }

    /**
     * 设置抽屉的内容
     */
    private fun setDrawer() {
        val param = cl_left_menu_contain.layoutParams as DrawerLayout.LayoutParams
        param.width = DensityUtils.getWindowSize(this).widthPixels * 3 / 4
        cl_left_menu_contain.layoutParams = param

        val arr = Arrays.asList(*resources.getStringArray(R.array.left_menu))
        val icons = Arrays.asList(*resources.getStringArray(R.array.left_menu_icon))
        val menus = ArrayList<LeftDrawerMenu>()
        for ((index, name) in arr.withIndex()) {
            val bean = LeftDrawerMenu(name, icons[index], index)
            menus.add(bean)
        }

        val adapter = object : CommonAdapter<LeftDrawerMenu>(this, R.layout.item_left_menu, menus) {
            override fun convert(holder: ViewHolder, bean: LeftDrawerMenu, position: Int) {
                holder.setText(R.id.tv_name_item_left_menu, bean.name)

//                ImageLoader.loadImgWithFitCenter(holder.getView(R.id.iv_icon_item_left_menu),
//                        resources.getIdentifier(bean.icon, "mipmap", packageName))

                holder.getView<ImageView>(R.id.iv_icon_item_left_menu)
                    .setBackgroundResource(resources.getIdentifier(bean.icon, "drawable", packageName))

                if (1 == bean.position || 4 == bean.position) {
                    holder.getView<View>(R.id.view_divider).visibility = View.VISIBLE
                }

            }
        }

        adapter.setOnItemClickListener(object : MultiItemTypeAdapter.OnItemClickListener {
            override fun onItemClick(view: View, holder: RecyclerView.ViewHolder, position: Int) {
                dl_contain_main.closeDrawer(GravityCompat.START)
                window.decorView.postDelayed({
                    println("onItemClick=========== $position")
                    when (position) {
                        0 -> { // 我的收藏
                            val intent = Intent(this@MainActivity, CollectActivity::class.java)
                            intent.putExtra(Constants.KEY_DRAWER_TYPE,0)
                            startActivity(intent)
                        }
                        1 -> { // 我的下载
                            val intent = Intent(this@MainActivity, CollectActivity::class.java)
                            intent.putExtra(Constants.KEY_DRAWER_TYPE,1)
                            startActivity(intent)
                        }
                        2 -> { // 最新欣赏
                            val intent = Intent(this@MainActivity, CollectActivity::class.java)
                            intent.putExtra(Constants.KEY_DRAWER_TYPE,2)
                            startActivity(intent)
                        }
                        3 -> { // 分类管理
                            startActivity(Intent(this@MainActivity, ColumnActivity::class.java))
                        }
                        4 -> { // 意见反馈
                            startActivity(Intent(this@MainActivity, FeedbackActivity::class.java))
                        }
                        5 -> { // 支持一下
                            val installs = AppraiseUtils.getInstallAppMarkets(this@MainActivity)
                            val markets = AppraiseUtils.SelectedInstalledAPPs(this@MainActivity, installs)
                            if (!markets.isEmpty()) {
                                AppraiseUtils.launchAppDetail(this@MainActivity.applicationContext,
                                    packageName, markets[0])
                            } else { // 应用商店网页版跳转
                                val uri = Uri.parse("")
                                val intent = Intent(Intent.ACTION_VIEW, uri)
//                                mActivity.startActivity(intent)
                            }
                        }
                        6 -> { // 版本更新
                            AndPermission.with(this@MainActivity)
                                .runtime()
                                .permission(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                    Manifest.permission.READ_EXTERNAL_STORAGE)
                                .onGranted {
                                    if (null == mUpdateDialog) { // 不需要更新
                                        JSnackBar.Builder().attachView(dl_contain_main)
                                            .message("嗨!暂时没有新版本呢!")
                                            .default()
                                            .build()
                                            .default()
                                            .show()
                                    } else {
                                        mUpdateDialog?.show()
                                    }

                                }.onDenied {
                                    // TODO 跳转到设置界面
                                }.start()
                        }
                        7 -> { // 分享APP
                            ShareUtil.originalShareImage(this@MainActivity, File(FileUtil.FILE_DIR, FileUtil.SHARE_MOLUE))
                        }
                    }
                }, 250)
            }
        })

        rv_list_left_menu.adapter = adapter
    }


    /**
     * 显示广告
     */
    private fun showADFragment() {
//        supportFragmentManager.beginTransaction()
//            .add(R.id.ll_root_main, ADFragment.newInstance(), ADFragment.TAG)
//            .commit()
    }

    /**
     * 显示主页
     */
    private fun showHomeFragment() {
        MMKV.defaultMMKV().encode(Constants.SCREEN_REAL_HEIGHT, ScreenUtils.getRealHeight(this))
//        window.decorView.setBackgroundColor(Color.TRANSPARENT)
        showStatusBarAndNavigationBar()
        supportFragmentManager.beginTransaction()
//            .setCustomAnimations(
//                R.anim.act_center_in,
//                R.anim.act_center_out,
//                R.anim.act_center_in,
//                R.anim.act_center_out
////                R.anim.slide_right_in,
////                R.anim.slide_left_out,
////                R.anim.slide_left_in,
////                R.anim.slide_right_out
//            )
            .replace(R.id.ll_root_main, HomeFragment.newInstance(), HomeFragment.TAG)
            .commit()
    }

    // https://blog.csdn.net/qq_39249422/article/details/80410332
    /**
     * @param displayWidthPercentage --> 0-1.0, 设置的 DrawerLayout 触发范围
     */
    private fun setDrawerLeftEdgeSize(activity: Activity, drawerLayout: DrawerLayout, displayWidthPercentage: Float) {
        try {
            // 找到 ViewDragHelper 并设置 Accessible 为true
            val leftDraggerField = drawerLayout.javaClass.getDeclaredField("mLeftDragger")//Right
            leftDraggerField.isAccessible = true
            val leftDragger = leftDraggerField.get(drawerLayout) as ViewDragHelper

            // 找到 edgeSizeField 并设置 Accessible 为true
            val edgeSizeField = leftDragger.javaClass.getDeclaredField("mEdgeSize")
            edgeSizeField.isAccessible = true
            val edgeSize = edgeSizeField.getInt(leftDragger)

            // 设置新的边缘大小
            val displaySize = Point()
            activity.windowManager.defaultDisplay.getSize(displaySize)
            edgeSizeField.setInt(leftDragger, Math.max(edgeSize, (displaySize.x * displayWidthPercentage).toInt()))
        } catch (e: NoSuchFieldException) {
        } catch (e: IllegalArgumentException) {
        } catch (e: IllegalAccessException) {
        }

    }

    override fun onBackPressed() {
        if (dl_contain_main.isDrawerOpen(GravityCompat.START)) {
            dl_contain_main.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

}
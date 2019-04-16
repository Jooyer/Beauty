package com.meirenmeitu.beauty.view


import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.meirenmeitu.beauty.R
import com.meirenmeitu.beauty.presenter.HomePresenter
import com.meirenmeitu.common.adapter.TitleFragmentStateAdapter
import com.meirenmeitu.library.rxbind.RxView
import com.meirenmeitu.library.tablayout.TabLayout
import com.meirenmeitu.library.utils.DensityUtils
import com.meirenmeitu.library.utils.StatusBarUtil
import com.meirenmeitu.net.network.NetworkType
import com.meirenmeitu.net.rxbus.RxBus
import com.meirenmeitu.net.rxbus.RxCodeManager
import com.meirenmeitu.net.rxbus.RxMessage
import com.meirenmeitu.ui.mvp.BaseFragment
import kotlinx.android.synthetic.main.fragment_home.*
import java.util.*


/**
 * 主页界面
 */
class HomeFragment : BaseFragment<HomePresenter>() {

    companion object {
        val TAG = HomeFragment::class.java.simpleName
        fun newInstance() = HomeFragment()
    }

    override fun createPresenter() = HomePresenter(this)

    override fun getLayoutId() = R.layout.fragment_home
    override fun setLogic() {
        resetTabLayout()
        setVpAdapter()
    }

    override fun bindEvent() {
        RxView.setOnClickListeners(this, iv_drawer_menu)
    }

    override fun onClick(view: View) {
        when (view) {
            iv_drawer_menu -> {
                RxBus.getDefault().send(RxMessage(RxCodeManager.RX_CODE_OPEN_DRAWER_LAYOUT))
            }
        }
    }

    /**
     *  重置 Toolbar 高度
     */
    private fun resetTabLayout() {
        val param = cl_top_menu.layoutParams as ConstraintLayout.LayoutParams
        val statusBarHeight = StatusBarUtil.getStatusBarHeight(mActivity)
        cl_top_menu.setPadding(0, statusBarHeight, 0, 0)
        param.height = DensityUtils.dpToPx(45) + statusBarHeight
        cl_top_menu.layoutParams = param
    }

    /**
     * 设置适配器
     */
    private fun setVpAdapter() {
        navp_container_home.setScrollAble(true)
        navp_container_home.setSmoothScroll(false)
        val titles = Arrays.asList(*resources.getStringArray(R.array.home_category))
        val fragments = ArrayList<BaseFragment<*>>()
        repeat(titles.size) {
            fragments.add(CategoryFragment.newInstance(it))
        }

        navp_container_home.adapter = TitleFragmentStateAdapter(childFragmentManager, fragments, titles)
        tl_container_home.setupWithViewPager(navp_container_home)

        tl_container_home.isNeedSwitchAnimation = true
        //设置 TabLayout 固定线宽 (设置的线宽大于最小tab宽度时 设置线宽失效 用默认 TabLayout 线宽显示逻辑)
        tl_container_home.selectedTabIndicatorWidth = DensityUtils.dpToPx(20)
        // TabLayout 高度
        tl_container_home.setSelectedTabIndicatorHeight(DensityUtils.dpToPx(3))

        for (index in titles.indices) {
            val tab = tl_container_home.getTabAt(index)
            if (null != tab) {
                val tv = TextView(mActivity)
                tv.gravity = Gravity.CENTER
                tv.id = R.id.book_city_tab
                if (0 == index) {
                    tv.setTextColor(ContextCompat.getColor(mActivity, R.color.color_write))
                    tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
                    tv.setShadowLayer(0.15F, 0.2F, 0.2F, ContextCompat.getColor(mActivity, R.color.color_write))
                } else {
                    tv.setTextColor(ContextCompat.getColor(mActivity, R.color.color_EEEEEE))
                    tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15f)
                    tv.setShadowLayer(0F, 0F, 0F, ContextCompat.getColor(mActivity, R.color.color_EEEEEE))
                }

                tv.text = titles[index]
                tab.customView = tv
            }
        }


        tl_container_home.addOnTabSelectedListener(object :
            TabLayout.ViewPagerOnTabSelectedListener(navp_container_home) {
            override fun onTabSelected(tab: TabLayout.Tab) {
                val tv: TextView = tab.customView as TextView
                tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
                tv.setTextColor(ContextCompat.getColor(mActivity, R.color.color_write))
                tv.setShadowLayer(0.15F, 0.2F, 0.2F, ContextCompat.getColor(mActivity, R.color.color_write))

            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
                val tv: TextView = tab.customView as TextView
                tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15f)
                tv.setTextColor(ContextCompat.getColor(mActivity, R.color.color_EEEEEE))
                tv.setShadowLayer(0F, 0F, 0F, ContextCompat.getColor(mActivity, R.color.color_EEEEEE))
            }
        })

    }


}

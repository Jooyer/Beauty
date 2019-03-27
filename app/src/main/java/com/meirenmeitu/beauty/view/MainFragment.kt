package com.meirenmeitu.beauty.view


import com.meirenmeitu.beauty.R
import com.meirenmeitu.beauty.presenter.MainFPresenter
import com.meirenmeitu.common.adapter.FragmentAdapter
import com.meirenmeitu.library.utils.StatusBarUtil
import com.meirenmeitu.ui.mvp.BaseFragment
import kotlinx.android.synthetic.main.fragment_main.*
import java.util.*


/** BottomNavigationBar ( https://blog.csdn.net/weihua_li/article/details/78561323 )
 * 主界面
 */
@Deprecated("暂时废弃")
class MainFragment : BaseFragment<MainFPresenter>() {

    companion object {
        val TAG = MainFragment::class.java.simpleName
        fun newInstance() = MainFragment()
    }

    override fun createPresenter() = MainFPresenter(this)

    override fun getLayoutId() = R.layout.fragment_main
    override fun setLogic() {
//        resetVp()
        vp_container.setScrollAble(true)
        vp_container.setSmoothScroll(false)
        val fragments = ArrayList<BaseFragment<*>>()
//        fragments.add(HomeFragment.newInstance())
//        fragments.add(CollectFragment.newInstance())
//        fragments.add(HomeFragment.newInstance())
//        fragments.add(CollectFragment.newInstance())

        val adapter = FragmentAdapter(childFragmentManager, fragments)
        vp_container.adapter = adapter

    }

    override fun bindEvent() {

    }

    /**
     *  重置 Toolbar 高度
     */
    private fun resetVp() {
//        val param = ll_container_main.layoutParams as LinearLayout.LayoutParams
//        param.setMargins(0,StatusBarUtil.getStatusBarHeight(mActivity),0,0)
//        ll_container_main.layoutParams = param
        ll_container_main.setPadding(0, StatusBarUtil.getStatusBarHeight(mActivity),0,0)
    }

}

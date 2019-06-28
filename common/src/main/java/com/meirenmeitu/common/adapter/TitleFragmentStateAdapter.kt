package com.meirenmeitu.common.adapter

import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.meirenmeitu.ui.mvp.BaseFragment

/**
 * Desc: https://www.jianshu.com/p/378bfff66053?utm_campaign=maleskine&utm_content=note&utm_medium=seo_notes&utm_source=recommendation
 * https://www.jianshu.com/p/25a02f5a15b3
 * Author: Jooyer
 * Date: 2019-02-13
 * Time: 22:47
 */
class TitleFragmentStateAdapter(
    private val fm: FragmentManager,
    private val fms: List<BaseFragment<*>>,
    private val titles: List<String>
) : FragmentStatePagerAdapter(fm) {
    override fun getItem(position: Int): Fragment {
        return fms[position]
    }

    override fun getCount() = fms.size

    override fun getPageTitle(position: Int): CharSequence? {
        return titles[position]
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
//        super.destroyItem(container, position, `object`)
    }
}
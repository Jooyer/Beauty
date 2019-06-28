package com.meirenmeitu.common.adapter

import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.meirenmeitu.ui.mvp.BaseFragment

/**
 * Desc: FragmentPagerAdapter --> 适用于 Fragment 不多的情况
 * Author: Jooyer
 * Date: 2018-07-30
 * Time: 21:13
 */
class TitleFragmentAdapter(
    private val fm: FragmentManager,
    private val fms: List<BaseFragment<*>>,
    private val titles: List<String>
) : FragmentPagerAdapter(fm) {
    override fun getItem(position: Int): Fragment {
        return fms[position]
    }

    override fun getCount(): Int {
        return fms.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return titles[position]
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
//        super.destroyItem(container, position, `object`)
    }


}
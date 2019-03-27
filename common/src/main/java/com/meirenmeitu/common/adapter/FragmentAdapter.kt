package com.meirenmeitu.common.adapter

import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.meirenmeitu.ui.mvp.BaseFragment

/**
 * Desc:
 * Author: Jooyer
 * Date: 2018-07-30
 * Time: 21:13
 */
class FragmentAdapter(private val fm: FragmentManager,
                      private val fms: List<BaseFragment<*>>)
    : FragmentPagerAdapter(fm) {
    override fun getItem(position: Int): Fragment {
        return fms[position]
    }

    override fun getCount(): Int {
        return fms.size
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
//        super.destroyItem(container, position, `object`)
    }


}
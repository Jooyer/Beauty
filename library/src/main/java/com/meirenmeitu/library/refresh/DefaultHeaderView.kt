package com.meirenmeitu.library.refresh

import android.content.Context
import android.graphics.Color
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import com.meirenmeitu.library.R
import com.meirenmeitu.library.utils.DensityUtils

/**
 * Desc:
 * Author: Jooyer
 * Date: 2019-08-06
 * Time: 17:34
 */
class DefaultHeaderView(context: Context) : LinearLayout(context), IHeaderWrapper {

    private var tvHeaderTip: TextView? = null
    private var ivHeaderTip: ImageView? = null
    private var pbRefreshing: ProgressBar? = null

    init {
        initView()
    }

    private fun initView() {
        val view = LayoutInflater.from(context).inflate(R.layout.header_default, this, false)
        ivHeaderTip = view.findViewById<ImageView>(R.id.iv_tip)
        tvHeaderTip = view.findViewById<TextView>(R.id.tv_tip)
        pbRefreshing = view.findViewById<ProgressBar>(R.id.pb_refreshing)
        val params = LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT)
        params.gravity = Gravity.CENTER_VERTICAL
        setBackgroundColor(Color.WHITE)
        addView(view, params)
    }


    override fun onPullDown(scrollY: Int, headerHeight: Int) {
        pbRefreshing?.visibility = View.GONE
        ivHeaderTip?.visibility = View.VISIBLE
        ivHeaderTip?.setImageResource(R.mipmap.ic_refresh_pull_down)
        tvHeaderTip?.text = "下拉刷新..."
    }

    override fun onPullDownAndReleasable(scrollY: Int, headerHeight: Int) {
        pbRefreshing?.visibility = View.GONE
        ivHeaderTip?.visibility = View.VISIBLE
        tvHeaderTip?.text = "松手可刷新"
    }

    override fun onRefreshReady(scrollY: Int, headerHeight: Int) {
    }

    override fun onRefreshing(scrollY: Int, headerHeight: Int) {
        pbRefreshing?.visibility = View.VISIBLE
        ivHeaderTip?.visibility = View.GONE
        tvHeaderTip?.text = "正在刷新"
    }

    override fun onRefreshComplete(scrollY: Int, headerHeight: Int, isRefreshSuccess: Boolean) {
        pbRefreshing?.visibility = View.GONE
        ivHeaderTip?.visibility = View.VISIBLE
        ivHeaderTip?.setImageResource(R.mipmap.ic_refresh_data_completed)
        tvHeaderTip?.text = "刷新完成"
    }

    override fun onRefreshCancel(scrollY: Int, headerHeight: Int) {

    }

    override fun getRefreshHeight(): Int {
        return DensityUtils.dpToPx(60)
    }

}
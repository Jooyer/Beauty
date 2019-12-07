package com.meirenmeitu.library.refresh

import android.content.Context
import android.graphics.Color
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
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
class DefaultFooterView(context: Context) : LinearLayout(context), IFooterWrapper {

    private var tvFooterTip: TextView? = null
    private var ivFooterTip: ImageView? = null
    private var pbRefreshing: ProgressBar? = null

    init {
        initView()
    }

    private fun initView() {
        val view = LayoutInflater.from(context).inflate(R.layout.footer_default, this, false)
        ivFooterTip = view.findViewById<ImageView>(R.id.iv_tip)
        tvFooterTip = view.findViewById<TextView>(R.id.tv_tip)
        pbRefreshing = view.findViewById<ProgressBar>(R.id.pb_loading)
        val params = LinearLayout.LayoutParams(-1, -2)
        params.gravity = Gravity.CENTER_VERTICAL
        setBackgroundColor(Color.WHITE)
        addView(view, params)
    }


    override fun onPullUp(scrollY: Int) {
        pbRefreshing?.visibility = View.GONE
        ivFooterTip?.visibility = View.VISIBLE
        ivFooterTip?.setImageResource(R.mipmap.ic_refresh_pull_up)
        tvFooterTip?.text = "上拉加载更多"
    }

    override fun onPullUpAndReleasable(scrollY: Int) {
        pbRefreshing?.visibility = View.GONE
        ivFooterTip?.visibility = View.VISIBLE
        tvFooterTip?.text = "松手可加载"
    }

    override fun onLoadReady(scrollY: Int) {

    }

    override fun onLoading(scrollY: Int) {
        pbRefreshing?.visibility = View.VISIBLE
        ivFooterTip?.visibility = View.GONE
        tvFooterTip?.text = "正在加载..."
    }

    override fun onLoadComplete(scrollY: Int, isLoadSuccess: Boolean) {
        pbRefreshing?.visibility = View.GONE
        ivFooterTip?.visibility = View.VISIBLE
        ivFooterTip?.setImageResource(R.mipmap.ic_refresh_data_completed)
        tvFooterTip?.text = "加载完成"
    }

    override fun onLoadCancel(scrollY: Int) {

    }

    override fun onNoMore() {
        tvFooterTip?.text = "没有更多数据"
    }

    override fun getLoadHeight(): Int {
      return  DensityUtils.dpToPx(50)
    }

}
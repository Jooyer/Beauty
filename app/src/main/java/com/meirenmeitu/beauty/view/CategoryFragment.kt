package com.meirenmeitu.beauty.view


import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.util.SparseArray
import android.view.View
import android.widget.ImageView
import androidx.core.util.set
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.meirenmeitu.base.adapter.CommonAdapter
import com.meirenmeitu.base.adapter.MultiItemTypeAdapter
import com.meirenmeitu.base.adapter.ViewHolder
import com.meirenmeitu.base.other.NormalDecoration
import com.meirenmeitu.beauty.R
import com.meirenmeitu.beauty.bean.ImageBean
import com.meirenmeitu.beauty.presenter.CategoryPresenter
import com.meirenmeitu.library.dragview.OnDataListener
import com.meirenmeitu.library.refresh.BounceCallBack
import com.meirenmeitu.library.refresh.EventForwardingHelper
import com.meirenmeitu.library.refresh.NormalBounceHandler
import com.meirenmeitu.library.refresh.footer.DefaultFooter
import com.meirenmeitu.library.refresh.header.DefaultHeader
import com.meirenmeitu.library.utils.*
import com.meirenmeitu.ui.mvp.BaseFragment
import com.tencent.mmkv.MMKV
import kotlinx.android.synthetic.main.fragment_category.*


/**
 * 类别界面
 */
class CategoryFragment : BaseFragment<CategoryPresenter>() {
    private val mImages = ArrayList<ImageBean>()

    private val imageUrls = arrayListOf<String>(
        "https://img-my.csdn.net/uploads/201508/05/1438760758_3497.jpg",
        "https://img-my.csdn.net/uploads/201508/05/1438760758_6667.jpg",
        "https://img-my.csdn.net/uploads/201508/05/1438760757_3588.jpg",
        "https://img-my.csdn.net/uploads/201508/05/1438760756_3304.jpg",
        "https://img-my.csdn.net/uploads/201508/05/1438760755_6715.jpeg",
        "https://img-my.csdn.net/uploads/201508/05/1438760726_5120.jpg",
        "https://img-my.csdn.net/uploads/201508/05/1438760726_8364.jpg",
        "https://img-my.csdn.net/uploads/201508/05/1438760725_4031.jpg",
        "https://img-my.csdn.net/uploads/201508/05/1438760724_9463.jpg",
        "https://img-my.csdn.net/uploads/201508/05/1438760724_2371.jpg",
        "https://img-my.csdn.net/uploads/201508/05/1438760707_4653.jpg",
        "https://img-my.csdn.net/uploads/201508/05/1438760706_6864.jpg",
        "https://img-my.csdn.net/uploads/201508/05/1438760706_9279.jpg",
        "https://img-my.csdn.net/uploads/201508/05/1438760704_2341.jpg",
        "https://img-my.csdn.net/uploads/201508/05/1438760704_5707.jpg",
        "https://img-my.csdn.net/uploads/201508/05/1438760685_5091.jpg",
        "https://img-my.csdn.net/uploads/201508/05/1438760685_4444.jpg",
        "https://img-my.csdn.net/uploads/201508/05/1438760684_8827.jpg",
        "https://img-my.csdn.net/uploads/201508/05/1438760683_3691.jpg",
        "https://img-my.csdn.net/uploads/201508/05/1438760683_7315.jpg",
        "https://img-my.csdn.net/uploads/201508/05/1438760663_7318.jpg",
        "https://img-my.csdn.net/uploads/201508/05/1438760662_3454.jpg",
        "https://img-my.csdn.net/uploads/201508/05/1438760662_5113.jpg",
        "https://img-my.csdn.net/uploads/201508/05/1438760661_3305.jpg",
        "https://img-my.csdn.net/uploads/201508/05/1438760661_7416.jpg",
        "https://img-my.csdn.net/uploads/201508/05/1438760589_2946.jpg",
        "https://img-my.csdn.net/uploads/201508/05/1438760589_1100.jpg",
        "https://img-my.csdn.net/uploads/201508/05/1438760588_8297.jpg",
        "https://img-my.csdn.net/uploads/201508/05/1438760587_2575.jpg",
        "https://img-my.csdn.net/uploads/201508/05/1438760587_8906.jpg",
        "https://img-my.csdn.net/uploads/201508/05/1438760550_2875.jpg",
        "https://img-my.csdn.net/uploads/201508/05/1438760550_9517.jpg",
        "https://img-my.csdn.net/uploads/201508/05/1438760549_7093.jpg",
        "https://img-my.csdn.net/uploads/201508/05/1438760549_1352.jpg",
        "https://img-my.csdn.net/uploads/201508/05/1438760548_2780.jpg",
        "https://img-my.csdn.net/uploads/201508/05/1438760531_1776.jpg",
        "https://img-my.csdn.net/uploads/201508/05/1438760531_1380.jpg",
        "https://img-my.csdn.net/uploads/201508/05/1438760530_4944.jpg",
        "https://img-my.csdn.net/uploads/201508/05/1438760530_5750.jpg",
        "https://img-my.csdn.net/uploads/201508/05/1438760529_3289.jpg",
        "https://img-my.csdn.net/uploads/201508/05/1438760500_7871.jpg",
        "https://img-my.csdn.net/uploads/201508/05/1438760500_6063.jpg",
        "https://img-my.csdn.net/uploads/201508/05/1438760499_6304.jpeg",
        "https://img-my.csdn.net/uploads/201508/05/1438760499_5081.jpg",
        "https://img-my.csdn.net/uploads/201508/05/1438760498_7007.jpg",
        "https://img-my.csdn.net/uploads/201508/05/1438760478_3128.jpg",
        "https://img-my.csdn.net/uploads/201508/05/1438760478_6766.jpg",
        "https://img-my.csdn.net/uploads/201508/05/1438760477_1358.jpg",
        "https://img-my.csdn.net/uploads/201508/05/1438760477_3540.jpg",
        "https://img-my.csdn.net/uploads/201508/05/1438760476_1240.jpg",
        "https://img-my.csdn.net/uploads/201508/05/1438760446_7993.jpg",
        "https://img-my.csdn.net/uploads/201508/05/1438760446_3641.jpg",
        "https://img-my.csdn.net/uploads/201508/05/1438760445_3283.jpg",
        "https://img-my.csdn.net/uploads/201508/05/1438760444_8623.jpg",
        "https://img-my.csdn.net/uploads/201508/05/1438760444_6822.jpg",
        "https://img-my.csdn.net/uploads/201508/05/1438760422_2224.jpg",
        "https://img-my.csdn.net/uploads/201508/05/1438760421_2824.jpg",
        "https://img-my.csdn.net/uploads/201508/05/1438760420_2660.jpg",
        "https://img-my.csdn.net/uploads/201508/05/1438760420_7188.jpg",
        "https://img-my.csdn.net/uploads/201508/05/1438760419_4123.jpg"
    )
    internal val listfragemnt = java.util.ArrayList<Class<*>>()
    private var list_viewholder = SparseArray<ViewHolder>()

    companion object {
        val TAG = CategoryFragment::class.java.simpleName
        fun newInstance(type: Int): CategoryFragment {
            val fragment = CategoryFragment()
            val bundle = Bundle()
            bundle.putInt(TAG, type)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun createPresenter() = CategoryPresenter(this)

    override fun getLayoutId() = R.layout.fragment_category

    override fun setLogic() {
//        val random = Random()
//        val color = Color.rgb(
//            random.nextInt(256),
//            random.nextInt(256),
//            random.nextInt(256)
//        )
//        rv_root_category.setBackgroundColor(color)

//        rv_root_category.postDelayed({
//            setAdapter()
//        },400)

        //设置滚动冲突的控制类
        bl_container_category.setBounceHandler(NormalBounceHandler(), rv_root_category)
        //设置刷新头，null意味着没有刷新头，不调用该函数意为着空
        bl_container_category.setHeaderView(DefaultHeader(mActivity), cl_container_category)
        bl_container_category.setFooterView(DefaultFooter(mActivity), cl_container_category)

        //自定义事件分发处理
        bl_container_category.setEventForwardingHelper(EventForwardingHelper { downX, downY, moveX, moveY ->
            true
        })

        bl_container_category.setBounceCallBack(object : BounceCallBack {
            //刷新回调
            override fun startRefresh() {
                arguments?.getInt(TAG,0).let {
                    mPresenter.getImages(it?:0)
                }
            }

            override fun startLoadingMore() {
                Handler().postDelayed(Runnable {
                    bl_container_category.setLoadingMoreCompleted()
                }, 2000)
            }
        })
    }

    override fun onFirstUserVisible() {
        setAdapter()
        bl_container_category.autoRefresh()
        mPresenter.mImages.observe(this, Observer {
            Log.e("Test", "=============== ")
            mImages.clear()
            mImages.addAll(it)
            rv_root_category.adapter?.notifyDataSetChanged()
        })
    }

    private fun setAdapter() {
        imageUrls.forEach { url ->
            mImages.add(ImageBean(url, "测试"))
            listfragemnt.add(PreviewFragment::class.java)
        }

        rv_root_category.layoutManager = GridLayoutManager(mActivity, 2)

        rv_root_category.addItemDecoration(
            NormalDecoration(mActivity, DensityUtils.dpToPx(3), Color.WHITE)
        )

        val adapter = object : CommonAdapter<ImageBean>(mActivity, R.layout.item_image_category, mImages) {
            val statusBarHeight = StatusBarUtil.getStatusBarHeight(mActivity)
            //            val navigationBarHeight = ScreenUtils.getNavigationBarHeight(mActivity)
            val height = (MMKV.defaultMMKV().decodeInt(Constants.SCREEN_REAL_HEIGHT, 0)
                    - DensityUtils.dpToPx(100) - statusBarHeight) / 2

            override fun convert(holder: ViewHolder, bean: ImageBean, position: Int) {
                val imageView = holder.getView<ImageView>(R.id.iv_content_item_category)
                val param = holder.itemView.layoutParams as RecyclerView.LayoutParams
                if (0 >= height) {
                    param.height = 800
                } else {
                    param.height = height
                }
                holder.itemView.layoutParams = param
                holder.itemView.tag = imageView
                list_viewholder[position] = holder
                ImageLoader.loadImgWithCenterCrop(imageView, bean.url)
            }

        }

        adapter.setOnItemClickListener(object : MultiItemTypeAdapter.OnItemClickListener {
            override fun onItemClick(view: View, holder: RecyclerView.ViewHolder, position: Int) {
                openPreview(position)
            }
        })

        rv_root_category.adapter = adapter
    }

    // https://www.jianshu.com/p/bf2e6e5a3ba0
    fun openPreview(position: Int) {
        PreviewActivity.startPreviewActivity(mActivity, position, object : OnDataListener {
            override fun getListView(): java.util.ArrayList<View> {
                return (0 until list_viewholder.size()).mapTo(ArrayList()) {
                    list_viewholder[it]?.itemView?.tag as View
                }
            }

            override fun getListData(): java.util.ArrayList<Any> {
                return java.util.ArrayList(imageUrls)
            }

            override fun getListFragmentClass(): java.util.ArrayList<Class<*>> {
                return listfragemnt
            }

            override fun onPageSelected(position: Int) {
                val manager = rv_root_category.layoutManager as GridLayoutManager
                if (position < manager.findFirstVisibleItemPosition() || position > manager.findLastVisibleItemPosition()) {
                    rv_root_category.smoothScrollToPosition(position)
                }
            }

            override fun onBackPressed(): Boolean {
                return true
            }

            override fun init() {
            }
        })


//        DragViewActivity.startActivity(mActivity, 0, object : OnDataListener {
//            override fun getListView(): ArrayList<View> {
//                return (0 until list_viewholder.size).mapTo(ArrayList()) {
//                    list_viewholder[it]?.itemView?.tag as View
//                }
//            }
//
//            override fun getListData(): java.util.ArrayList<Any> {
//                return java.util.ArrayList(imageUrls)
//            }
//
//            override fun getListFragmentClass(): java.util.ArrayList<Class<*>> {
//                return listfragemnt
//            }
//
//            override fun onPageSelected(position: Int) {
//                val manager = rv_root_category.layoutManager as GridLayoutManager
//                if (position < manager.findFirstVisibleItemPosition() || position > manager.findLastVisibleItemPosition()) {
//                    rv_root_category.smoothScrollToPosition(position)
//                }
//            }
//
//            override fun onBackPressed(): Boolean {
//                return true
//            }
//
//            override fun init() {
//
//            }
//
//        })
    }

    override fun showError(message: String) {
        JSnackBar.Builder().attachView(cl_container_category)
            .message("message")
            .default()
            .build()
            .default()
            .show()
    }

}

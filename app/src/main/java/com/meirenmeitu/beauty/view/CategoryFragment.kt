package com.meirenmeitu.beauty.view


import android.graphics.Color
import android.util.SparseArray
import android.view.View
import android.widget.ImageView
import androidx.core.util.set
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
import com.meirenmeitu.library.utils.Constants
import com.meirenmeitu.library.utils.DensityUtils
import com.meirenmeitu.library.utils.ImageLoader
import com.meirenmeitu.library.utils.StatusBarUtil
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
        fun newInstance() = CategoryFragment()
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

        rv_root_category.postDelayed({
            setAdapter()
        },400)
    }


    private fun setAdapter() {

        imageUrls.forEach { url ->
            mImages.add(ImageBean(url, "测试"))
            listfragemnt.add(PreviewFragment::class.java)
        }

        rv_root_category.layoutManager = GridLayoutManager(mActivity, 2)

        rv_root_category.addItemDecoration(
            NormalDecoration(mActivity,DensityUtils.dpToPx(3), Color.WHITE)
        )

        val adapter = object : CommonAdapter<ImageBean>(mActivity, R.layout.item_image_category, mImages) {
            val statusBarHeight = StatusBarUtil.getStatusBarHeight(mActivity)
//            val navigationBarHeight = ScreenUtils.getNavigationBarHeight(mActivity)
            val height = (MMKV.defaultMMKV().decodeInt(Constants.SCREEN_REAL_HEIGHT,0)
                    - DensityUtils.dpToPx(100) - statusBarHeight)/2
            override fun convert(holder: ViewHolder, bean: ImageBean, position: Int) {
                val imageView = holder.getView<ImageView>(R.id.iv_content_item_category)
                val param = holder.itemView.layoutParams as RecyclerView.LayoutParams
                if (0 >= height) {
                    param.height = 800
                }else{
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

    override fun bindEvent() {
    }

    // https://www.jianshu.com/p/bf2e6e5a3ba0
    fun openPreview( position: Int) {
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
}

package com.meirenmeitu.beauty.view


import android.graphics.Color
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.util.SparseArray
import android.view.View
import androidx.appcompat.widget.AppCompatImageView
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
import com.meirenmeitu.library.refresh.DefaultFooterView
import com.meirenmeitu.library.refresh.DefaultHeaderView
import com.meirenmeitu.library.refresh.OnRefreshAndLoadListener
import com.meirenmeitu.library.refresh.PowerRefreshLayout
import com.meirenmeitu.library.utils.Constants
import com.meirenmeitu.library.utils.DensityUtils
import com.meirenmeitu.library.utils.ImageLoader
import com.meirenmeitu.library.utils.JSnackBar
import com.meirenmeitu.ui.mvp.BaseFragment
import com.tencent.mmkv.MMKV
import kotlinx.android.synthetic.main.fragment_category.*


/**
 * 类别界面
 */
class CategoryFragment : BaseFragment<CategoryPresenter>() {
    private val mImages = ArrayList<ImageBean>()
    private val mSeries = ArrayList<ImageBean>()
    internal val listfragemnt = java.util.ArrayList<Class<*>>()
    private var list_viewholder = SparseArray<ViewHolder>()

    private var currentPage = 1L
    private var totalPage = 0L

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
    }

    override fun onFirstUserVisible() {
        setAdapter()

        bl_container_category.addHeader(DefaultHeaderView(mActivity))
        bl_container_category.addFooter(DefaultFooterView(mActivity))

        bl_container_category.setOnRefreshAndLoadListener(object :OnRefreshAndLoadListener(){
            override fun onRefresh(refreshLayout: PowerRefreshLayout) {
                refreshLayout.postDelayed({
                    bl_container_category.stopRefresh(true)
                },2500)
            }

            override fun onLoad(refreshLayout: PowerRefreshLayout) {
                refreshLayout.postDelayed({
                    bl_container_category.stopLoadMore(true)
                },2500)
            }
        })


        mPresenter.mImages.observe(this, Observer {
            totalPage = Math.ceil(it.total.toDouble() / Constants.PAGE_INFO_SIZE_PRE_PAGE_10).toLong()
            if (1L == currentPage) {
                mImages.clear()
            }
            mImages.addAll(it.list)

            rv_root_category.adapter?.notifyDataSetChanged()
        })

        mPresenter.mImage.observe(this, Observer {
            Log.e("Test", "===============获取一张图 ")
            mImages.clear()
            mImages.add(it)
            rv_root_category.adapter?.notifyDataSetChanged()
        })

    }

    private fun setAdapter() {
        rv_root_category.layoutManager = GridLayoutManager(mActivity, 2)

        rv_root_category.addItemDecoration(
            NormalDecoration(mActivity, DensityUtils.dpToPx(4), Color.WHITE)
        )

        val adapter = object : CommonAdapter<String>(mActivity, R.layout.item_image_category, imageUrls) {
            private val location = Rect()
            override fun convert(holder: ViewHolder, bean: String, position: Int) {
                val imageView = holder.getView<AppCompatImageView>(R.id.iv_content_item_category)
//                holder.getView<TextView>(R.id.tv_content_item_title).text = bean.imageName
//                holder.getView<TextView>(R.id.tv_content_item_like).text = "${bean.likeCount}"
//                holder.getView<TextView>(R.id.tv_content_item_count).text =
//                    MessageFormat.format("{0}P", bean.seriesCount)
//

                location.setEmpty()
                rv_root_category.getGlobalVisibleRect(location)

                val lp = imageView.layoutParams
                lp.width = DensityUtils.getWindowSize(mActivity).widthPixels / 2 - DensityUtils.dpToPx(2)
                val rate = lp.width * 1.0F / location.right
                lp.height = (rate * location.bottom).toInt()
                imageView.layoutParams = lp

                if (0 == position) {
                    MMKV.defaultMMKV().encode(Constants.KEY_WIDTH_HEIGHT_RATE, rate)
                }

                val param = holder.itemView.layoutParams as RecyclerView.LayoutParams
                param.height = lp.height + DensityUtils.dpToPx(45)
                holder.itemView.layoutParams = param

                holder.itemView.tag = imageView
                list_viewholder[position] = holder
//                ImageLoader.loadImgWithCenterCropAndNoPlaceHolder(
//                    imageView,
//                    Constants.BASE_URL.plus(bean.imageId).plus("/")
//                        .plus(bean.imageUrl.split("@@")[0])
//                )
                ImageLoader.loadImgWithCenterCropAndNoPlaceHolder(
                    imageView, bean
                )
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
        listfragemnt.clear()
        mSeries.clear()
        // 点击封面其实里面包含了一个文件夹,里面有多个图片
        val imageBean = mImages[position]
        val urls = imageBean.imageUrl.split("@@")
        urls.forEach {
            listfragemnt.add(PreviewFragment::class.java)
            val image = ImageBean(
                imageBean.imageId,
                imageBean.imageName,
                it,
                imageBean.imageType,
                imageBean.collectCount,
                imageBean.likeCount,
                imageBean.seriesCount,
                imageBean.createTime,
                imageBean.updateTime
            )
            mSeries.add(image)
        }

        Log.e("Test", "openPreview============ ${listfragemnt.size}")
        PreviewActivity.startPreviewActivity(mActivity, position, object : OnDataListener {
            override val listView: ArrayList<View>
                get() = (0 until list_viewholder.size()).mapTo(ArrayList()) {
                    list_viewholder[it]?.itemView?.tag as View
                }
            override val listData: ArrayList<ImageBean>
                get() = mSeries
            override val listFragmentClass: ArrayList<Class<*>>
                get() = listfragemnt

            override fun onPageSelected(position: Int) {
//                val manager = rv_root_category.layoutManager as GridLayoutManager
//                if (position < manager.findFirstVisibleItemPosition() || position > manager.findLastVisibleItemPosition()) {
//                    rv_root_category.smoothScrollToPosition(position)
//                }
            }

            override fun onBackPressed(): Boolean = true

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
        JSnackBar.Builder().attachView(bl_container_category)
            .message("message")
            .default()
            .build()
            .default()
            .show()
    }


    private val imageUrls = arrayListOf<String>(
        "https://img-my.csdn.net/uploads/201508/05/1438760758_3497.jpg",
        "https://img-my.csdn.net/uploads/201508/05/1438760758_6667.jpg",
        "https://img-my.csdn.net/uploads/201508/05/1438760757_3588.jpg",
        "https://img-my.csdn.net/uploads/201508/05/1438760756_3304.jpg"
    )

}

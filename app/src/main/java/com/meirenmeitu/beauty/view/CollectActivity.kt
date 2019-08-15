package com.meirenmeitu.beauty.view

import android.content.Intent
import android.graphics.Color
import android.util.SparseArray
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.util.set
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.meirenmeitu.base.adapter.CommonAdapter
import com.meirenmeitu.base.adapter.MultiItemTypeAdapter
import com.meirenmeitu.base.adapter.ViewHolder
import com.meirenmeitu.base.other.NormalDecoration
import com.meirenmeitu.beauty.R
import com.meirenmeitu.beauty.bean.ImageBean
import com.meirenmeitu.beauty.presenter.CollectPresenter
import com.meirenmeitu.library.dragview.OnDataListener
import com.meirenmeitu.library.rxbind.RxView
import com.meirenmeitu.library.utils.Constants
import com.meirenmeitu.library.utils.DensityUtils
import com.meirenmeitu.library.utils.ImageLoader
import com.meirenmeitu.library.utils.StatusBarUtil
import com.meirenmeitu.ui.mvp.BaseActivity
import com.tencent.mmkv.MMKV
import kotlinx.android.synthetic.main.activity_collect.*
import kotlinx.android.synthetic.main.fragment_category.*

/**
 * 我的收藏
 */
class CollectActivity : BaseActivity<CollectPresenter>() {
    private val mImages = ArrayList<ImageBean>()
    internal val listfragemnt = java.util.ArrayList<Class<*>>()
    private var list_viewholder = SparseArray<ViewHolder>()
    override fun needUseImmersive() = true
    override fun requestWindowFeature() {
        StatusBarUtil.setStatusBarColor(this, ContextCompat.getColor(this, R.color.main_theme_color))
    }

    override fun createPresenter() = CollectPresenter(this)

    override fun getLayoutId() = R.layout.activity_collect

    override fun setLogic() {
        when (intent.getIntExtra(Constants.KEY_DRAWER_TYPE, 0)) {
            0 -> ct_toolbar.tv_center_title_menu.text = getString(R.string.comm_collect)
            1 -> ct_toolbar.tv_center_title_menu.text = getString(R.string.comm_download)
            2 -> ct_toolbar.tv_center_title_menu.text = getString(R.string.comm_appreciate)
        }
        setAdapter()
    }

    override fun bindEvent() {
        RxView.setOnClickListeners(this, ct_toolbar.iv_left_icon_menu, ct_toolbar)


    }

    override fun onClick(view: View) {
        when (view) {
            ct_toolbar.iv_left_icon_menu -> finish()

            ct_toolbar -> {
                startActivity(Intent(this@CollectActivity,LoginActivity::class.java))
            }
        }
    }

    private fun setAdapter() {

        imageUrls.forEach { url ->
            //            mImages.add(Image(url, "测试"))
            listfragemnt.add(PreviewFragment::class.java)
        }

        rv_list_images.layoutManager = GridLayoutManager(this, 2)
//        PagerSnapHelper().attachToRecyclerView(rv_root_category)

//        rv_root_category.addItemDecoration(object : RecyclerView.ItemDecoration() {
//            override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
//                val pos = parent.getChildAdapterPosition(view)
//                if (pos % 2 == 0) { // 左边
//                    outRect.right = TypedValue.applyDimension(
//                        TypedValue.COMPLEX_UNIT_DIP, 6f, view.resources.displayMetrics
//                    ).toInt()
//                    outRect.left = 0
//                } else { // 右边
//                    outRect.left = TypedValue.applyDimension(
//                        TypedValue.COMPLEX_UNIT_DIP, 6f, view.resources.displayMetrics
//                    ).toInt()
//                    outRect.right = 0
//                }
//                outRect.bottom = TypedValue.applyDimension(
//                    TypedValue.COMPLEX_UNIT_DIP, 6f, view.resources.displayMetrics
//                ).toInt()
//                if (0 == pos || 1 == pos) {
//                    outRect.top = 0
//                } else {
//                    outRect.top = outRect.bottom
//                }
//            }
//        })

        rv_list_images.addItemDecoration(
            NormalDecoration(this, DensityUtils.dpToPx(3), Color.WHITE)
        )

        val adapter = object : CommonAdapter<ImageBean>(this, R.layout.item_image_category, mImages) {
            override fun convert(holder: ViewHolder, bean: ImageBean, position: Int) {
                val imageView = holder.getView<ImageView>(R.id.iv_content_item_category)
                holder.getView<TextView>(R.id.tv_content_item_title).text = bean.imageName

                val lp = imageView.layoutParams
                lp.width = DensityUtils.getWindowSize(this@CollectActivity).widthPixels / 2 - DensityUtils.dpToPx(2)
                lp.height = (lp.width / MMKV.defaultMMKV().decodeFloat(Constants.KEY_NETWORK_STATE, 1.0F)).toInt()
                imageView.layoutParams = lp

                val param = holder.itemView.layoutParams as RecyclerView.LayoutParams
                param.height = lp.height + DensityUtils.dpToPx(45)
                holder.itemView.layoutParams = param

                holder.itemView.tag = imageView
                list_viewholder[position] = holder
                ImageLoader.loadImgWithCenterCrop(
                    imageView,
                    Constants.BASE_URL.plus(bean.imageId).plus("/")
                        .plus(bean.imageUrl.split("@@")[0])
                )
            }

        }

        adapter.setOnItemClickListener(object : MultiItemTypeAdapter.OnItemClickListener {
            override fun onItemClick(view: View, holder: RecyclerView.ViewHolder, position: Int) {
                openPreview(position)
            }
        })

        rv_list_images.adapter = adapter
    }

    // https://www.jianshu.com/p/bf2e6e5a3ba0
    fun openPreview(position: Int) {
        PreviewActivity.startPreviewActivity(this@CollectActivity, position, object : OnDataListener {
            override val listView: ArrayList<View>
                get() = (0 until list_viewholder.size()).mapTo(ArrayList()) {
                    list_viewholder[it]?.itemView?.tag as View
                }
            override val listData: ArrayList<ImageBean>
                get() = mImages
            override val listFragmentClass: ArrayList<Class<*>>
                get() = listfragemnt

            override fun onPageSelected(position: Int) {
                val manager = rv_root_category.layoutManager as GridLayoutManager
                if (position < manager.findFirstVisibleItemPosition() || position > manager.findLastVisibleItemPosition()) {
                    rv_root_category.smoothScrollToPosition(position)
                }
            }

            override fun onBackPressed(): Boolean = true

            override fun init() {

            }
        })
    }

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
}
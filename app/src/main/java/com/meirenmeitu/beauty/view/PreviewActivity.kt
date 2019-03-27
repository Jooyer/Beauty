package com.meirenmeitu.beauty.view

import android.app.Activity
import android.content.Intent
import android.view.View
import android.view.ViewTreeObserver
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.ViewPager
import com.meirenmeitu.beauty.R
import com.meirenmeitu.beauty.bean.ImageBean
import com.meirenmeitu.beauty.presenter.PreviewPresenter
import com.meirenmeitu.library.dragview.*
import com.meirenmeitu.library.utils.DensityUtils
import com.meirenmeitu.library.utils.ScreenUtils
import com.meirenmeitu.library.utils.StatusBarUtil
import com.meirenmeitu.ui.mvp.BaseActivity
import kotlinx.android.synthetic.main.activity_preview.*

/**
 *  仿微信图片拖拽
 * https://www.jianshu.com/p/297e6af61ee6
 * https://www.jianshu.com/p/dffca954fb52
 * https://www.jianshu.com/p/3c6384973db6
 *
 * https://github.com/HpWens/MeiWidgetView (更多效果)
 *
 * 仿小视频拖拽
 * https://www.jianshu.com/p/bf2e6e5a3ba0
 * https://blog.csdn.net/u012551350/article/details/80641638
 * https://github.com/HpWens/MeiWidgetView
 *
 * 图片浏览界面
 *
 *  ImageView.setPhotoUri(Uri.parse("res://" + getPackageName() + "/" + R.mipmap.ic_mei_ripple));
 */

class PreviewActivity : BaseActivity<PreviewPresenter>(), DragViewLayout.OnDrawerStatusListener,
    DragViewLayout.OnCurViewListener,
    ViewPager.OnPageChangeListener {

    private val mImages = ArrayList<ImageBean>()
    private val imageUrls = arrayOf(
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

    private var onDrawerOffsetListener: OnDrawerOffsetListener? = null
    private lateinit var mDragStatePageAdapter: DragStatePagerAdapter
    private var currentPosition = 0;

    override fun createPresenter() = PreviewPresenter(this)

    override fun useStartAnim() = false
    override fun getLayoutId() = R.layout.activity_preview
    override fun setLogic() {
        iv_black_background.setBackgroundColor(ContextCompat.getColor(this,R.color.color_2E2E2E))
        resetToolbar()
        currentPosition = intent.getIntExtra(KEY_CURRENT_POSITION, 0)
        viewPager.setDragViewLayout(dragLayout)
        dragLayout.dragView = viewPager
        setAdapter()
    }


    override fun bindEvent() {
        dragLayout.setOnDrawerStatusListener(this)
        dragLayout.setOnCurViewListener(this)
        dragLayout.setOnDrawerOffsetListener {
            onDrawerOffsetListener?.onDrawerOffset(it)
            cl_bottom_container.alpha = it - 0.3F
        }
    }

    override fun getCurView(): DragViewLayout.ImageBean? {
        if (null != onDataListener){
            return getImageBean(if (null != onDataListener && onDataListener!!.listView != null)
                onDataListener!!.listView[currentPosition] else null)
        }
        return null
    }

    override fun onStatus(status: Int) {
        if (DragViewLayout.CLOSE == status){
            onFinish()
        }
        val fragment: DragFragment = mDragStatePageAdapter.getItem(currentPosition) as DragFragment
        fragment.onDragStatus(status)
    }

    override fun onPageScrollStateChanged(state: Int) {}

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
        if (0F == positionOffset){
            dragLayout.isStop = false
            currentPosition = position
            val fragment: DragFragment = mDragStatePageAdapter.getItem(position) as DragFragment
            dragLayout.setScaleView(fragment.dragView)
            fragment.init()
            dragLayout.setStartView(getImageBean(if (null != onDataListener && onDataListener!!.listView != null)
                onDataListener!!.listView[currentPosition] else null))
            if (fragment.dragView is PinchImageView){
                (fragment.dragView as PinchImageView).setDragViewLayout(dragLayout)
            }
        }else{
            dragLayout.isStop = true
        }
    }

    override fun onPageSelected(position: Int) {
        onDataListener?.onPageSelected(position)
    }


    private fun setAdapter() {
        mDragStatePageAdapter = DragStatePagerAdapter(
            supportFragmentManager,
            if (null != onDataListener) onDataListener!!.listFragmentClass else null,
            if (null != onDataListener) onDataListener!!.listData else null
        )

        viewPager.adapter = mDragStatePageAdapter
        viewPager.currentItem = currentPosition
        viewPager.addOnPageChangeListener(this)
        viewPager.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                viewPager.viewTreeObserver.removeOnGlobalLayoutListener(this)
                onPageSelected(currentPosition)
            }
        })

        onDataListener?.init()

//        imageUrls.forEach { url ->
//            mImages.add(ImageBean(url, "测试"))
//        }
//
//        rv_content_preview.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
//        PagerSnapHelper().attachToRecyclerView(rv_content_preview)
//        val adapter = object : CommonAdapter<ImageBean>(this, R.layout.item_image_category, mImages) {
//            override fun convert(holder: ViewHolder, bean: ImageBean, position: Int) {
//                println("============== ${bean.url} : ${bean.desc}")
//                ImageLoader.loadImgWithCenterCrop(holder.getView(R.id.iv_content_item_category), bean.url)
//            }
//        }
//
//        adapter.setOnItemClickListener(object : MultiItemTypeAdapter.OnItemClickListener {
//            override fun onItemClick(view: View, holder: RecyclerView.ViewHolder, position: Int) {
//                Toast.makeText(this@PreviewActivity, "position:$position", Toast.LENGTH_SHORT).show()
//            }
//        })
//
//        rv_content_preview.adapter = adapter
    }


    /**
     *  重置 Toolbar 高度
     */
    private fun resetToolbar() {
//        val param = ct_toolbar.layoutParams as ConstraintLayout.LayoutParams
//        val statusBarHeight = StatusBarUtil.getStatusBarHeight(this)
//        ct_toolbar.setPadding(0, statusBarHeight, 0, 0)
//        param.height = DensityUtils.dpToPx(50) + statusBarHeight
//        ct_toolbar.layoutParams = param
    }

    fun getImageBean(view: View?): DragViewLayout.ImageBean? {
        if (view == null) return null
        val imageBean = DragViewLayout.ImageBean()
        val location = IntArray(2)
        view.getLocationOnScreen(location)
        imageBean.left = location[0]
        imageBean.top = location[1]
        imageBean.width = view.width
        imageBean.height = view.height

        println("======= $imageBean")
        println("=======width: " + ScreenUtils.getRealWidth(this))
        println("=======height: " + ScreenUtils.getRealHeight(this))
        println("=======width: " + DensityUtils.getWindowSize(this).widthPixels)
        println("=======height: " + DensityUtils.getWindowSize(this).heightPixels)
        println("=======statusBarHeight: " + StatusBarUtil.getStatusBarHeight(this))
        return imageBean
    }

    fun onFinish() {
        finish()
        overridePendingTransition(0, 0)
    }

    override fun onBackPressed() {
        if (null != onDataListener && !onDataListener!!.onBackPressed()) {
            return
        }
        dragLayout.close()
    }

    override fun onDestroy() {
        super.onDestroy()
        dragLayout.removeOnDrawerStatusListener()
        dragLayout.removeOnCurViewListener()
        dragLayout.removeOnDrawerOffsetListener()
        onDrawerOffsetListener = null
        onDataListener = null
    }

    companion object {
        const val KEY_CURRENT_POSITION = "KEY_CURRENT_POSITION"
        private var onDataListener: OnDataListener? = null
        fun startPreviewActivity(activity: Activity, position: Int, listener: OnDataListener) {
            val intent = Intent(activity, PreviewActivity::class.java)
            intent.putExtra(KEY_CURRENT_POSITION, position)
            onDataListener = listener
            activity.startActivity(intent)
            activity.overridePendingTransition(0, 0)
        }
    }

}

package com.meirenmeitu.beauty.view

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import com.meirenmeitu.beauty.R
import com.meirenmeitu.beauty.bean.ImageBean
import com.meirenmeitu.library.dragview.DragFragment
import com.meirenmeitu.library.dragview.PinchImageView
import com.meirenmeitu.library.utils.Constants
import com.meirenmeitu.library.utils.ImageLoader
import com.meirenmeitu.library.utils.ScreenUtils

/**
 *  浏览图片/视频
 */
class PreviewFragment : DragFragment() {
    private var dragView: PinchImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.fragment_preview)
        super.onCreate(savedInstanceState)
    }

    override fun getDragView(): View? {
        return if (view == null) null else view.findViewById(R.id.dragview)

    }

    override fun initView() {
        dragView = view.findViewById(R.id.dragview) as PinchImageView
        data?.let {
            val image = it as ImageBean
            Log.e("Test", "Drag======= ${ScreenUtils.hasNavigationBar(activity!!)}")
            dragView?.let { view ->
                ImageLoader.loadWithDrawableCenterCropTransition(
                    view as AppCompatImageView,
                    Constants.BASE_URL.plus(image.imageId).plus("/")
                        .plus(image.imageUrl),
                    R.drawable.logo
                )
            }
        }


    }

    override fun init() {
    }

    override fun onDragStatus(status: Int) {
    }
}
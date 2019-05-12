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
//        return if (view == null) null else view.findViewById(R.id.cl_root_preview)
    }

    override fun initView() {
        dragView = view.findViewById(R.id.dragview) as PinchImageView
        Log.e("Test", "initView==========1")
        data?.let {
            val image = it as ImageBean
            Log.e("Test", "initView==========$image")
            dragView?.let {
                ImageLoader.loadWithDrawableCenterCropTransition(
                    it as AppCompatImageView,
//                    Constants.BASE_URL.plus(image.imageId).plus("/").plus(image.imageUrl),
                    Constants.BASE_URL.plus(image.imageId).plus("/")
                        .plus(image.imageUrl.split("@@")[0]),
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
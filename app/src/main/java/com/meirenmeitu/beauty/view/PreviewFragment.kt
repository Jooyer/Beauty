package com.meirenmeitu.beauty.view

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import com.meirenmeitu.beauty.R
import com.meirenmeitu.library.dragview.DragFragment
import com.meirenmeitu.library.dragview.PinchImageView
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

        dragView?.let {
            ImageLoader.loadWithDrawableTransition(
                it as AppCompatImageView,
                "https://img-my.csdn.net/uploads/201508/05/1438760444_6822.jpg",
                R.drawable.logo
            )
        }


    }

    override fun init() {
    }

    override fun onDragStatus(status: Int) {
    }
}
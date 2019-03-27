package com.meirenmeitu.library.utils

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.meirenmeitu.library.R
import java.io.File

/**
 * https://www.jianshu.com/p/aecd92515cea  --> glide4.7.1封装
 * http://www.imooc.com/article/79104 --> Kotlin 下使用 Glide
 *
 *  占位图和过渡动画冲突解决方案
 * DrawableCrossFadeFactory drawableCrossFadeFactory = new DrawableCrossFadeFactory.Builder(300).setCrossFadeEnabled(true).build();
Glide.with(this)
.load(URL_JPG)
.apply(new RequestOptions().placeholder(R.drawable.ic_launcher))
.transition(DrawableTransitionOptions.with(drawableCrossFadeFactory))
.into(imageView);

 *
 * Created by Jooyer
 * Date 2017/12/16
 * Des  http://blog.csdn.net/u013005791/article/details/74532091  ,使用方法
 */

object ImageLoader {


    fun loadImgWithCenterCrop(imageView: ImageView, path: String) {
        Glide.with(imageView.context.applicationContext)
            .load(path)
            .apply(
                RequestOptions()
                    .priority(Priority.HIGH)
                    .placeholder(R.drawable.logo)
                    .error(R.drawable.logo)
                    .centerCrop()
            )
            .into(imageView)
    }

    fun loadImgWithCenterCrop(imageView: ImageView, drawableId: Int) {
        Glide.with(imageView.context.applicationContext)
            .load(drawableId)
            .apply(
                RequestOptions()
                    .priority(Priority.HIGH)
                    .placeholder(R.drawable.logo)
                    .error(R.drawable.logo)
                    .centerCrop()
            )
            .into(imageView)
    }

    fun loadImgWithFitCenter(imageView: ImageView, drawableId: Int) {
        Glide.with(imageView.context.applicationContext)
            .load(drawableId)
            .apply(
                RequestOptions()
                    .priority(Priority.HIGH)
                    .placeholder(R.drawable.logo)
                    .error(R.drawable.logo)
                    .fitCenter()
            )
            .into(imageView)
    }

    fun loadImgWithFitCenter(imageView: ImageView, uri: Uri) {
        Glide.with(imageView.context.applicationContext)
            .load(uri)
            .apply(
                RequestOptions()
                    .priority(Priority.HIGH)

                    .fitCenter()
            )
            .into(imageView)
    }


    fun loadImgWithFitCenter(imageView: ImageView, path: String) {
        Glide.with(imageView.context.applicationContext)
            .load(path)
            .apply(
                RequestOptions()
                    .priority(Priority.HIGH)
                    .placeholder(R.drawable.logo)
                    .error(R.drawable.logo)
                    .fitCenter()
            )
            .into(imageView)
    }

    fun loadImgWithFitXY(imageView: ImageView, path: String) {
        Glide.with(imageView.context.applicationContext)
            .load(path)
            .apply(
                RequestOptions()
                    .priority(Priority.HIGH)
                    .placeholder(R.drawable.logo)
                    .error(R.drawable.logo)
                    .dontAnimate()
                    .centerCrop()
            )
            .into(imageView)
    }


    fun loadImgWithFitCenter(imageView: ImageView, file: File) {
        Glide.with(imageView.context.applicationContext)
            .load(file)
            .apply(
                RequestOptions()
                    .priority(Priority.HIGH)
                    .placeholder(R.drawable.logo)
                    .error(R.drawable.logo)
                    .fitCenter()
            )
            .into(imageView)
    }


    fun loadImgWithFitCenter(imageView: ImageView, uri: Uri, drawable: Drawable) {
        Glide.with(imageView.context.applicationContext)
            .load(uri)
            .apply(
                RequestOptions()
                    .priority(Priority.HIGH)
                    .placeholder(drawable)
                    .error(drawable)
                    .fitCenter()
            )
            .into(imageView)
    }


    fun loadImgWithFitCenter(imageView: ImageView, path: String, drawable: Drawable) {
        Glide.with(imageView.context.applicationContext)
            .load(path)
            .apply(
                RequestOptions()
                    .priority(Priority.HIGH)

                    .placeholder(drawable)
                    .error(drawable)
                    .fitCenter()
            )
            .into(imageView)
    }

    fun loadImgWithFitCenter(imageView: ImageView, drawable: Drawable) {
        Glide.with(imageView.context.applicationContext)
            .load(drawable)
            .apply(
                RequestOptions()
                    .priority(Priority.HIGH)
                    .placeholder(R.drawable.logo)
                    .error(R.drawable.logo)
                    .fitCenter()
            )
            .into(imageView)
    }


    fun loadImgWithFitCenter(imageView: ImageView, file: File, drawable: Drawable) {
        Glide.with(imageView.context.applicationContext)
            .load(file)
            .apply(
                RequestOptions()
                    .priority(Priority.HIGH)
                    .placeholder(R.drawable.logo)
                    .error(R.drawable.logo)
                    .placeholder(drawable)
                    .fitCenter()
            )
            .into(imageView)
    }

    fun loadCircleImg(imageView: ImageView, path: String) {
        Glide.with(imageView.context.applicationContext)
            .load(path)
            .apply(
                RequestOptions()
                    .priority(Priority.HIGH)

                    .placeholder(R.drawable.logo)
                    .error(R.drawable.logo)
                    .dontAnimate()
                    .centerCrop()
                    .circleCrop()
            )
            .into(imageView)
    }

    fun loadCircleImg(imageView: ImageView, drawable: Drawable) {
        Glide.with(imageView.context.applicationContext)
            .load(drawable)
            .apply(
                RequestOptions()
                    .priority(Priority.HIGH)

                    .placeholder(R.drawable.logo)
                    .error(R.drawable.logo)
                    .dontAnimate()
                    .centerCrop()
                    .circleCrop()
            )
            .into(imageView)
    }

    fun loadImageToBitmap(imageView: ImageView, path: String, drawable: Drawable) {
        Glide.with(imageView.context.applicationContext)
            .asBitmap()
            .load(path)
            .apply(
                RequestOptions()
                    .priority(Priority.HIGH)
                    .placeholder(drawable)
                    .error(drawable)
                    .dontAnimate()
            )
            .into(object : SimpleTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    imageView.background = BitmapDrawable(imageView.resources, resource)
                }
            })
    }

    fun loadWithDrawableTransition(imageView: ImageView, path: String, drawable: Drawable) {
        Glide.with(imageView.context.applicationContext)
            .load(path)
            .transition(DrawableTransitionOptions.withCrossFade())
            .apply(
                RequestOptions()
                    .priority(Priority.HIGH)
                    .centerCrop()
                    .placeholder(drawable)
                    .error(drawable)
            ).into(imageView)

    }

    fun loadWithDrawableTransition(imageView: ImageView, path: String, drawable: Int) {
        Glide.with(imageView.context.applicationContext)
            .load(path)
            .transition(DrawableTransitionOptions.withCrossFade())
            .apply(
                RequestOptions()
//                    .override(imageView.width/2,imageView.height/2)
                    .priority(Priority.HIGH)
                    .fitCenter()
                    .placeholder(drawable)
                    .error(drawable)
            ).into(imageView)

    }

}
/**

RequestOptions options =new RequestOptions()

.placeholder(R.mipmap.ic_launcher)//加载成功之前占位图

.error(R.mipmap.ic_launcher)//加载错误之后的错误图

.override(400,400)//指定图片的尺寸

//指定图片的缩放类型为fitCenter （等比例缩放图片，宽或者是高等于ImageView的宽或者是高。）

.fitCenter()

//指定图片的缩放类型为centerCrop （等比例缩放图片，直到图片的狂高都大于等于ImageView的宽度，然后截取中间的显示。）

.centerCrop()

.circleCrop()//指定图片的缩放类型为centerCrop （圆形）

.skipMemoryCache(true)//跳过内存缓存

.diskCacheStrategy(DiskCacheStrategy.ALL)//缓存所有版本的图像

.diskCacheStrategy(DiskCacheStrategy.NONE)//跳过磁盘缓存

.diskCacheStrategy(DiskCacheStrategy.DATA)//只缓存原来分辨率的图片

.diskCacheStrategy(DiskCacheStrategy.RESOURCE)//只缓存最终的图片 ;

        **/
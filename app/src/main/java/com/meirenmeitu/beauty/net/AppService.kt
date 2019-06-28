package com.meirenmeitu.beauty.net

import com.meirenmeitu.beauty.bean.ImageBean
import com.meirenmeitu.net.cover.TypeData
import com.meirenmeitu.net.response.PageInfo
import io.reactivex.Maybe
import retrofit2.http.GET
import retrofit2.http.Query

/**  https://www.jianshu.com/p/164ce59ea28f Retrofit 系列
 * Desc:
 * Author: Jooyer
 * Date: 2019-03-02
 * Time: 21:22
 */
interface AppService {

    /**
     * 获取某一个系列的图片,如获取 柳岩的 湖边图集
     * @param imageId --> 某个系列的图片ID
     * @param imageCode --> 这个系列的第几张图片,从001开始
     */
    @TypeData(successCode = 200, dataKey = "data")
    @GET("getOneImageInSeries")
    fun getOneImageInSeries(
        @Query("imageId") imageId: String,
        @Query("imageCode") imageCode: String
    ): Maybe<ImageBean>


    /**
     * 获取某一个系列的图片,如获取 柳岩的 湖边图集
     * @param imageId --> 某个系列的图片ID
     */
    @TypeData(successCode = 200, dataKey = "data")
    @GET("getSeries")
    fun getSeries(
        @Query("imageId") imageId: String
    ): Maybe<ImageBean>


    /**
     * 获取多个系列,如获取 性感 的多个系列(柳岩,姚晨)
     * @param type --> 女神, 性感
     * @param page --> 第几页
     * @param size --> 每页多少
     */
    @TypeData(successCode = 200, dataKey = "data")
    @GET("getImages")
    fun getImages(
        @Query("type") type: Int = 0,
        @Query("page") page: Long,
        @Query("size") size: Long
    ): Maybe<PageInfo<List<ImageBean>>>


}
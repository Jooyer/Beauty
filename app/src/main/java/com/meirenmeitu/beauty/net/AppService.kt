package com.meirenmeitu.beauty.net

import com.meirenmeitu.beauty.bean.ImageBean
import com.meirenmeitu.net.cover.TypeData
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
     * 获取图片
     * @param type --> 女神, 性感
     * @param page --> 第几页
     * @param size --> 每页多少
     */
    @TypeData(successCode = 200, listKey = "data")
    @GET("image/category")
    fun getImages(
        @Query("type") type: Int = 1,
        @Query("page") page: Int = 1,
        @Query("size") size: Int = 20
    ): Maybe<List<ImageBean>>

}
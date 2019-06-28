package com.meirenmeitu.beauty.bean

/**
 * Desc:
 * Author: Jooyer
 * Date: 2019-02-15
 * Time: 21:32
 */
data class ImageBean(
    val imageId: String,
    val imageName: String,
    val imageUrl: String,
    val imageType: Int,
    val collectCount: Int,
    val likeCount: Int,
    val seriesCount: Int,
    val createTime: String,
    val updateTime: String
)


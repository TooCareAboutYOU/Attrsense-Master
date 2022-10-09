package com.attrsense.android.model

import kotlinx.serialization.Serializable

/**
 * 图片信息
 * @param create_time 图片上传时间
 * @param filename
 * @param md5 源图片的md5值
 * @param size ANF图片大小, 单位字节
 * @param type 源图片类型
 * @param url ANF图片URL
 */

@Serializable
data class ImageInfoBean(
    val create_time: String,
    val filename: String,
    val md5: String,
    val size: String,
    val type: String,
    val url: String
)

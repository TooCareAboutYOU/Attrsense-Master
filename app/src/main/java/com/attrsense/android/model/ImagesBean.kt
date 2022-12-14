package com.attrsense.android.model

import kotlinx.serialization.Serializable

/**
 * author : zhangshuai@attrsense.com
 * date : 2022/10/8 11:34
 * mark : custom something
 */
@Serializable
data class ImagesBean(val images: List<ImageInfoBean>)

//@Serializable
//data class ImagesBean(val image: ImageInfoBean)
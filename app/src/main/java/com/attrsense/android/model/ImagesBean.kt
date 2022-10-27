package com.attrsense.android.model

import kotlinx.serialization.Serializable

/**
 * author : zhangshuai@attrsense.com
 * date : 2022/10/8 11:34
 * mark : custom something
 */
@Serializable
data class ImagesBean(var images: MutableList<ImageInfoBean>? = null)
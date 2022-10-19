package com.attrsense.android.model

/**
 * author : zhangshuai@attrsense.com
 * date : 2022/10/18 11:17
 * mark : custom something
 */
@kotlinx.serialization.Serializable
data class UserBean(
    var createTime: String? = "",     //用户注册时间(UTC时间).
    var mobile: String? = "",
    var totalSrcImageSize: Int = 0,  //所有图片压缩前的大小.
    var totalImageSize: Int = 0,     //所有图片压缩后的大小.
    var imageCount: Int = 0          //用户所有的压缩图片的个数.
)





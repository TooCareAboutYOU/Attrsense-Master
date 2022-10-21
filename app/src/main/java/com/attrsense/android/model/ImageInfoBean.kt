package com.attrsense.android.model

import com.google.gson.Gson
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
    var createTime: String? = "",         //图片上传时间(UTC时间).
    var srcFilename: String? = "",        //原始图片的文件名.
    var srcType: String? = "",            //原始图片的类型.
    var srcSize: Int = 0,                 //原始图片的大小, 类型int, 单位字节.
    var rate: Int = 0,                    //压缩参数1.
    var roiRate: Int = 0,                 //压缩参数2.
    var fileId: String? = "",             //压缩图片的文件ID(用于删除图片).
    var size: Int = 0,                    //压缩图片的大小, 类型int, 单位字节.
    var url: String? = "",                //压缩图片的URL.
    var width: Int = 0,                   //原始图片的宽.
    var height: Int = 0,                  //原始图片的高.
    var thumbnailFileId: String? = "",    //缩略图的文件ID.
    var thumbnailUrl: String? = "",       //缩略图的URL.
    var cacheImage: String? = ""//当本地下载anf图片后
) {
    override fun toString(): String {
        return Gson().toJson(this)
    }
}

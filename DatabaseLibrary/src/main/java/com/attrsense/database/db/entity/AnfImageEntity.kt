package com.attrsense.database.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.Gson

/**
 * author : zhangshuai@attrsense.com
 * date : 2022/10/20 15:04
 * mark : custom something
 */
@Entity(tableName = "anf_image_table")
data class AnfImageEntity(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int = 0,

    @ColumnInfo(name = "userToken")
    var token: String? = "",

    //本地原图
    @ColumnInfo(name = "originalImage")
    var originalImage: String? = "",

    //本地缩略图，云端使用https网络图
    @ColumnInfo(name = "thumbImage")
    var thumbImage: String? = "",

    //anf文件存储地址
    @ColumnInfo(name = "anfImage")
    var anfImage: String? = "",

    //云端anf文件解码后jpg图片
    @ColumnInfo(name = "cache_Image")
    var cacheImage: String? = "",

    //是否本地数据，还是云端数据
    @ColumnInfo(name = "isLocal")
    var isLocal: Boolean = true,

    //针对远端anf文件是否已经下载
    @ColumnInfo(name = "isDownload")
    var isDownload: Boolean = false

) {

    override fun toString(): String {
        return Gson().toJson(this)
    }

}

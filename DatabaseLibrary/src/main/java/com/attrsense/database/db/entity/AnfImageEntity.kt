package com.attrsense.database.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.attrsense.database.base.BaseEntity
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

    @ColumnInfo(name = "user_token")
    var token: String? = "",

    //用户手机号
    @ColumnInfo(name = "user_mobile")
    var mobile: String? = "",

    //本地原图
    @ColumnInfo(name = "original_image")
    var originalImage: String? = "",

    //本地缩略图，云端使用https网络图
    @ColumnInfo(name = "thumb_image")
    var thumbImage: String? = "",

    //anf文件存储地址
    @ColumnInfo(name = "anf_image")
    var anfImage: String? = "",

    //云端anf文件解码后jpg图片
    @ColumnInfo(name = "cache_image")
    var cacheImage: String? = "",

    //压缩图片的文件ID(用于删除图片).
    @ColumnInfo(name = "fileId")
    var fileId: String? = "",

    //原图大小
    @ColumnInfo(name = "src_size")
    var srcSize: Int = 0,

    //是否本地数据，还是云端数据
    @ColumnInfo(name = "is_local")
    var isLocal: Boolean = true,

    //针对远端anf文件是否已经下载
    @ColumnInfo(name = "is_download")
    var isDownload: Boolean = false

) : BaseEntity()
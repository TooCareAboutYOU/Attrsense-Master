package com.attrsense.database.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

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

    //本地使用原图，云端使用https网络图
    @ColumnInfo(name = "originalImage")
    var originalImage: String? = "",

    @ColumnInfo(name = "anfImage")
    var anfImage: String? = "",

    //云端anf文件解码后jpg图片
    @ColumnInfo(name = "cache_Image")
    var cacheImage: String? = "",

    @ColumnInfo(name = "isLocal")
    var isLocal: Boolean = true
)

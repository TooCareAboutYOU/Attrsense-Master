package com.attrsense.database.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.attrsense.database.base.BaseEntity

/**
 * @author zhangshuai@attrsense.com
 * @date 2022/11/7 11:26
 * @description
 */
@Entity(tableName = "local_anf_data_table")
data class LocalAnfDataEntity(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int = 0,

    //用户手机号
    @ColumnInfo(name = "user_mobile")
    var mobile: String? = "",

    //原图总大小
    @ColumnInfo(name = "original_size")
    var originalAllSize: Int = 0,

    //ANF总大小
    @ColumnInfo(name = "anf_all_size")
    var anfAllSize: Int = 0,

    //压缩的数量
    @ColumnInfo(name = "count")
    var count: Int = 0
) : BaseEntity()

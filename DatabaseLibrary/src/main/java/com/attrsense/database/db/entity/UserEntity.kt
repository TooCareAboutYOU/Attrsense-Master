package com.attrsense.database.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.attrsense.database.base.BaseEntity

@Entity(tableName = "user_table")
data class UserEntity(
    //索引，自增
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id", typeAffinity = ColumnInfo.INTEGER)
    var id: Int = 0,

    //用户名
    @ColumnInfo(name = "name", typeAffinity = ColumnInfo.TEXT)
    var name: String? = "",

    //用户手机号
    @ColumnInfo(name = "mobile", typeAffinity = ColumnInfo.TEXT)
    var mobile: String? = "",

    //密码
    @ColumnInfo(name = "token", typeAffinity = ColumnInfo.TEXT)
    var token: String? = "",

    //密码
    @ColumnInfo(name = "refresh_token", typeAffinity = ColumnInfo.TEXT)
    var refreshToken: String? = ""

) : BaseEntity()

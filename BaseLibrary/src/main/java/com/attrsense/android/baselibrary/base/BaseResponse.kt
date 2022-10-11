package com.attrsense.android.baselibrary.base

import com.google.gson.Gson
import kotlinx.serialization.Serializable

/**
 * author : zhangshuai@attrsense.com
 * date : 2022/10/8 10:55
 * mark : custom something
 */
@Serializable
open class BaseResponse<T>(
    val data: T,
    val message: String,
    val errorCode: String,
) {
    override fun toString(): String {
        return Gson().toJson(this)
    }
}
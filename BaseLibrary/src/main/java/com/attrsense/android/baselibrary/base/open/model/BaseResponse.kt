package com.attrsense.android.baselibrary.base.open.model

import com.google.gson.Gson
import kotlinx.serialization.Serializable

/**
 * author : zhangshuai@attrsense.com
 * date : 2022/10/8 10:55
 * mark : custom something
 */
@Serializable
open class BaseResponse<T>(
    var data: T? = null,
    var message: String? = "",
    var errorCode: String? = "",
) {
    override fun toString(): String {
        return Gson().toJson(this)
    }
}
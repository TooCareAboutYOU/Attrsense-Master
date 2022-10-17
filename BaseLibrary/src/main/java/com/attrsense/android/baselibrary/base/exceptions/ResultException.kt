package com.attrsense.android.baselibrary.base.exceptions

/**
 * author : zhangshuai@attrsense.com
 * date : 2022/10/17 18:28
 * mark : custom something
 */
class ResultException constructor(private val errorCode: String?, private val msg: String?) :
    RuntimeException(msg) {

    fun getErrorCode(): String? = errorCode
}
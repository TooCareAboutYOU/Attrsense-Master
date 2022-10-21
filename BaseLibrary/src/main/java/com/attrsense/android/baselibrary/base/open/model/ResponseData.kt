package com.attrsense.android.baselibrary.base.open.model


/**
 * author : zhangshuai@attrsense.com
 * date : 2022/10/18 15:02
 * mark : 网络接口请求回调
 */
sealed class ResponseData<out T : Any?> {

    //请求成功
    data class onSuccess<out T : Any>(val value: T?) : ResponseData<T>()

    //请求失败
    data class onFailed(val throwable: Throwable) : ResponseData<Nothing>()
}

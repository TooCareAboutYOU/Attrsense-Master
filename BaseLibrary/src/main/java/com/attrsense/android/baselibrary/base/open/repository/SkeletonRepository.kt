package com.attrsense.android.baselibrary.base.open.repository

import com.attrsense.android.baselibrary.base.open.model.ResponseData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*

/**
 * author : zhangshuai@attrsense.com
 * date : 2022/10/12 9:31
 * mark :
 */
open class SkeletonRepository {

    private val map: MutableMap<String, Any?> by lazy { mutableMapOf() }

    protected fun getBody(): MutableMap<String, Any?> {
        map.clear()
        return map
    }

    protected fun <T> request(
        block: suspend () -> T
    ): Flow<ResponseData<T>> {
        return flow {
            emit(ResponseData.onSuccess(block()))
        }.flowOn(Dispatchers.Default)
    }
}
package com.attrsense.android.baselibrary.base.open.repository

import android.util.Log
import com.attrsense.android.baselibrary.base.BaseResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn

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

    //收集异常信息，并切换到子线程做网络请求
    protected fun <T> Flow<BaseResponse<T>>.flowOnIO(): Flow<BaseResponse<T>> {
        return this.catch { e ->
            Log.e("printInfo", "BaseRepository::flowOnIO: $e")
            //处理异常状态
            emit(BaseResponse.error(e))
        }.flowOn(Dispatchers.Default)
    }
}
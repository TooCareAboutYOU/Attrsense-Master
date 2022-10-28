package com.attrsense.android.baselibrary.base.open.repository

import android.util.Log
import com.attrsense.android.baselibrary.base.open.model.BaseResponse
import com.attrsense.android.baselibrary.base.open.model.ResponseData
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
    protected fun <T : Any> Flow<ResponseData<T>>.flowOnIO(): Flow<ResponseData<T>> {
        return this.catch { e ->
            Log.e("print_logs", "BaseRepository::flowOnIO: $e")
            //处理异常状态
            emit(ResponseData.onFailed(e))
        }.flowOn(Dispatchers.Default)
    }
}
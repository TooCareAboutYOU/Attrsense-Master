package com.attrsense.android.baselibrary.crash

import android.util.Log
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlin.coroutines.CoroutineContext

/**
 * @author zhangshuai@attrsense.com
 * @date 2022/11/4 10:31
 * @description
 */
class GlobalCoroutineExceptionHandler : CoroutineExceptionHandler {
    override val key: CoroutineContext.Key<*>
        get() = CoroutineExceptionHandler

    override fun handleException(context: CoroutineContext, exception: Throwable) {
        Log.e("print_logs", "GlobalCoroutineExceptionHandler::handleException: $exception")
    }
}
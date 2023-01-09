package com.attrsense.android.baselibrary.base.open.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.attrsense.android.baselibrary.BuildConfig
import com.attrsense.android.baselibrary.base.open.model.ResponseData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart

/**
 * @author zhangshuai@attrsense.com
 * @date 2022/11/8 19:35
 * @description ViewModel扩展函数
 */

/**
 * 请求加载弹框
 * @receiver Flow<ResponseData<T>>
 * @param vm ViewModel
 * @return Flow<ResponseData<T>>
 */
fun <T : Any> Flow<ResponseData<T>>.showLoadingAndCatch(
    vm: ViewModel,
    isShowDialog: Boolean = true,
    dialogText: String = ""
): Flow<ResponseData<T>> {
    return this.onStart {
        if (isShowDialog) {
            when (vm) {
                is SkeletonViewModel -> {
                    vm.showLoadingDialog(dialogText)
                }
                is SkeletonAndroidViewModel -> {
                    vm.showLoadingDialog(dialogText)
                }
                else -> {}
            }
        }
    }.catchs().onCompletion {
        if (BuildConfig.DEBUG) {
            Log.e("print_logs", "VMExpand::onCompletion")
        }
        if (isShowDialog) {
            when (vm) {
                is SkeletonViewModel -> {
                    vm.dismissLoadingDialog()
                }
                is SkeletonAndroidViewModel -> {
                    vm.dismissLoadingDialog()
                }
                else -> {}
            }
        }
    }
}

fun <T : Any> Flow<ResponseData<T>>.catchs(): Flow<ResponseData<T>> {
    return this.catch { e ->
        if (BuildConfig.DEBUG) {
            Log.e("print_logs", "VMExpand::catch: $e")
        }
        emit(ResponseData.OnFailed(e))
    }
}

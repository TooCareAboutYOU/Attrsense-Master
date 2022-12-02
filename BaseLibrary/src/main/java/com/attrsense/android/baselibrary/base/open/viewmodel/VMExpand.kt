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
fun <T : Any> Flow<ResponseData<T>>.showLoading(vm: ViewModel): Flow<ResponseData<T>> {
    return this.onStart {
        when (vm) {
            is SkeletonViewModel -> {
                vm.showLoadingDialog()
            }
            is SkeletonAndroidViewModel -> {
                vm.showLoadingDialog()
            }
            else -> {}
        }
    }.catch { e ->
        if (BuildConfig.DEBUG) {
            Log.e("print_logs", "VMExpand::catch: $e")
        }
        when (vm) {
            is SkeletonViewModel -> {
                vm.dismissLoadingDialog()
            }
            is SkeletonAndroidViewModel -> {
                vm.dismissLoadingDialog()
            }
            else -> {}
        }
        emit(ResponseData.OnFailed(e))
    }.onCompletion {
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
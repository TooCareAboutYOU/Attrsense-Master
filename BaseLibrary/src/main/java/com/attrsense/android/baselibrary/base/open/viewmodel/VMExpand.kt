package com.attrsense.android.baselibrary.base.open.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.attrsense.android.baselibrary.base.open.model.ResponseData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart

/**
 * @author zhangshuai
 * @date 2022/11/8 19:35
 * @description ViewModel扩展函数
 */

/**
 * 请求加载弹框
 * @receiver Flow<ResponseData<T>>
 * @param viewModel ViewModel
 * @return Flow<ResponseData<T>>
 */
fun <T : Any> Flow<ResponseData<T>>.showLoading(viewModel: ViewModel): Flow<ResponseData<T>> {
    return this.onStart {
        when (viewModel) {
            is BaseViewModel -> {
                viewModel.showLoadingDialog()
            }
            is BaseAndroidViewModel -> {
                viewModel.showLoadingDialog()
            }
            else -> {}
        }
    }.catch { e ->
        Log.e("print_logs", "showLoading: $e")
        emit(ResponseData.onFailed(e))
        when (viewModel) {
            is BaseViewModel -> {
                viewModel.dismissLoadingDialog()
            }
            is BaseAndroidViewModel -> {
                viewModel.dismissLoadingDialog()
            }
            else -> {}
        }
    }.onCompletion {
        when (viewModel) {
            is BaseViewModel -> {
                viewModel.dismissLoadingDialog()
            }
            is BaseAndroidViewModel -> {
                viewModel.dismissLoadingDialog()
            }
            else -> {}
        }
    }
}
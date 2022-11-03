package com.attrsense.android.baselibrary.base.open.viewmodel

import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.attrsense.android.baselibrary.app.SkeletonApplication
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

/**
 * author : zhangshuai@attrsense.com
 * date : 2022/10/12 10:17
 * mark : custom something
 */
open class BaseAndroidViewModel : AndroidViewModel(SkeletonApplication.getInstance()) {


    private var _loadViewImpl: LoadViewImpl? = null

    fun setLoadView(loadViewImpl: LoadViewImpl?) {
        this._loadViewImpl = loadViewImpl
    }

    protected inline fun <T> Flow<T>.collectInLaunch(
        viewModel: BaseAndroidViewModel? = null,
        isShowLoading: Boolean = true,
        crossinline action: suspend (value: T) -> Unit
    ) = viewModelScope.launch(Dispatchers.Main) {
        collect {

            if (isShowLoading) {
                viewModel?.showLoading()
            }

            action.invoke(it)

            if (isShowLoading) {
                viewModel?.hideLoading()
            }
        }
    }

    fun showLoading() {
        Log.d("print_logs", "BaseAndroidViewModel::collectInLaunch: showLoading")
        this._loadViewImpl?.showLoadingDialog()
    }

    fun hideLoading() {
        Log.d("print_logs", "BaseAndroidViewModel::collectInLaunch: hideLoading")
        this._loadViewImpl?.hideLoadingDialog()
    }

    override fun onCleared() {
        this._loadViewImpl = null
        super.onCleared()
        viewModelScope.cancel()
    }
}
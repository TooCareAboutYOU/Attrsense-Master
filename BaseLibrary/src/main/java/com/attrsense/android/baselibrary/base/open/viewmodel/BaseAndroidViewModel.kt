package com.attrsense.android.baselibrary.base.open.viewmodel

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.attrsense.android.baselibrary.app.SkeletonApplication
import io.reactivex.rxjava3.disposables.Disposable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

/**
 * author : zhangshuai@attrsense.com
 * date : 2022/10/12 10:17
 * mark : custom something
 */
abstract class BaseAndroidViewModel : AndroidViewModel(SkeletonApplication.getInstance()),
    OnViewModelCallback {


    private var mOnViewModelCallback: OnViewModelCallback? = null

    fun setOnViewModelCallback(onViewModelCallback: OnViewModelCallback?) {
        this.mOnViewModelCallback = onViewModelCallback
    }

    protected inline fun <T> Flow<T>.collectInLaunch(
        viewModel: BaseAndroidViewModel? = null,
        isShowLoading: Boolean = true,
        title: String = "null",
        crossinline action: suspend (value: T) -> Unit
    ) = viewModelScope.launch(Dispatchers.Main) {
        collect {
            if (isShowLoading) {
                viewModel?.showLoadingDialog(title)
            }

            action.invoke(it)

            if (isShowLoading) {
                viewModel?.dismissLoadingDialog()
            }
        }
    }


    override fun showLoadingDialog(text: String) {
        this.mOnViewModelCallback?.showLoadingDialog(text)
    }

    override fun dismissLoadingDialog() {
        this.mOnViewModelCallback?.dismissLoadingDialog()
    }

    override fun addDisposable(disposable: Disposable) {
        this.mOnViewModelCallback?.addDisposable(disposable)
    }

    override fun removeDisposable(disposable: Disposable) {
        this.mOnViewModelCallback?.removeDisposable(disposable)
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
        this.mOnViewModelCallback = null
    }
}
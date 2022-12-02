package com.attrsense.android.baselibrary.base.open.viewmodel

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.attrsense.android.baselibrary.app.SkeletonApplication
import io.reactivex.rxjava3.disposables.Disposable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

/**
 * author : zhangshuai@attrsense.com
 * date : 2022/10/12 10:17
 * mark : custom something
 */
abstract class SkeletonAndroidViewModel : AndroidViewModel(SkeletonApplication.getInstance()),
    OnViewModelCallback {


    private var mOnViewModelCallback: OnViewModelCallback? = null

    fun setOnViewModelCallback(onViewModelCallback: OnViewModelCallback?) {
        this.mOnViewModelCallback = onViewModelCallback
    }

    protected inline fun <T> Flow<T>.collectInLaunch(
        crossinline action: suspend (value: T) -> Unit
    ) = viewModelScope.launch(Dispatchers.Main) {
        collect {
            action.invoke(it)
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

    override fun showToast(text: String?, isLong: Boolean) {
        this.mOnViewModelCallback?.showToast(text, isLong)
    }

    override fun onCleared() {
        super.onCleared()
        this.mOnViewModelCallback = null
    }
}
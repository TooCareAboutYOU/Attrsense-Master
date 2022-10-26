package com.attrsense.android.baselibrary.base.open.viewmodel

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.attrsense.android.baselibrary.app.SkeletonApplication
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

/**
 * author : zhangshuai@attrsense.com
 * date : 2022/10/12 10:17
 * mark : custom something
 */
open class BaseAndroidViewModel : AndroidViewModel(SkeletonApplication.getInstance()) {

    protected inline fun <T> Flow<T>.collectInLaunch(crossinline action: suspend (value: T) -> Unit) =
        viewModelScope.launch {
            collect {
                action.invoke(it)
            }
        }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }
}
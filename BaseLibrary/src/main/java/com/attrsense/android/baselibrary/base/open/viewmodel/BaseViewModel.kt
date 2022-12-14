package com.attrsense.android.baselibrary.base.open.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.attrsense.android.baselibrary.base.BaseResponse
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

/**
 * author : zhangshuai@attrsense.com
 * date : 2022/10/12 10:17
 * mark : custom something
 */
open class BaseViewModel : ViewModel() {

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
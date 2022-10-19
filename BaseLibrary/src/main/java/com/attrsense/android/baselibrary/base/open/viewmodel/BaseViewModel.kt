package com.attrsense.android.baselibrary.base.open.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

/**
 * author : zhangshuai@attrsense.com
 * date : 2022/10/12 10:17
 * mark : custom something
 */
open class BaseViewModel : ViewModel() {

    private val map: MutableMap<String, Any?> by lazy { mutableMapOf() }

    protected fun getBody(): MutableMap<String, Any?> {
        map.clear()
        return map
    }

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
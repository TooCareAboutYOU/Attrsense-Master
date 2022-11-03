package com.attrsense.android.ui.launch

import androidx.lifecycle.*
import com.attrsense.android.baselibrary.base.open.viewmodel.BaseViewModel
import com.attrsense.android.api.ApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * author : zhangshuai@attrsense.com
 * date : 2022/10/9 16:51
 * mark : custom something
 */
@HiltViewModel
class LaunchViewModel @Inject constructor(
    private val apiService: ApiService,
) : BaseViewModel() {

}
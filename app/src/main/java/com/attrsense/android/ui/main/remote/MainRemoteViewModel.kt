package com.attrsense.android.ui.main.remote

import com.attrsense.android.baselibrary.base.open.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * author : zhangshuai@attrsense.com
 * date : 2022/10/14 15:17
 * mark : custom something
 */
@HiltViewModel
class MainRemoteViewModel @Inject constructor(private val mainRemoteRepository: MainRemoteRepository) :
    BaseViewModel() {
}
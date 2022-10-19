package com.attrsense.android.ui.main.local

import com.attrsense.android.baselibrary.base.open.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * author : zhangshuai@attrsense.com
 * date : 2022/10/14 15:17
 * mark : custom something
 */
@HiltViewModel
class MainLocalViewModel @Inject constructor(private val mainLocalRepository: MainLocalRepository) :
    BaseViewModel() {
}
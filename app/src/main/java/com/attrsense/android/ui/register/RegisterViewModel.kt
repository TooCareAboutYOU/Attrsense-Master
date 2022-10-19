package com.attrsense.android.ui.register

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.flowWithLifecycle
import com.attrsense.android.baselibrary.base.open.model.BaseResponse
import com.attrsense.android.baselibrary.base.open.model.EmptyBean
import com.attrsense.android.baselibrary.base.open.model.ResponseData
import com.attrsense.android.baselibrary.base.open.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * author : zhangshuai@attrsense.com
 * date : 2022/10/14 15:17
 * mark : custom something
 */
@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerRepository: RegisterRepository
) : BaseViewModel() {
    val registerLivedata: MutableLiveData<ResponseData<BaseResponse<EmptyBean?>>> =
        MutableLiveData()

    fun register(mobile: String, code: String) {
        registerRepository.register(mobile, code).collectInLaunch {
            registerLivedata.value = it
        }
    }
}
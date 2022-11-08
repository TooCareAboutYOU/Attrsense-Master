package com.attrsense.android.ui.register

import com.attrsense.android.baselibrary.base.open.livedata.ResponseMutableLiveData
import com.attrsense.android.baselibrary.base.open.model.EmptyBean
import com.attrsense.android.baselibrary.base.open.viewmodel.BaseViewModel
import com.attrsense.android.baselibrary.base.open.viewmodel.showLoading
import com.attrsense.android.repository.AppRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * author : zhangshuai@attrsense.com
 * date : 2022/10/14 15:17
 * mark : custom something
 */
@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val appRepository: AppRepository
) : BaseViewModel() {

    val registerLivedata = ResponseMutableLiveData<EmptyBean?>()

    fun register(mobile: String, code: String) {
        appRepository.register(mobile, code)
            .showLoading(this)
            .collectInLaunch {
                registerLivedata.value = it
            }
    }
}
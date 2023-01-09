package com.attrsense.android.ui.register

import com.attrsense.android.baselibrary.base.open.livedata.ResponseBaseMutableLiveData
import com.attrsense.android.baselibrary.base.open.model.EmptyBean
import com.attrsense.android.baselibrary.base.open.viewmodel.SkeletonViewModel
import com.attrsense.android.baselibrary.base.open.viewmodel.showLoadingAndCatch
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
) : SkeletonViewModel() {

    val registerLivedata = ResponseBaseMutableLiveData<EmptyBean?>()

    fun register(mobile: String, code: String) {
        appRepository.register(mobile, code)
            .showLoadingAndCatch(this)
            .collectInLaunch {
                registerLivedata.value = it
            }
    }
}
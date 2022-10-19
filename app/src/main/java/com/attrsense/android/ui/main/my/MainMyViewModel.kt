package com.attrsense.android.ui.main.my

import androidx.lifecycle.MutableLiveData
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
class MainMyViewModel @Inject constructor(private val mainMyRepository: MainMyRepository) :
    BaseViewModel() {

    val logoutLivedata: MutableLiveData<ResponseData<BaseResponse<EmptyBean?>>> =
        MutableLiveData()

    fun logout() {
        mainMyRepository.logout().collectInLaunch {
            logoutLivedata.value = it
            when (it) {
                is ResponseData.onFailed -> {

                }
                is ResponseData.onSuccess -> {
                    mainMyRepository.removeUserToken()
                }
            }
        }
    }

}
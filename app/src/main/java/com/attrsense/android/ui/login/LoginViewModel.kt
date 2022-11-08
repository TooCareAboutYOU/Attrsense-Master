package com.attrsense.android.ui.login

import com.attrsense.android.baselibrary.base.open.livedata.ResponseMutableLiveData
import com.attrsense.android.baselibrary.base.open.model.ResponseData
import com.attrsense.android.baselibrary.base.open.viewmodel.BaseViewModel
import com.attrsense.android.baselibrary.base.open.viewmodel.showLoading
import com.attrsense.android.model.LoginBean
import com.attrsense.android.repository.AppRepository
import com.attrsense.database.db.entity.UserEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * author : zhangshuai@attrsense.com
 * date : 2022/10/14 15:17
 * mark : custom something
 */
@HiltViewModel
class LoginViewModel @Inject constructor(
    private val appRepository: AppRepository
) : BaseViewModel() {

    val loginLivedata = ResponseMutableLiveData<LoginBean?>()

    fun login(mobile: String, code: String) {
        appRepository.login(mobile, code).showLoading(this)
            .collectInLaunch {
                when (it) {
                    is ResponseData.onFailed -> {

                    }
                    is ResponseData.onSuccess -> {
                        saveUser(mobile, it.value?.data?.token, it.value?.data?.refresh_token)
                        loginLivedata.value = it
                    }
                }
            }
    }

    private fun saveUser(mobile: String, token: String?, refresh_token: String?) {
        appRepository.addUser(
            UserEntity(
                mobile = mobile,
                token = token,
                refreshToken = refresh_token
            )
        ).collectInLaunch {

        }
        appRepository.userManger.save(
            mobile,
            token,
            refresh_token
        )
    }
}
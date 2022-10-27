package com.attrsense.android.ui.login

import androidx.lifecycle.MutableLiveData
import com.attrsense.android.baselibrary.base.open.model.BaseResponse
import com.attrsense.android.baselibrary.base.open.model.ResponseData
import com.attrsense.android.baselibrary.base.open.viewmodel.BaseViewModel
import com.attrsense.android.model.LoginBean
import com.attrsense.android.manager.UserDataManager
import com.attrsense.database.db.entity.UserEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * author : zhangshuai@attrsense.com
 * date : 2022/10/14 15:17
 * mark : custom something
 */
@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: LoginRepository,
    private val userManger: UserDataManager
) : BaseViewModel() {

    val loginLivedata: MutableLiveData<ResponseData<BaseResponse<LoginBean?>>> = MutableLiveData()

    fun login(mobile: String, code: String) {
        repository.login(mobile, code).collectInLaunch {
            when (it) {
                is ResponseData.onSuccess -> {
                    withContext(Dispatchers.IO) {
                        saveUser(mobile, it.value?.data?.token)
                    }
                    loginLivedata.value = it.apply {
                        userManger.setToken(value?.data?.token, value?.data?.refresh_token)
                    }
                }
                else -> {
                }
            }

            loginLivedata.value = it
        }
    }

    private suspend fun saveUser(mobile: String, token: String?) {
        repository.getUserDao().add(UserEntity(mobile = mobile, token = token))
    }
}
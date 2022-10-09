package com.attrsense.android.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.attrsense.android.baselibrary.base.BaseResponse
import com.attrsense.android.http.ApiService
import com.attrsense.android.model.GitHubBean
import com.attrsense.android.model.LoginBean
import com.orhanobut.logger.Logger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Response
import okhttp3.ResponseBody
import javax.inject.Inject

/**
 * author : zhangshuai@attrsense.com
 * date : 2022/10/9 16:51
 * mark : custom something
 */
@HiltViewModel
class MainViewModel @Inject constructor(private val apiService: ApiService) : ViewModel() {

    val loginLiveData: MutableLiveData<GitHubBean?> = MutableLiveData()

    fun login(mobile: String, code: String) {
        viewModelScope.launch {
//            val result: BaseResponse<LoginBean?> = withContext(Dispatchers.IO) {
//                apiService.login("18874703154", "111111")
//            }
//            loginLiveData.value = result.data

            val result = withContext(Dispatchers.IO) {
                apiService.getUsers()
            }
            loginLiveData.value = result
        }
    }


}
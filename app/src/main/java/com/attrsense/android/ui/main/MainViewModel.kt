package com.attrsense.android.ui.main

import android.util.Log
import androidx.lifecycle.*
import com.attrsense.android.http.ApiService
import com.attrsense.android.model.HPIImageBean
import com.attrsense.android.model.LoginBean
import com.google.gson.Gson
import com.orhanobut.logger.Logger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * author : zhangshuai@attrsense.com
 * date : 2022/10/9 16:51
 * mark : custom something
 */
@HiltViewModel
class MainViewModel @Inject constructor(private val apiService: ApiService) : ViewModel() {

    private val githubLiveData: MutableLiveData<String> = MutableLiveData()
    fun requestImage(format: String) {
        githubLiveData.value = format
    }

    fun github(): LiveData<Result<HPIImageBean?>> = githubLiveData.switchMap { format ->
        liveData {
            val result = try {
                Log.i("printInfo", "发起请求: $format")
                Result.success(apiService.getHPIImage(format, 1, 1))
            } catch (e: Exception) {
                Log.i("printInfo", "请求失败！$e")
                Result.failure(e)
            }
            Logger.json("${result.getOrNull()?.toString()}")
            emit(result)
        }
    }

    val loginLiveData: MutableLiveData<LoginBean?> = MutableLiveData()
    fun login(mobile: String, code: String) {
        viewModelScope.launch {
            loginLiveData.value = withContext(Dispatchers.IO) {
                apiService.login(mobile, code)
            }.data
        }
    }

}
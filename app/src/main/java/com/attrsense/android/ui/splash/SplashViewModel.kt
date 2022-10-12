package com.attrsense.android.ui.splash

import android.util.Log
import androidx.lifecycle.*
import com.attrsense.android.baselibrary.test.Event
import com.attrsense.android.http.ApiService
import com.attrsense.android.test.HPIImageBean
import com.orhanobut.logger.Logger
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * author : zhangshuai@attrsense.com
 * date : 2022/10/9 16:51
 * mark : custom something
 */
@HiltViewModel
class SplashViewModel @Inject constructor(
    private val apiService: ApiService,
    private val event: Event
) : ViewModel() {

    private val githubLiveData: MutableLiveData<String> = MutableLiveData()

    fun load(format: String) {
        event.load()
        githubLiveData.value = format
    }

    fun github(): LiveData<Result<HPIImageBean?>> = githubLiveData.switchMap { format ->
        liveData {
            val result = try {
                Log.i("PrintLog", "发起请求: $format")
                Result.success(apiService.getHPIImage(format, 1, 1))
            } catch (e: Exception) {
                Log.i("PrintLog", "请求失败！$e")
                Result.failure(e)
            }
            Logger.json("${result.getOrNull()?.toString()}")
            emit(result)
        }
    }
}
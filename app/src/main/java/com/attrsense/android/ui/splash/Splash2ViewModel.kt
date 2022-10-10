package com.attrsense.android.ui.splash

import android.app.Application
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.*
import com.attrsense.android.http.ApiService
import com.attrsense.android.model.HPIImageBean
import com.orhanobut.logger.Logger
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

/**
 * author : zhangshuai@attrsense.com
 * date : 2022/10/9 16:51
 * mark : custom something
 */
class Splash2ViewModel constructor(private val context: Context) : ViewModel() {

    private val githubLiveData: MutableLiveData<String> = MutableLiveData()
    fun load(format: String) {
        githubLiveData.value = format
        Toast.makeText(context, "context传入成功！", Toast.LENGTH_SHORT).show()
    }

    fun github(): LiveData<Result<String>> = githubLiveData.switchMap { format ->
        liveData {
            val result = try {
                Log.i("PrintLog", "发起请求: $format")
                Result.success("成功！")
            } catch (e: Exception) {
                Log.i("PrintLog", "请求失败！$e")
                Result.failure(e)
            }
            emit(result)
        }
    }
}
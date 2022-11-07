package com.attrsense.android.ui.statistics

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.attrsense.android.baselibrary.base.open.livedata.ResponseMutableLiveData
import com.attrsense.android.baselibrary.base.open.livedata.ResponseMutableLiveData2
import com.attrsense.android.baselibrary.base.open.model.ResponseData
import com.attrsense.android.baselibrary.base.open.viewmodel.BaseViewModel
import com.attrsense.android.model.UserDataBean
import com.attrsense.android.repository.AppRepository
import com.attrsense.database.db.entity.LocalAnfDataEntity
import com.attrsense.database.repository.DatabaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @author zhangshuai
 * @date 2022/11/7 10:56
 * @description
 */
@HiltViewModel
class StatisticsViewModel @Inject constructor(
    private val databaseRepository: DatabaseRepository,
    private val appRepository: AppRepository
) : BaseViewModel() {

    val remoteLiveData = ResponseMutableLiveData2<UserDataBean>()

    val localLiveData = ResponseMutableLiveData2<LocalAnfDataEntity?>()

    fun getRemoteData() {
        appRepository.getUserInfo().collectInLaunch(this) {
            when (it) {
                is ResponseData.onFailed -> {
                    showToast(it.throwable.toString())
                }
                is ResponseData.onSuccess -> {
                    it.value?.data?.let { bean ->
                        remoteLiveData.value = ResponseData.onSuccess(bean)
                    }
                }
            }
        }
    }

    fun getLocalData() {
        databaseRepository.getLocalData(appRepository.userManger.getMobile()).collectInLaunch {
            when (it) {
                is ResponseData.onFailed -> {
                    Log.e("print_logs", "StatisticsViewModel::getLocalData: ${it.throwable}")
                }
                is ResponseData.onSuccess -> {
                    it.value?.let { entity ->
                        localLiveData.value = ResponseData.onSuccess(entity)
                    }
                }
            }
        }
    }

}
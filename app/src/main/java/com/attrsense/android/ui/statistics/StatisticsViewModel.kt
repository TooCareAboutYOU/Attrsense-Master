package com.attrsense.android.ui.statistics

import android.util.Log
import com.attrsense.android.baselibrary.base.open.livedata.ResponseMutableLiveData
import com.attrsense.android.baselibrary.base.open.model.ResponseData
import com.attrsense.android.baselibrary.base.open.viewmodel.SkeletonViewModel
import com.attrsense.android.baselibrary.base.open.viewmodel.showLoading
import com.attrsense.android.model.UserDataBean
import com.attrsense.android.repository.AppRepository
import com.attrsense.database.db.entity.LocalAnfDataEntity
import com.attrsense.database.repository.DatabaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * @author zhangshuai@attrsense.com
 * @date 2022/11/7 10:56
 * @description
 */
@HiltViewModel
class StatisticsViewModel @Inject constructor(
    private val databaseRepository: DatabaseRepository,
    private val appRepository: AppRepository
) : SkeletonViewModel() {

    val remoteLiveData = ResponseMutableLiveData<UserDataBean>()

    val localLiveData = ResponseMutableLiveData<LocalAnfDataEntity?>()

    fun getRemoteData() {
        appRepository.getUserInfo().showLoading(this).collectInLaunch {
            when (it) {
                is ResponseData.OnFailed -> {
                    showToast(it.throwable.toString())
                }
                is ResponseData.OnSuccess -> {
                    it.value?.data?.let { bean ->
                        remoteLiveData.value = ResponseData.OnSuccess(bean)
                    }
                }
            }
        }
    }

    fun getLocalData() {
        databaseRepository.getLocalData(appRepository.userManger.getMobile()).collectInLaunch {
            when (it) {
                is ResponseData.OnFailed -> {
                    Log.e("print_logs", "StatisticsViewModel::getLocalData: ${it.throwable}")
                }
                is ResponseData.OnSuccess -> {
                    it.value?.let { entity ->
                        localLiveData.value = ResponseData.OnSuccess(entity)
                    }
                }
            }
        }
    }

}
package com.attrsense.android.ui.main.my

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.attrsense.android.baselibrary.base.open.model.BaseResponse
import com.attrsense.android.baselibrary.base.open.model.EmptyBean
import com.attrsense.android.baselibrary.base.open.model.ResponseData
import com.attrsense.android.baselibrary.base.open.viewmodel.BaseViewModel
import com.attrsense.android.baselibrary.util.MMKVUtils
import com.attrsense.android.manager.UserDataManager
import com.attrsense.android.repository.AppRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * author : zhangshuai@attrsense.com
 * date : 2022/10/14 15:17
 * mark : custom something
 */
@HiltViewModel
class MainMyViewModel @Inject constructor(
    private val appRepository: AppRepository,
    private val userDataManager: UserDataManager
) :
    BaseViewModel() {

    val logoutLivedata: MutableLiveData<ResponseData<BaseResponse<EmptyBean?>>> =
        MutableLiveData()

    fun logout() {
        //网络接口退出
        appRepository.logout().collectInLaunch {
            it.apply {
                when (it) {
                    is ResponseData.onFailed -> {

                    }
                    is ResponseData.onSuccess -> {
                        //清空对应数据
                        appRepository.databaseRepository.clearByToken(appRepository.userManger.getMobile())
                            .collectInLaunch { state ->
                                when (state) {
                                    is ResponseData.onFailed -> {

                                    }
                                    is ResponseData.onSuccess -> {
                                        //删除用户表对应用户
                                        appRepository.getUserDao().deleteByMobile(userDataManager.getMobile())
                                        //最后清空临时存储
                                        appRepository.userManger.save()
                                        logoutLivedata.value = this
                                    }
                                }
                            }
                    }
                }
            }
        }
    }

}
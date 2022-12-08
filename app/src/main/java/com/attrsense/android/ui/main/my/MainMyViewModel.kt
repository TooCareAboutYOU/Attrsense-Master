package com.attrsense.android.ui.main.my

import android.os.Environment
import com.attrsense.android.BuildConfig
import com.attrsense.android.baselibrary.base.open.livedata.ResponseBaseMutableLiveData
import com.attrsense.android.baselibrary.base.open.model.BaseResponse
import com.attrsense.android.baselibrary.base.open.model.EmptyBean
import com.attrsense.android.baselibrary.base.open.model.ResponseData
import com.attrsense.android.baselibrary.base.open.viewmodel.SkeletonViewModel
import com.attrsense.android.baselibrary.base.open.viewmodel.showLoading
import com.attrsense.android.repository.AppRepository
import com.attrsense.database.repository.DatabaseRepository
import com.blankj.utilcode.util.FileUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import java.io.File
import javax.inject.Inject

/**
 * author : zhangshuai@attrsense.com
 * date : 2022/10/14 15:17
 * mark : custom something
 */
@HiltViewModel
class MainMyViewModel @Inject constructor(
    private val databaseRepository: DatabaseRepository,
    private val appRepository: AppRepository
) :
    SkeletonViewModel() {

    val logoutLivedata = ResponseBaseMutableLiveData<EmptyBean?>()

    fun logout() {
        //网络接口退出
        appRepository.logout().showLoading(this).collectInLaunch {
            it.apply {
                when (it) {
                    is ResponseData.OnFailed -> {
                        showToast(it.throwable.message)
                    }
                    is ResponseData.OnSuccess -> {
                        clearUserByToken(it)
                    }
                }
            }
        }
    }

    //清空对应数据库
    private fun clearUserByToken(callback: ResponseData<BaseResponse<EmptyBean?>>) {
        databaseRepository.clearByMobile(appRepository.userManger.getMobile())
            .collectInLaunch { state ->
                when (state) {
                    is ResponseData.OnFailed -> {

                    }
                    is ResponseData.OnSuccess -> {
                        //删除用户表对应用户
                        databaseRepository.getUserDao()
                            .deleteByMobile(appRepository.userManger.getMobile())
                        //最后清空临时存储
                        appRepository.userManger.unSave()

                        if (BuildConfig.DEBUG) {
                            appRepository.reset()
                            databaseRepository.reset()
                            FileUtils.deleteAllInDir(Environment.getExternalStoragePublicDirectory("Attrsense").absolutePath + File.separator)
                        }

                        logoutLivedata.value = callback
                    }
                }
            }
    }

}
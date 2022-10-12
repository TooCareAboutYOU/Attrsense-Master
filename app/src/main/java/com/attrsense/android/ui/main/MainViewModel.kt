package com.attrsense.android.ui.main

import androidx.lifecycle.*
import com.attrsense.android.base.AppConfig
import com.attrsense.android.baselibrary.base.BaseResponse
import com.attrsense.android.baselibrary.base.open.viewmodel.BaseViewModel
import com.attrsense.android.baselibrary.util.MMKVUtils
import com.attrsense.android.model.ImageInfoBean
import com.attrsense.android.model.ImagesBean
import com.attrsense.android.model.LoginBean
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * author : zhangshuai@attrsense.com
 * date : 2022/10/9 16:51
 * mark : custom something
 */
@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: MainRepository,
    private val mmkv: MMKVUtils
) : BaseViewModel() {

    private val token: String? by lazy { mmkv.getString(AppConfig.KEY_ACCOUNT_TOKEN) }

    /**
     * 登录
     */
    private val _loginLiveData: MutableLiveData<BaseResponse<LoginBean?>> = MutableLiveData()
    val loginLiveData: LiveData<BaseResponse<LoginBean?>>
        get() = _loginLiveData

    fun login(mobile: String?, code: String?) {
        repository.login(mobile, code).collectInLaunch {
            _loginLiveData.value = it.apply {
                mmkv.setValue(AppConfig.KEY_ACCOUNT_TOKEN, this.data?.token)
                mmkv.setValue(AppConfig.KEY_ACCOUNT_REFRESH_TOKEN, this.data?.refresh_token)
            }
        }
    }


    /**
     * 刷新token
     */
    private val _refreshLiveData: MutableLiveData<BaseResponse<LoginBean?>> = MutableLiveData()
    val refreshLiveData: LiveData<BaseResponse<LoginBean?>>
        get() = _refreshLiveData

    fun refreshToken() {
        val refreshToken = mmkv.getString(AppConfig.KEY_ACCOUNT_REFRESH_TOKEN)
        repository.refreshToken(refreshToken).collectInLaunch {
            _refreshLiveData.value = it
        }
    }

    /**
     * 文件上传
     */
    private val _uploadFileLiveData: MutableLiveData<BaseResponse<ImagesBean?>> =
        MutableLiveData()
    val uploadFileLiveData: LiveData<BaseResponse<ImagesBean?>>
        get() = _uploadFileLiveData

    fun uploadFile(rate: String?, roiRate: String?, imageFilePath: String) {
        repository.uploadFile(token, rate, roiRate, imageFilePath).collectInLaunch {
            _uploadFileLiveData.value = it
        }
    }


    /**
     * 查询上传的文件
     */
    private val _queryUploadFileLiveData: MutableLiveData<BaseResponse<ImagesBean?>> =
        MutableLiveData()
    val queryUploadFileLiveData: LiveData<BaseResponse<ImagesBean?>>
        get() = _queryUploadFileLiveData

    fun queryUploadFile(page: Int?, perPage: Int?) {
        repository.getUploadFile(token, page, perPage).collectInLaunch {
            _queryUploadFileLiveData.value = it
        }
    }


    /**
     * 删除文件
     */
    private val _deleteFileLiveData: MutableLiveData<BaseResponse<Any?>> =
        MutableLiveData()
    val deleteFileLiveData: LiveData<BaseResponse<Any?>>
        get() = _deleteFileLiveData

    fun deleteFile(fileId: String) {
        repository.deleteFile(token, fileId).collectInLaunch {
            _deleteFileLiveData.value = it
        }
    }

}
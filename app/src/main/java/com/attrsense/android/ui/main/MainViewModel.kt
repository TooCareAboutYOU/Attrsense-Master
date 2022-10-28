package com.attrsense.android.ui.main

import com.attrsense.android.baselibrary.base.open.viewmodel.BaseViewModel
import com.attrsense.android.baselibrary.util.MMKVUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * author : zhangshuai@attrsense.com
 * date : 2022/10/9 16:51
 * mark : custom something
 */
@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: MainRepository,
    private val mmkv: MMKVUtils,
) : BaseViewModel() {
//    /**
//     * 登录
//     */
//    private val _loginLiveData: MutableLiveData<BaseResponse<LoginBean?>> = MutableLiveData()
//    val loginLiveData: LiveData<BaseResponse<LoginBean?>>
//        get() = _loginLiveData
//
//    fun login(mobile: String?, code: String?) {
//        repository.login(mobile, code).collectInLaunch {
//            Log.e("print_logs", "MainViewModel::login: $it")
//            _loginLiveData.value = it.apply {
//                if (it.errorCode != BaseResponse.CODE_LOGIN_INVALID) {
////                    mmkv.setValue(AppConfig.KEY_ACCOUNT_TOKEN, data?.token)
////                    mmkv.setValue(AppConfig.KEY_ACCOUNT_REFRESH_TOKEN, data?.refresh_token)
////
////                    withContext(Dispatchers.IO) {
////                        saveUser(mobile, data?.token)
////                    }
//                }
//            }
//        }
//    }
//
//
//
//
//    /**
//     * 刷新token
//     */
//    private val _refreshLiveData: MutableLiveData<BaseResponse<LoginBean?>> = MutableLiveData()
//    val refreshLiveData: LiveData<BaseResponse<LoginBean?>>
//        get() = _refreshLiveData
//
//    fun refreshToken() {
//        val refreshToken = mmkv.getString(AppConfig.KEY_ACCOUNT_REFRESH_TOKEN)
//        repository.refreshToken(refreshToken).collectInLaunch {
//            _refreshLiveData.value = it
//        }
//    }
//
//    /**
//     * 文件上传
//     */
//    private val _uploadFileLiveData: MutableLiveData<BaseResponse<ImagesBean?>> =
//        MutableLiveData()
//    val uploadFileLiveData: LiveData<BaseResponse<ImagesBean?>>
//        get() = _uploadFileLiveData
//
//    fun uploadFile(rate: String?, roiRate: String?, imageFilePath: List<String>) {
//        repository.uploadFile(localToken, rate, roiRate, imageFilePath).collectInLaunch {
//            _uploadFileLiveData.value = it
//        }
//    }
//
//
//    /**
//     * 查询上传的文件
//     */
//    private val _queryUploadFileLiveData: MutableLiveData<BaseResponse<ImagesBean?>> =
//        MutableLiveData()
//    val queryUploadFileLiveData: LiveData<BaseResponse<ImagesBean?>>
//        get() = _queryUploadFileLiveData
//
//    fun queryUploadFile(page: Int?, perPage: Int?) {
//        repository.getUploadFile(localToken, page, perPage).collectInLaunch {
//            _queryUploadFileLiveData.value = it
//        }
//    }
//
//
//    /**
//     * 删除文件
//     */
//    private val _deleteFileLiveData: MutableLiveData<BaseResponse<EmptyBean?>> =
//        MutableLiveData()
//    val deleteFileLiveData: LiveData<BaseResponse<EmptyBean?>>
//        get() = _deleteFileLiveData
//
//    fun deleteFile(fileId: String) {
//        repository.deleteFile(localToken, fileId).collectInLaunch {
//            _deleteFileLiveData.value = it
//        }
//    }

}
package com.attrsense.android.ui.main

import com.attrsense.android.base.BaseRepository
import com.attrsense.android.api.ApiService
import com.attrsense.database.repository.DatabaseRepository
import javax.inject.Inject

/**
 * author : zhangshuai@attrsense.com
 * date : 2022/10/9 16:51
 * mark : custom something
 */
class MainRepository @Inject constructor(
    private val apiService: ApiService,
    private val databaseRepository: DatabaseRepository
) : BaseRepository() {

//    /**
//     * 刷新token
//     */
//    fun refreshToken(refreshToken: String?) = flow {
//        emit(apiService.refreshToken(refreshToken))
//    }.flowOnIO()
//
//
//    /**
//     * 文件上传
//     * @param token 身份标识
//     * @param rate 压缩参数1
//     * @param roiRate 压缩参数2
//     * @param imageFilePath 图片文件
//     */
//    fun uploadFile(token: String?, rate: String?, roiRate: String?, imageFilePaths: List<String>?=null) = flow {
//        if (!databaseRepository.isLoginByToken(token)) {
//            Log.i("print_logs", "MainRepository::login: 验证登录状态->")
//            emit(BaseResponse.loginInvalid(BaseResponse.CODE_LOGIN_INVALID, "登录失败！"))
//        } else {
//            emit(apiService.uploadFile(token, rate?.toRequestBody(), roiRate?.toRequestBody(), imageFilePaths?.toMultipartBody()))
//        }
//    }.flowOnIO()
//
//
//    /**
//     * 查询上传的文件
//     * @param token 身份标识
//     * @param page 查询的页号
//     * @param perPage 每页数目
//     */
//    fun getUploadFile(token: String?, page: Int?, perPage: Int?) = flow {
//        val body = getBody().apply {
//            this["page"] = page
//            this["perPage"] = perPage
//        }
//        emit(apiService.queryUploadFile(token, body))
//    }.flowOnIO()
//
//
//    /**
//     * 删除文件
//     * @param token 身份标识
//     * @param fileId
//     */
//    fun deleteFile(token: String?, fileId: String) = flow {
//        val body = getBody().apply {
//            this["fileId"] = fileId
//        }
//        emit(apiService.deleteFile(token, body))
//    }.flowOnIO()
//
//    /**
//     * 体验反馈
//     */
//    fun feedback(feedback: String, imageFilePaths: List<String>? = null) = flow {
//        emit(apiService.feedback(feedback.toRequestBody(), imageFilePaths?.toMultipartBody()))
//    }.flowOnIO()
}
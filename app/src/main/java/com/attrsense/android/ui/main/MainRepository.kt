package com.attrsense.android.ui.main

import android.util.Log
import com.attrsense.android.baselibrary.base.open.repository.BaseRepository
import com.attrsense.android.http.ApiService
import com.attrsense.database.db.AttrSenseRoomDatabase
import com.attrsense.database.db.dao.UserDao
import com.attrsense.database.db.entity.UserEntity
import kotlinx.coroutines.flow.flow
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

/**
 * author : zhangshuai@attrsense.com
 * date : 2022/10/9 16:51
 * mark : custom something
 */
class MainRepository @Inject constructor(
    private val apiService: ApiService,
    private val userDao: UserDao
) :
    BaseRepository() {

    fun getUserDao(): UserDao = userDao

    /**
     * 登录获取 token
     */
    fun login(mobile: String?, code: String?) = flow {
        val body = getBody().apply {
            this["mobile"] = mobile
            this["code"] = code
        }
        emit(apiService.login(body))
    }.flowOnIO()


    /**
     * 刷新token
     */
    fun refreshToken(refreshToken: String?) = flow {
        emit(apiService.refreshToken(refreshToken))
    }.flowOnIO()


    /**
     * 文件上传
     * @param token 身份标识
     * @param rate 压缩参数1
     * @param roiRate 压缩参数2
     * @param imageFilePath 图片文件
     */
    fun uploadFile(token: String?, rate: String?, roiRate: String?, imageFilePath: String) = flow {
        val mediaType = "text/plain".toMediaType()
        val rateBody = rate?.toRequestBody(mediaType)
        val roiRateBody = roiRate?.toRequestBody(mediaType)

        val file = java.io.File(imageFilePath)
        val imageFileBody = file.asRequestBody("image/jpg".toMediaType())
        val filePart = MultipartBody.Part.createFormData("imageFile", file.name, imageFileBody)

        Log.i(
            "printInfo",
            "MainRepository::uploadFile: $token,\n$rate, \n$roiRate, \n$imageFilePath"
        )
        emit(apiService.uploadFile(token, rateBody, roiRateBody, filePart))
    }.flowOnIO()


    /**
     * 查询上传的文件
     * @param token 身份标识
     * @param page 查询的页号
     * @param perPage 每页数目
     */
    fun getUploadFile(token: String?, page: Int?, perPage: Int?) = flow {
        val body = getBody().apply {
            this["page"] = page
            this["perPage"] = perPage
        }
        emit(apiService.queryUploadFile(token, body))
    }.flowOnIO()


    /**
     * 删除文件
     * @param token 身份标识
     * @param fileId
     */
    fun deleteFile(token: String?, fileId: String) = flow {
        val body = getBody().apply {
            this["fileId"] = fileId
        }
        emit(apiService.deleteFile(token, body))
    }.flowOnIO()
}
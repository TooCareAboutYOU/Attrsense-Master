package com.attrsense.android.ui.main.remote

import com.attrsense.android.base.BaseRepository
import com.attrsense.android.baselibrary.base.open.model.ResponseData
import com.attrsense.android.http.ApiService
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * author : zhangshuai@attrsense.com
 * date : 2022/10/18 16:34
 * mark : custom something
 */
class MainRemoteRepository @Inject constructor(private val apiService: ApiService) :
    BaseRepository() {

    /**
     * 文件上传
     * @param token 身份标识
     * @param rate 压缩参数1
     * @param roiRate 压缩参数2
     * @param imageFilePath 图片文件
     */
    fun uploadFile(
        token: String?,
        rate: String?,
        roiRate: String?,
        imageFilePaths: List<String>? = null
    ) = flow {
        emit(
            ResponseData.onSuccess(
                apiService.uploadFile(
                    token,
                    rate?.toRequestBody(),
                    roiRate?.toRequestBody(),
                    imageFilePaths?.toMultipartBody()
                )
            )
        )
    }.flowOnIO()
}
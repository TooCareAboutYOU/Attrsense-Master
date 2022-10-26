package com.attrsense.android.ui.main.remote

import com.attrsense.android.base.BaseRepository
import com.attrsense.android.baselibrary.base.open.model.ResponseData
import com.attrsense.android.api.ApiService
import com.attrsense.android.util.UserDataManager
import com.attrsense.database.DatabaseRepository
import com.attrsense.database.db.entity.AnfImageEntity
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * author : zhangshuai@attrsense.com
 * date : 2022/10/18 16:34
 * mark : custom something
 */
class MainRemoteRepository @Inject constructor(
    private val apiService: ApiService,
    private val databaseRepository: DatabaseRepository,
    val userManger: UserDataManager
) :
    BaseRepository() {

    /**
     * 添加集合
     */
    fun addEntities(entityList: List<AnfImageEntity>) = flow {
        databaseRepository.getAnfDao().addList(entityList)
        emit(ResponseData.onSuccess(true))
    }.flowOnIO()

    /**
     * 清空所有的
     */
    fun cleatDatabase(isLocal: Boolean? = false) = flow {
        databaseRepository.getAnfDao().clearRemote(userManger.getToken(), isLocal)
        emit(ResponseData.onSuccess(true))
    }.flowOnIO()

    /**
     * 文件上传
     * @param rate 压缩参数1
     * @param roiRate 压缩参数2
     * @param imageFilePath 图片文件
     */
    fun getRemoteFiles(
        page: Int,
        perPage: Int
    ) = flow {
        val body = getBody().apply {
            this["page"] = page
            this["perPage"] = perPage
        }
        emit(ResponseData.onSuccess(apiService.getUploadFiles(userManger.getToken(), body)))
    }.flowOnIO()

    /**
     * 文件上传
     * @param token 身份标识
     * @param rate 压缩参数1
     * @param roiRate 压缩参数2
     * @param imageFilePath 图片文件
     */
    fun uploadFile(
        rate: String?,
        roiRate: String?,
        imageFilePaths: List<String>? = null
    ) = flow {
        emit(
            ResponseData.onSuccess(
                apiService.uploadFile(
                    userManger.getToken(),
                    rate?.toRequestBody(),
                    roiRate?.toRequestBody(),
                    imageFilePaths?.toMultipartBody()
                )
            )
        )
    }.flowOnIO()

    /**
     * 删除
     */
    fun deleteByAnfPath(anfImage: String?) = flow {
        emit(ResponseData.onSuccess(databaseRepository.getAnfDao().deleteByAnfPath(anfImage)))
    }.flowOnIO()

}
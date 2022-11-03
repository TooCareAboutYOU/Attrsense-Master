package com.attrsense.android.repository

import com.attrsense.android.api.ApiService
import com.attrsense.android.base.BaseRepository
import com.attrsense.android.baselibrary.base.open.model.BaseResponse
import com.attrsense.android.baselibrary.base.open.model.EmptyBean
import com.attrsense.android.baselibrary.base.open.model.ResponseData
import com.attrsense.android.manager.UserDataManager
import com.attrsense.android.model.LoginBean
import com.attrsense.database.db.dao.UserDao
import com.attrsense.database.repository.DatabaseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * 数据库操作管理类
 */
class AppRepository @Inject constructor(
    val databaseRepository: DatabaseRepository,
    private val apiService: ApiService,
    val userManger: UserDataManager
) : BaseRepository() {

    fun getUserDao(): UserDao = databaseRepository.getUserDao()


    /**
     * 登录获取 token
     * BaseResponse<EmptyBean?>
     */
    fun register(mobile: String, code: String): Flow<ResponseData<BaseResponse<EmptyBean?>>> =
        flow {
            val body = getBody().apply {
                this["mobile"] = mobile
                this["code"] = code
            }
            emit(ResponseData.onSuccess(apiService.register(body)))
        }.flowOnIO()

    /**
     * 登录获取
     * @param mobile String
     * @param code String
     * @return Flow<ResponseData<BaseResponse<LoginBean?>>>
     */
    fun login(mobile: String, code: String): Flow<ResponseData<BaseResponse<LoginBean?>>> = flow {
        val body = getBody().apply {
            this["mobile"] = mobile
            this["code"] = code
        }
        emit(ResponseData.onSuccess(apiService.login(body)))
    }.flowOnIO()

    /**
     * 退出登录
     * @return Flow<ResponseData<BaseResponse<EmptyBean?>>>
     */
    fun logout(): Flow<ResponseData<BaseResponse<EmptyBean?>>> = flow {
        emit(ResponseData.onSuccess(apiService.logout(userManger.getToken())))
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
     * @param rate 压缩参数1
     * @param roiRate 压缩参数2
     * @param imageFilePath 图片文件
     */
    fun uploadFile(
        rate: String?,
        roiRate: String?,
        imageFilePaths: List<String> = emptyList()
    ) = flow {
        emit(
            ResponseData.onSuccess(
                apiService.uploadFile(
                    userManger.getToken(),
                    rate?.toRequestBody(),
                    roiRate?.toRequestBody(),
                    imageFilePaths.toMultipartBody()
                )
            )
        )
    }.flowOnIO()

    fun deleteFile(fileId: String?) = flow {
        val body = getBody().apply {
            this["fileId"] = fileId
        }
        emit(ResponseData.onSuccess(apiService.deleteFile(userManger.getToken(), body)))
    }.flowOnIO()


    /**
     * 意见反馈
     * @param description String
     * @param pictures List<String?>?
     * @return Flow<ResponseData<BaseResponse<EmptyBean?>>>
     */
    fun feedback(description: String, pictures: List<String?> = emptyList<String>()) =
        flow {
            emit(
                ResponseData.onSuccess(
                    apiService.feedback(
                        userManger.getToken(),
                        description.toRequestBody(),
                        pictures.toMultipartBody()
                    )
                )
            )
        }.flowOnIO()


    /**
     * 申请内测
     * @param name String
     * @param mobile String
     * @param company String
     * @param email String
     * @param briefly String?
     * @return Flow<ResponseData<BaseResponse<EmptyBean?>>>
     */
    fun apply(name: String, mobile: String, company: String, email: String, briefly: String?) =
        flow {
            val body = getBody().apply {
                this["name"] = name
                this["mobile"] = mobile
                this["company"] = company
                this["email"] = email
                this["briefly"] = briefly
            }
            emit(ResponseData.onSuccess(apiService.applyTest(userManger.getToken(), body)))
        }.flowOnIO()

}



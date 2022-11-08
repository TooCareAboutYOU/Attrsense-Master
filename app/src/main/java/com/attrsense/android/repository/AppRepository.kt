package com.attrsense.android.repository

import com.attrsense.android.api.ApiService
import com.attrsense.android.base.BaseRepository
import com.attrsense.android.manager.UserDataManager
import com.attrsense.database.db.dao.UserDao
import com.attrsense.database.db.entity.UserEntity
import com.attrsense.database.repository.DatabaseRepository
import javax.inject.Inject

/**
 * 数据库操作管理类
 */
class AppRepository @Inject constructor(
    private val databaseRepository: DatabaseRepository,
    private val apiService: ApiService,
    val userManger: UserDataManager
) : BaseRepository() {

    private fun getUserDao(): UserDao = databaseRepository.getUserDao()

    /**
     * 登录获取 token
     * BaseResponse<EmptyBean?>
     */
    fun register(mobile: String, code: String) = request {
        val body = getBody().apply {
            this["mobile"] = mobile
            this["code"] = code
        }
        apiService.register(body)
    }

    /**
     * 登录获取
     * @param mobile String
     * @param code String
     * @return Flow<ResponseData<BaseResponse<LoginBean?>>>
     */
    fun login(mobile: String, code: String) = request {
        val body = getBody().apply {
            this["mobile"] = mobile
            this["code"] = code
        }
        apiService.login(body)
    }


    /**
     * 添加用户到数据库
     * @param userEntity UserEntity
     * @return Flow<ResponseData<Boolean>
     */
    fun addUser(userEntity: UserEntity) = request {
        getUserDao().addUser(userEntity)
        true
    }

    /**
     * 退出登录
     * @return Flow<ResponseData<BaseResponse<EmptyBean?>>>
     */
    fun logout() = request {
        apiService.logout(userManger.getToken())
    }


    /**
     * 获取用户编解码数据信息
     * @return Flow<ResponseData<BaseResponse<UserDataBean?>>>
     */
    fun getUserInfo() = request {
        apiService.getUserInfo(userManger.getToken())
    }


    /**
     * 文件上传
     * @param rate 压缩参数1
     * @param roiRate 压缩参数2
     * @param imageFilePath 图片文件
     */
    fun getRemoteFiles(
        page: Int,
        perPage: Int
    ) = request {
        val body = getBody().apply {
            this["page"] = page
            this["perPage"] = perPage
        }
        apiService.getUploadFiles(userManger.getToken(), body)
    }

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
    ) = request {
        apiService.uploadFile(
            userManger.getToken(),
            rate?.toRequestBody(),
            roiRate?.toRequestBody(),
            imageFilePaths.toMultipartBody()
        )
    }

    fun deleteFile(fileId: String?) = request {
        val body = getBody().apply {
            this["fileId"] = fileId
        }
        apiService.deleteFile(userManger.getToken(), body)
    }


    /**
     * 意见反馈
     * @param description String
     * @param pictures List<String?>?
     * @return Flow<ResponseData<BaseResponse<EmptyBean?>>>
     */
    fun feedback(description: String, pictures: List<String?> = emptyList<String>()) = request {
        apiService.feedback(
            userManger.getToken(),
            description.toRequestBody(),
            pictures.toMultipartBody()
        )
    }


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
        request {
            val body = getBody().apply {
                this["name"] = name
                this["mobile"] = mobile
                this["company"] = company
                this["email"] = email
                this["briefly"] = briefly
            }
            apiService.applyTest(userManger.getToken(), body)
        }


    fun reset() = request {
        getUserDao().clear()
        getUserDao().reset()
        true
    }

}



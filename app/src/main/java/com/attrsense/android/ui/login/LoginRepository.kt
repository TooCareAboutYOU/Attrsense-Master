package com.attrsense.android.ui.login

import com.attrsense.android.base.BaseRepository
import com.attrsense.android.baselibrary.base.open.model.BaseResponse
import com.attrsense.android.baselibrary.base.open.model.ResponseData
import com.attrsense.android.api.ApiService
import com.attrsense.android.model.LoginBean
import com.attrsense.database.db.dao.UserDao
import com.attrsense.android.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * author : zhangshuai@attrsense.com
 * date : 2022/10/9 16:51
 * mark : custom something
 */
class LoginRepository @Inject constructor(
    private val apiService: ApiService,
    private val userRepository: UserRepository
) : BaseRepository() {

    fun getUserDao(): UserDao = userRepository.getUserDao()

    /**
     * 登录获取 token
     */
    fun login(mobile: String, code: String): Flow<ResponseData<BaseResponse<LoginBean?>>> = flow {
        val body = getBody().apply {
            this["mobile"] = mobile
            this["code"] = code
        }
        emit(ResponseData.onSuccess(apiService.login(body)))
    }.flowOnIO()
}
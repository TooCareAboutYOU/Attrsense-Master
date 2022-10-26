package com.attrsense.android.ui.main.my

import com.attrsense.android.base.BaseRepository
import com.attrsense.android.baselibrary.base.open.model.BaseResponse
import com.attrsense.android.baselibrary.base.open.model.EmptyBean
import com.attrsense.android.baselibrary.base.open.model.ResponseData
import com.attrsense.android.api.ApiService
import com.attrsense.android.util.UserDataManager
import com.attrsense.database.DatabaseRepository
import com.attrsense.database.db.dao.UserDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * author : zhangshuai@attrsense.com
 * date : 2022/10/18 16:34
 * mark : custom something
 */
class MainMyRepository @Inject constructor(
    private val apiService: ApiService,
    private val databaseRepository: DatabaseRepository,
    private val userManger: UserDataManager
) : BaseRepository() {

    private val token: String? by lazy { userManger.getToken() }

    private val userDao: UserDao = databaseRepository.getUserDao()

    suspend fun removeUserToken() {
        userDao.queryByToken(token)?.apply {
            if (this.isNotEmpty()) {
                val new = this[0].apply {
                    token = ""
                }
                userDao.update(new)
                userManger.setToken()
            }
        }
    }

    fun logout(): Flow<ResponseData<BaseResponse<EmptyBean?>>> = flow {
        emit(ResponseData.onSuccess(apiService.logout(token)))
    }.flowOnIO()

}
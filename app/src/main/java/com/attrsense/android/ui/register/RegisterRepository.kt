package com.attrsense.android.ui.register

import android.util.Log
import androidx.lifecycle.flowWithLifecycle
import com.attrsense.android.base.BaseRepository
import com.attrsense.android.baselibrary.base.open.model.BaseResponse
import com.attrsense.android.baselibrary.base.open.model.EmptyBean
import com.attrsense.android.baselibrary.base.open.model.ResponseData
import com.attrsense.android.http.ApiService
import com.attrsense.android.model.LoginBean
import com.attrsense.database.DatabaseRepository
import com.attrsense.database.db.dao.UserDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

/**
 * author : zhangshuai@attrsense.com
 * date : 2022/10/9 16:51
 * mark : custom something
 */
class RegisterRepository @Inject constructor(
    private val apiService: ApiService
) : BaseRepository() {
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
}
package com.attrsense.android.ui.register

import com.attrsense.android.base.BaseRepository
import com.attrsense.android.baselibrary.base.open.model.BaseResponse
import com.attrsense.android.baselibrary.base.open.model.EmptyBean
import com.attrsense.android.baselibrary.base.open.model.ResponseData
import com.attrsense.android.api.ApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
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
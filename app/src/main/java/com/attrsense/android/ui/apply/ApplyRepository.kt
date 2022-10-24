package com.attrsense.android.ui.apply

import com.attrsense.android.base.BaseRepository
import com.attrsense.android.api.ApiService
import com.attrsense.android.baselibrary.base.open.model.ResponseData
import com.attrsense.android.util.UserManger
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * author : zhangshuai@attrsense.com
 * date : 2022/10/24 18:16
 * mark : custom something
 */
class ApplyRepository @Inject constructor(
    private val apiService: ApiService,
    private val userManger: UserManger
) : BaseRepository() {

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
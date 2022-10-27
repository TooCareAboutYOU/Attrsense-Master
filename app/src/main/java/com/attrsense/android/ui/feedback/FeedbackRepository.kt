package com.attrsense.android.ui.feedback

import com.attrsense.android.base.BaseRepository
import com.attrsense.android.api.ApiService
import com.attrsense.android.baselibrary.base.open.model.ResponseData
import com.attrsense.android.manager.UserDataManager
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * author : zhangshuai@attrsense.com
 * date : 2022/10/24 18:16
 * mark : custom something
 */
class FeedbackRepository @Inject constructor(
    private val apiService: ApiService,
    private val userManger: UserDataManager
) : BaseRepository() {

    fun feedback(description: String, pictures: List<String?>? = arrayListOf()) =
        flow {
            emit(
                ResponseData.onSuccess(
                    apiService.feedback(
                        userManger.getToken(),
                        description.toRequestBody(),
                        pictures?.toMultipartBody()
                    )
                )
            )
        }.flowOnIO()

}
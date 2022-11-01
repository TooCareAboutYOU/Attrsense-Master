package com.attrsense.android.ui.feedback

import androidx.lifecycle.MutableLiveData
import com.attrsense.android.baselibrary.base.open.model.BaseResponse
import com.attrsense.android.baselibrary.base.open.model.EmptyBean
import com.attrsense.android.baselibrary.base.open.model.ResponseData
import com.attrsense.android.baselibrary.base.open.viewmodel.BaseViewModel
import com.attrsense.android.repository.AppRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * author : zhangshuai@attrsense.com
 * date : 2022/10/14 15:17
 * mark : custom something
 */
@HiltViewModel
class FeedbackViewModel @Inject constructor(private val appRepository: AppRepository) :
    BaseViewModel() {

    val feedbackLivedata: MutableLiveData<Boolean> =
        MutableLiveData()

    fun feedback(description: String, pictures: List<String?>? = null) =
        appRepository.feedback(description, pictures).collectInLaunch {
            feedbackLivedata.value = when (it) {
                is ResponseData.onFailed -> false
                is ResponseData.onSuccess -> true
            }
        }
}
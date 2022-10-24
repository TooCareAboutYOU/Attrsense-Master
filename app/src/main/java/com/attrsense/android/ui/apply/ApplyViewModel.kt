package com.attrsense.android.ui.apply

import androidx.lifecycle.MutableLiveData
import com.attrsense.android.baselibrary.base.open.model.BaseResponse
import com.attrsense.android.baselibrary.base.open.model.EmptyBean
import com.attrsense.android.baselibrary.base.open.model.ResponseData
import com.attrsense.android.baselibrary.base.open.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.PrimitiveIterator
import javax.inject.Inject

/**
 * author : zhangshuai@attrsense.com
 * date : 2022/10/14 15:17
 * mark : custom something
 */
@HiltViewModel
class ApplyViewModel @Inject constructor(
    private val applyRepository: ApplyRepository
) : BaseViewModel() {

    val applyLiveData: MutableLiveData<ResponseData<BaseResponse<EmptyBean?>>> = MutableLiveData()

    /**
     * 提交申请
     */
    fun apply(name: String, mobile: String, company: String, email: String, briefly: String?) =
        applyRepository.apply(name, mobile, company, email, briefly).collectInLaunch {
            applyLiveData.value = it
        }

}
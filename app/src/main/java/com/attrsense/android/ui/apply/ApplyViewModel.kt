package com.attrsense.android.ui.apply

import com.attrsense.android.baselibrary.base.open.livedata.ResponseBaseMutableLiveData
import com.attrsense.android.baselibrary.base.open.model.EmptyBean
import com.attrsense.android.baselibrary.base.open.viewmodel.SkeletonViewModel
import com.attrsense.android.baselibrary.base.open.viewmodel.showLoadingAndCatch
import com.attrsense.android.repository.AppRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * author : zhangshuai@attrsense.com
 * date : 2022/10/14 15:17
 * mark : custom something
 */
@HiltViewModel
class ApplyViewModel @Inject constructor(
    private val appRepository: AppRepository
) : SkeletonViewModel() {

    val applyLiveData = ResponseBaseMutableLiveData<EmptyBean?>()

    /**
     * 提交申请
     */
    fun apply(name: String, mobile: String, company: String, email: String, briefly: String?) {
        appRepository.apply(name, mobile, company, email, briefly)
            .showLoadingAndCatch(this)
            .collectInLaunch {
                applyLiveData.value = it
            }
    }

}
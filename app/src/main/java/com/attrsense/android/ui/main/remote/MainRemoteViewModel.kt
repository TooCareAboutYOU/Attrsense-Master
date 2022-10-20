package com.attrsense.android.ui.main.remote

import androidx.lifecycle.MutableLiveData
import com.attrsense.android.baselibrary.base.open.model.BaseResponse
import com.attrsense.android.baselibrary.base.open.model.ResponseData
import com.attrsense.android.baselibrary.base.open.viewmodel.BaseViewModel
import com.attrsense.android.model.ImagesBean
import com.attrsense.database.db.entity.AnfImageEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * author : zhangshuai@attrsense.com
 * date : 2022/10/14 15:17
 * mark : custom something
 */
@HiltViewModel
class MainRemoteViewModel @Inject constructor(private val mainRemoteRepository: MainRemoteRepository) :
    BaseViewModel() {

    val getAllLiveData: MutableLiveData<ResponseData<BaseResponse<ImagesBean?>>> = MutableLiveData()

    fun uploadFile(
        token: String?,
        rate: String?,
        roiRate: String?,
        imageFilePaths: List<String>? = null
    ) = mainRemoteRepository.uploadFile(token, rate, roiRate, imageFilePaths).collectInLaunch {
        getAllLiveData.value = it
    }
}
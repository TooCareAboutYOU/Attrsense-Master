package com.attrsense.android.ui.main.remote

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.attrsense.android.baselibrary.base.open.model.BaseResponse
import com.attrsense.android.baselibrary.base.open.model.ResponseData
import com.attrsense.android.baselibrary.base.open.viewmodel.BaseViewModel
import com.attrsense.android.model.ImageInfoBean
import com.attrsense.android.model.ImagesBean
import com.attrsense.database.db.entity.AnfImageEntity
import com.attrsense.database.repository.DatabaseRepository
import com.orhanobut.logger.Logger
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.ArrayList
import javax.inject.Inject

/**
 * author : zhangshuai@attrsense.com
 * date : 2022/10/14 15:17
 * mark : custom something
 */
@HiltViewModel
class MainRemoteViewModel @Inject constructor(
    private val mainRemoteRepository: MainRemoteRepository,
    private val databaseRepository: DatabaseRepository
) : BaseViewModel() {

    val getAllLiveData: MutableLiveData<ResponseData<BaseResponse<ImagesBean?>>> = MutableLiveData()
    val uploadLiveData: MutableLiveData<ResponseData<BaseResponse<ImagesBean?>>> = MutableLiveData()
    val deleteLiveData: MutableLiveData<Int> = MutableLiveData()

    /**
     * 获取远端数据
     * @param page Int
     * @param perPage Int
     * @return Job
     */
    fun getRemoteFiles(
        page: Int,
        perPage: Int
    ) = mainRemoteRepository.getRemoteFiles(
        page,
        perPage
    ).collectInLaunch {
        saveToDatabase(it)
        getAllLiveData.value = it.apply {
            when (this) {
                is ResponseData.onFailed -> {
                    Log.e("printInfo", "MainRemoteViewModel::getRemoteFiles: ${this.throwable}")
                }
                is ResponseData.onSuccess -> {
                    Log.i("printInfo", "MainRemoteViewModel::getRemoteFiles: ${this.value}")
                }
            }
        }
    }

    /**
     * 上传文件
     * @param rate String?
     * @param roiRate String?
     * @param imageFilePaths List<String>?
     * @return Job
     */
    fun uploadFile(
        rate: String?,
        roiRate: String?,
        imageFilePaths: List<String>? = null
    ) = mainRemoteRepository.uploadFile(
        rate,
        roiRate,
        imageFilePaths
    ).collectInLaunch {
        saveToDatabase(it)
        uploadLiveData.value = it
    }




    /**
     * 保存到数据库
     */
    private val entityList: ArrayList<AnfImageEntity> by lazy { arrayListOf() }
    private fun saveToDatabase(it: ResponseData<BaseResponse<ImagesBean?>>) {
        when (it) {
            is ResponseData.onFailed -> {
                Log.e(
                    "printInfo",
                    "MainRemoteViewModel::saveToDatabase: ${it.throwable}"
                )
            }
            is ResponseData.onSuccess -> {
                it.value?.data?.images?.apply {
                    if (this.isNotEmpty()) {
                        entityList.clear()
                        this.forEach { item ->
                            val entity = AnfImageEntity(
                                token = mainRemoteRepository.userManger.getToken(),
                                originalImage = item?.thumbnailUrl,
                                anfImage = item?.url,
                                isLocal = false
                            )
                            entityList.add(entity)
                        }
                        addEntities(entityList)
                    }
                }
            }
        }
    }


    fun addEntities(entityList: List<AnfImageEntity>) =
        databaseRepository.addList(entityList).collectInLaunch {
            it.also { data ->
                when (data) {
                    is ResponseData.onFailed -> {
                        Log.e(
                            "printInfo",
                            "MainRemoteViewModel::addEntities: 添加失败！${data.throwable}"
                        )
                    }
                    is ResponseData.onSuccess -> {
                        Log.i("printInfo", "MainRemoteViewModel::addEntities: 添加成功！${data.value}")
                    }
                }
            }
        }


    fun deleteByAnfPath(position: Int, anfImage: String?) =
        databaseRepository.deleteByAnf(mainRemoteRepository.userManger.getToken(), anfImage)
            .collectInLaunch {
                when (it) {
                    is ResponseData.onFailed -> {

                    }
                    is ResponseData.onSuccess -> {
                        Log.i("printInfo", "MainLocalViewModel::deleteByAnfPath: ${it.value}")
                        deleteLiveData.value = position
                    }
                }
            }

    override fun onCleared() {
        super.onCleared()
        entityList.clear()
    }
}
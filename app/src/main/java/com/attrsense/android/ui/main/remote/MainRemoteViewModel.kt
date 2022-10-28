package com.attrsense.android.ui.main.remote

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.attrsense.android.baselibrary.base.open.model.BaseResponse
import com.attrsense.android.baselibrary.base.open.model.ResponseData
import com.attrsense.android.baselibrary.base.open.viewmodel.BaseAndroidViewModel
import com.attrsense.android.model.ImageInfoBean
import com.attrsense.android.model.ImagesBean
import com.attrsense.android.service.DownloadIntentService
import com.attrsense.database.db.entity.AnfImageEntity
import com.attrsense.database.repository.DatabaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
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
) : BaseAndroidViewModel() {

    val getAllLiveData: MutableLiveData<ResponseData<BaseResponse<ImagesBean?>>> = MutableLiveData()
    val uploadLiveData: MutableLiveData<ResponseData<BaseResponse<ImagesBean?>>> = MutableLiveData()
    val getByThumbLiveData: MutableLiveData<ResponseData<AnfImageEntity?>> = MutableLiveData()
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
        getAllLiveData.value = it.apply {
            when (this) {
                is ResponseData.onFailed -> {
                    Log.e("print_logs", "MainRemoteViewModel::getRemoteFiles: ${this.throwable}")
                }
                is ResponseData.onSuccess -> {
                    value?.data?.images?.also { imgList ->
                        if (imgList.isNotEmpty()) {
                            saveToDatabase(imgList)
                        }
                    }
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
        uploadLiveData.value = it.apply {
            when (this) {
                is ResponseData.onFailed -> {
                    Log.e("print_logs", "MainRemoteViewModel::getRemoteFiles: ${this.throwable}")
                }
                is ResponseData.onSuccess -> {
                    value?.data?.images?.also { imgList ->
                        if (imgList.isNotEmpty()) {
                            saveToDatabase(imgList)
                        }
                    }
                }
            }
        }
    }


    /**
     * 保存到数据库
     */
    private fun saveToDatabase(images: MutableList<ImageInfoBean>) {
        images.forEach { item ->
            databaseRepository.getByThumb(
                mainRemoteRepository.userManger.getToken(),
                item.thumbnailUrl
            ).collectInLaunch {
                when (it) {
                    is ResponseData.onFailed -> {
                        Log.e(
                            "print_logs",
                            "DownloadIntentService::onStartCommand: ${it.throwable}"
                        )
                    }
                    is ResponseData.onSuccess -> {
                        if (it.value == null || it.value?.isDownload == false) {
                            val entity = AnfImageEntity(
                                token = mainRemoteRepository.userManger.getToken(),
                                originalImage = item.thumbnailUrl,
                                thumbImage = item.thumbnailUrl,
                                anfImage = item.url,
                                isLocal = false
                            )
                            Log.i(
                                "print_logs",
                                "MainRemoteViewModel::saveToDatabase: $entity"
                            )
                            addEntity(entity)
                        }
                    }
                }
            }
        }
    }


    private fun addEntity(entity: AnfImageEntity) =
        databaseRepository.getAddOrUpdateByThumb(mainRemoteRepository.userManger.getToken(), entity)
            .collectInLaunch {
                it.also { data ->
                    when (data) {
                        is ResponseData.onFailed -> {
                            Log.e(
                                "print_logs",
                                "MainRemoteViewModel::addEntities: 添加失败！${data.throwable}"
                            )
                        }
                        is ResponseData.onSuccess -> {
                            Log.i(
                                "print_logs",
                                "MainRemoteViewModel::addEntities: 添加成功！${data.value}"
                            )
                            DownloadIntentService.start(
                                getApplication(),
                                entity.thumbImage,
                                entity.anfImage
                            )
                        }
                    }
                }
            }

    fun getByThumb(thumbImage: String?) =
        databaseRepository.getByThumb(mainRemoteRepository.userManger.getToken(), thumbImage)
            .collectInLaunch {
                getByThumbLiveData.value=it
            }


    fun addEntities(entityList: List<AnfImageEntity>) =
        databaseRepository.addList(entityList).collectInLaunch {
//            addEntityLiveData.value = it.also { data ->
//                when (data) {
//                    is ResponseData.onFailed -> {
//                        Log.e(
//                            "print_logs",
//                            "MainLocalViewModel::addEntities: 添加失败！${data.throwable}"
//                        )
//                    }
//                    is ResponseData.onSuccess -> {
//
//                    }
//                }
//            }
        }


    fun deleteByAnfPath(position: Int, anfImage: String?) =
        databaseRepository.deleteByAnf(mainRemoteRepository.userManger.getToken(), anfImage)
            .collectInLaunch {
                when (it) {
                    is ResponseData.onFailed -> {

                    }
                    is ResponseData.onSuccess -> {
                        Log.i("print_logs", "MainLocalViewModel::deleteByAnfPath: ${it.value}")
                        deleteLiveData.value = position
                    }
                }
            }

}
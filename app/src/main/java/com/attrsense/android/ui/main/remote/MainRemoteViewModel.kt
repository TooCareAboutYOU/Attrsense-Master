package com.attrsense.android.ui.main.remote

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.attrsense.android.baselibrary.base.open.model.BaseResponse
import com.attrsense.android.baselibrary.base.open.model.ResponseData
import com.attrsense.android.baselibrary.base.open.viewmodel.BaseAndroidViewModel
import com.attrsense.android.model.ImageInfoBean
import com.attrsense.android.model.ImagesBean
import com.attrsense.android.repository.AppRepository
import com.attrsense.android.service.DownloadService
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
    private val databaseRepository: DatabaseRepository,
    private val appRepository: AppRepository
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
    ) = appRepository.getRemoteFiles(
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
    ) = appRepository.uploadFile(
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
                appRepository.userManger.getMobile(),
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
                        if (it.value == null) {
                            val entity = AnfImageEntity(
                                token = appRepository.userManger.getToken(),
                                mobile = appRepository.userManger.getMobile(),
                                originalImage = item.thumbnailUrl,
                                thumbImage = item.thumbnailUrl,
                                anfImage = item.url,
                                isLocal = false
                            )
                            addEntity(entity)
                        }
                    }
                }
            }
        }
    }


    private fun addEntity(entity: AnfImageEntity) =
        databaseRepository.getAddOrUpdateByThumb(appRepository.userManger.getMobile(), entity)
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
                            DownloadService.start(
                                getApplication(),
                                entity.thumbImage,
                                entity.anfImage
                            )
                        }
                    }
                }
            }

    fun getByThumb(thumbImage: String?) =
        databaseRepository.getByThumb(appRepository.userManger.getMobile(), thumbImage)
            .collectInLaunch {
                getByThumbLiveData.value = it
            }


    fun addEntities(entityList: List<AnfImageEntity>) =
        databaseRepository.addList(entityList).collectInLaunch {
            when (it) {
                is ResponseData.onFailed -> {
                    Log.e(
                        "print_logs",
                        "MainLocalViewModel::addEntities: 添加失败！${it.throwable}"
                    )
                }
                is ResponseData.onSuccess -> {

                }
            }
        }


    fun deleteByThumb(position: Int, thumbImage: String?, fileId: String?) =
        appRepository.deleteFile(fileId).collectInLaunch {
            when (it) {
                is ResponseData.onFailed -> {
                    Log.e("print_logs", "MainLocalViewModel::deleteByThumb: 添加失败！${it.throwable}")
                }
                is ResponseData.onSuccess -> {
                    databaseRepository.deleteByThumb(
                        appRepository.userManger.getMobile(),
                        thumbImage
                    )
                        .collectInLaunch { state ->
                            when (state) {
                                is ResponseData.onFailed -> {

                                }
                                is ResponseData.onSuccess -> {
                                    deleteLiveData.value = position
                                }
                            }
                        }
                }
            }
        }
}
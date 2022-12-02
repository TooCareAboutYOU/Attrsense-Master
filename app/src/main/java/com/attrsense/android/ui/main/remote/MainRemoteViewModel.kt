package com.attrsense.android.ui.main.remote

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.attrsense.android.baselibrary.base.open.livedata.ResponseMutableBaseLiveData
import com.attrsense.android.baselibrary.base.open.livedata.ResponseMutableLiveData
import com.attrsense.android.baselibrary.base.open.model.ResponseData
import com.attrsense.android.baselibrary.base.open.viewmodel.SkeletonAndroidViewModel
import com.attrsense.android.baselibrary.base.open.viewmodel.showLoading
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
) : SkeletonAndroidViewModel() {

    val getAllLiveData = ResponseMutableBaseLiveData<ImagesBean?>()
    val uploadLiveData = ResponseMutableBaseLiveData<ImagesBean?>()

    val deleteLiveData = ResponseMutableLiveData<Int>()

    val getByThumbLiveData = ResponseMutableLiveData<Int>()
    val getAllDbLiveData = MutableLiveData<MutableList<AnfImageEntity>>()

    /**
     * 获取远端数据
     * @param page Int
     * @param perPage Int
     * @return Job
     */
    fun getRemoteFiles(
        page: Int,
        perPage: Int
    ) {
        appRepository.getRemoteFiles(
            page,
            perPage
        ).showLoading(this)
            .collectInLaunch {
                getAllLiveData.value = it.apply {
                    when (this) {
                        is ResponseData.OnFailed -> {
                            Log.e(
                                "print_logs",
                                "MainRemoteViewModel::getRemoteFiles: ${this.throwable.message}"
                            )
                        }
                        is ResponseData.OnSuccess -> {
                            value?.data?.images?.also { imgList ->
                                if (imgList.isNotEmpty()) {
                                    saveToDatabase(imgList)
                                }
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
        imageFilePaths: List<String> = emptyList()
    ) {
        appRepository.uploadFile(
            rate,
            roiRate,
            imageFilePaths
        ).collectInLaunch {
            uploadLiveData.value = it.apply {
                when (this) {
                    is ResponseData.OnFailed -> {
                        Log.e(
                            "print_logs",
                            "MainRemoteViewModel::getRemoteFiles: ${this.throwable.message}"
                        )
                    }
                    is ResponseData.OnSuccess -> {
                        value?.data?.images?.also { imgList ->
                            if (imgList.isNotEmpty()) {
                                saveToDatabase(imgList)
                            }
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
        val list = ArrayList<AnfImageEntity>(images.size)
        images.forEach { item ->
            val entity = AnfImageEntity(
                token = appRepository.userManger.getToken(),
                mobile = appRepository.userManger.getMobile(),
                originalImage = item.thumbnailUrl,
                thumbImage = item.thumbnailUrl,
                anfImage = item.url,
                fileId = item.fileId,
                srcSize = item.srcSize,
                isLocal = false
            )
            list.add(entity)
        }

        databaseRepository.addList(list).collectInLaunch {
            when (it) {
                is ResponseData.OnFailed -> {

                }
                is ResponseData.OnSuccess -> {

                    it.value?.also { entities ->
                        if (entities.isNotEmpty()) {
                            entities.forEach { data ->
                                DownloadService.start(
                                    getApplication(),
                                    data.thumbImage,
                                    data.anfImage
                                )
                            }
                        }

                    }
                }
            }
        }

    }

    fun getByThumb(position: Int, thumbImage: String?) {
        databaseRepository.getByThumb(appRepository.userManger.getMobile(), thumbImage)
            .collectInLaunch {
                getByThumbLiveData.value = ResponseData.OnSuccess(position)
                getAll()
            }
    }

    private fun getAll() {
        databaseRepository.getAllByType(appRepository.userManger.getMobile(), false)
            .collectInLaunch {
                when (it) {
                    is ResponseData.OnFailed -> {}
                    is ResponseData.OnSuccess -> {
                        it.value?.let { entities ->
                            if (entities.isNotEmpty()) {
                                getAllDbLiveData.value = entities
                            }
                        }
                    }
                }
            }
    }


    fun deleteByThumb(position: Int, thumbImage: String?, fileId: String?) {
        appRepository.deleteFile(fileId)
            .showLoading(this)
            .collectInLaunch {
                when (it) {
                    is ResponseData.OnFailed -> {
                        Log.e(
                            "print_logs",
                            "MainLocalViewModel::deleteByThumb: 添加失败！${it.throwable.message}"
                        )
                    }
                    is ResponseData.OnSuccess -> {
                        databaseRepository.deleteByThumb(
                            appRepository.userManger.getMobile(),
                            thumbImage
                        ).collectInLaunch { state ->
                            when (state) {
                                is ResponseData.OnFailed -> {

                                }
                                is ResponseData.OnSuccess -> {
                                    deleteLiveData.value = ResponseData.OnSuccess(position)
                                }
                            }
                        }
                    }
                }
            }
    }

    fun deleteAll() {
        databaseRepository.deleteType(appRepository.userManger.getMobile(), false).collectInLaunch {
            when (it) {
                is ResponseData.OnFailed -> {

                }
                is ResponseData.OnSuccess -> {

                }
            }
        }
    }


    override fun onCleared() {
        deleteAll()
        super.onCleared()
    }
}
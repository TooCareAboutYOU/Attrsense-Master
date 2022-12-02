package com.attrsense.android.ui.main.local

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.attrsense.android.baselibrary.base.open.livedata.ResponseMutableLiveData
import com.attrsense.android.baselibrary.base.open.model.ResponseData
import com.attrsense.android.baselibrary.base.open.viewmodel.SkeletonAndroidViewModel
import com.attrsense.android.manager.UserDataManager
import com.attrsense.database.db.entity.AnfImageEntity
import com.attrsense.database.db.entity.LocalAnfDataEntity
import com.attrsense.database.repository.DatabaseRepository
import com.blankj.utilcode.util.FileUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * author : zhangshuai@attrsense.com
 * date : 2022/10/14 15:17
 * mark : custom something
 */
@HiltViewModel
class MainLocalViewModel @Inject constructor(
    private val userDataManager: UserDataManager,
    private val databaseRepository: DatabaseRepository
) : SkeletonAndroidViewModel() {

    val getLiveData = ResponseMutableLiveData<MutableList<AnfImageEntity>>()
    val deleteLiveData: MutableLiveData<Int> = MutableLiveData()

    /**
     * 新增或者替换
     * @param entityList List<AnfImageEntity>
     * @return Job
     */
    fun addEntities(entityList: MutableList<AnfImageEntity>) {
        databaseRepository.addList(entityList).collectInLaunch {
            getLiveData.value = it.also { data ->
                when (data) {
                    is ResponseData.OnFailed -> {
                        Log.e(
                            "print_logs",
                            "MainLocalViewModel::addEntities: 添加失败！${data.throwable.message}"
                        )
                    }
                    is ResponseData.OnSuccess -> {
                        data.value?.forEach { entity ->
                            addToStatistics(entity)
                        }
                    }
                }
            }
        }
    }

    //添加到统计表
    private fun addToStatistics(entity: AnfImageEntity) {
        val data = LocalAnfDataEntity(
            mobile = userDataManager.getMobile(),
            originalAllSize = entity.srcSize,
            anfAllSize = FileUtils.getLength(entity.anfImage).toInt()
        )

        databaseRepository.addLocalData(data).collectInLaunch {
            when (it) {
                is ResponseData.OnFailed -> {}
                is ResponseData.OnSuccess -> {}
            }
        }
    }


    fun getAll() {
        databaseRepository.getAllByType(userDataManager.getMobile())
            .collectInLaunch { getLiveData.value = it }
    }

    fun deleteByAnfPath(position: Int, anfImage: String?) {
        databaseRepository.deleteByAnf(userDataManager.getMobile(), anfImage).collectInLaunch {
            when (it) {
                is ResponseData.OnFailed -> {
                    Log.e("print_logs", "MainLocalViewModel::deleteByAnfPath: ${it.throwable.message}")
                }
                is ResponseData.OnSuccess -> {
                    deleteLiveData.value = position
                }
            }
        }
    }

}
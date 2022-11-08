package com.attrsense.android.ui.main

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.attrsense.android.baselibrary.base.open.livedata.ResponseMutableLiveData2
import com.attrsense.android.baselibrary.base.open.model.ResponseData
import com.attrsense.android.baselibrary.base.open.viewmodel.BaseAndroidViewModel
import com.attrsense.android.baselibrary.base.open.viewmodel.BaseViewModel
import com.attrsense.android.manager.UserDataManager
import com.attrsense.database.repository.DatabaseRepository
import com.attrsense.database.db.entity.AnfImageEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * author : zhangshuai@attrsense.com
 * date : 2022/10/14 15:17
 * mark : custom something
 */
@HiltViewModel
class ImageViewPagerViewModel @Inject constructor(
    private val databaseRepository: DatabaseRepository
) : BaseViewModel() {

    val getLiveData = ResponseMutableLiveData2<List<AnfImageEntity>?>()

    /**
     * 更多数据
     * @param entityList List<AnfImageEntity>
     * @return Job
     * 注： addEntities()方法有同样的效果
     */
    fun updateList(entityList: List<AnfImageEntity>) {
        databaseRepository.updateList(entityList).collectInLaunch {
            when (it) {
                is ResponseData.onFailed -> {
                    Log.e(
                        "print_logs",
                        "MainLocalViewModel::addEntities: 添加失败！${it.throwable}"
                    )
                }
                is ResponseData.onSuccess -> {
                    getLiveData.value = it
                }
            }
        }
    }
}
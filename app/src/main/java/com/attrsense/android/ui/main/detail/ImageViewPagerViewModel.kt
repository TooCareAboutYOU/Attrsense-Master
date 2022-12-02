package com.attrsense.android.ui.main.detail

import android.util.Log
import com.attrsense.android.baselibrary.base.open.livedata.ResponseMutableLiveData
import com.attrsense.android.baselibrary.base.open.model.ResponseData
import com.attrsense.android.baselibrary.base.open.viewmodel.SkeletonViewModel
import com.attrsense.android.manager.UserDataManager
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
class ImageViewPagerViewModel @Inject constructor(
    private val databaseRepository: DatabaseRepository,
    private val userDataManager: UserDataManager
) : SkeletonViewModel() {

    val getLiveData = ResponseMutableLiveData<AnfImageEntity>()

    /**
     * 更多数据
     * @param entityList List<AnfImageEntity>
     * @return Job
     * 注： addEntities()方法有同样的效果
     */
    fun update(entity: AnfImageEntity) {
        databaseRepository.update(userDataManager.getMobile(),entity).collectInLaunch {
            when (it) {
                is ResponseData.OnFailed -> {
                    Log.e(
                        "print_logs",
                        "MainLocalViewModel::addEntities: 添加失败！${it.throwable.message}"
                    )
                }
                is ResponseData.OnSuccess -> {
                    getLiveData.value = it
                }
            }
        }
    }
}
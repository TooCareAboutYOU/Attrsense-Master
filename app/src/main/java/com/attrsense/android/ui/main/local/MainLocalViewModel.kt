package com.attrsense.android.ui.main.local

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.attrsense.android.baselibrary.base.open.model.ResponseData
import com.attrsense.android.baselibrary.base.open.viewmodel.BaseAndroidViewModel
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
class MainLocalViewModel @Inject constructor(
    private val userDataManager: UserDataManager,
    private val databaseRepository: DatabaseRepository
) : BaseAndroidViewModel() {

    val addEntityLiveData: MutableLiveData<ResponseData<Boolean>> = MutableLiveData()
    val getAllLiveData: MutableLiveData<ResponseData<List<AnfImageEntity?>>> = MutableLiveData()
    val deleteLiveData: MutableLiveData<Int> = MutableLiveData()

    fun addEntities(entityList: List<AnfImageEntity>) =
        databaseRepository.addList(entityList).collectInLaunch {
            addEntityLiveData.value = it.also { data ->
                when (data) {
                    is ResponseData.onFailed -> {
                        Log.e(
                            "printInfo",
                            "MainLocalViewModel::addEntities: 添加失败！${data.throwable}"
                        )
                    }
                    is ResponseData.onSuccess -> {
                        Log.i("printInfo", "MainLocalViewModel::addEntities: 添加成功！${data.value}")
                    }
                }
            }
        }

    fun getAll() =
        databaseRepository.getAll(userDataManager.getToken()).collectInLaunch {
            getAllLiveData.value = it
        }

    fun deleteByAnfPath(position: Int, anfImage: String?) =
        databaseRepository.deleteByAnf(userDataManager.getToken(),anfImage).collectInLaunch {
            when (it) {
                is ResponseData.onFailed -> {
                    Log.e("printInfo", "MainLocalViewModel::deleteByAnfPath: ${it.throwable}")
                }
                is ResponseData.onSuccess -> {
                    Log.i("printInfo", "MainLocalViewModel::deleteByAnfPath: ${it.value}")
                    deleteLiveData.value = position
                }
            }
        }

}
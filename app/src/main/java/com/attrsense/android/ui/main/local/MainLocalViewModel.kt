package com.attrsense.android.ui.main.local

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.attrsense.android.baselibrary.base.open.model.ResponseData
import com.attrsense.android.baselibrary.base.open.viewmodel.BaseViewModel
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
    private val mainLocalRepository: MainLocalRepository
) : BaseViewModel() {

    val addSingleLiveData: MutableLiveData<ResponseData<Boolean>> = MutableLiveData()
    val addListLiveData: MutableLiveData<ResponseData<Boolean>> = MutableLiveData()
    val getAllLiveData: MutableLiveData<ResponseData<List<AnfImageEntity?>>> = MutableLiveData()

    fun addEntity(entity: AnfImageEntity) =
        mainLocalRepository.add(entity).collectInLaunch {
            addSingleLiveData.value = it.also { data ->
                when (data) {
                    is ResponseData.onFailed -> {
                        Log.i("printInfo", "MainLocalViewModel::addEntity: 添加失败！${data.throwable}")
                    }
                    is ResponseData.onSuccess -> {
                        Log.i("printInfo", "MainLocalViewModel::addEntity: 添加成功！${data.value}")
                    }
                }
            }
        }


    fun addEntities(entityList: List<AnfImageEntity>) =
        mainLocalRepository.addEntities(entityList).collectInLaunch {
            addListLiveData.value = it.also { data ->
                when (data) {
                    is ResponseData.onFailed -> {
                        Log.i(
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
        mainLocalRepository.getAll().collectInLaunch {
            getAllLiveData.value = it
        }

}
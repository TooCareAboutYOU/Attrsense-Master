package com.attrsense.android.ui.main.local

import com.attrsense.android.base.BaseRepository
import com.attrsense.android.baselibrary.base.open.model.ResponseData
import com.attrsense.database.DatabaseRepository
import com.attrsense.database.db.entity.AnfImageEntity
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * author : zhangshuai@attrsense.com
 * date : 2022/10/18 16:34
 * mark : custom something
 */
class MainLocalRepository @Inject constructor(
    private val databaseRepository: DatabaseRepository
) : BaseRepository() {

    /**
     * 添加单个
     */
    fun add(entity: AnfImageEntity) = flow {
        databaseRepository.getAnfDao().add(entity)
        emit(ResponseData.onSuccess(true))
    }.flowOnIO()

    /**
     * 添加集合
     */
    fun addEntities(entityList: List<AnfImageEntity>) = flow {
        databaseRepository.getAnfDao().addList(entityList)
        emit(ResponseData.onSuccess(true))
    }.flowOnIO()


    fun getByAnf(anfPath: String?) = flow {
        emit(ResponseData.onSuccess(databaseRepository.getAnfDao().getByAnf(anfPath)))
    }.flowOnIO()


    /**
     * 获取列表中的所有图片
     */
    fun getAll() = flow {
        emit(ResponseData.onSuccess(databaseRepository.getAnfDao().getAll()))
    }.flowOnIO()

    /**
     * 删除
     */
    fun deleteByAnfPath(anfImage: String?) = flow {
        emit(ResponseData.onSuccess(databaseRepository.getAnfDao().deleteByAnfPath(anfImage)))
    }.flowOnIO()
}
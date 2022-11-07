package com.attrsense.database.repository

import android.util.Log
import com.attrsense.android.baselibrary.base.open.model.ResponseData
import com.attrsense.android.baselibrary.base.open.repository.SkeletonRepository
import com.attrsense.database.db.AttrSenseRoomDatabase
import com.attrsense.database.db.dao.AnfImageDao
import com.attrsense.database.db.dao.UserDao
import com.attrsense.database.db.entity.AnfImageEntity
import com.attrsense.database.db.entity.LocalAnfDataEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * 数据库操作管理类
 */
class DatabaseRepository @Inject constructor(
    private val db: AttrSenseRoomDatabase,
) : SkeletonRepository() {

    private val anfDao = db.getAnfImageDao()

    fun getUserDao(): UserDao = db.getUserDao()

    private fun getAnfDao(): AnfImageDao = anfDao

    private fun getLocalAnfDao() = db.getLocalAnfData()


    /**
     * ---------------------------------------------------------------------------------------------
     *                                          优美的分割线
     * ---------------------------------------------------------------------------------------------
     * @description：
     */

    /**
     * 添加一个或多个
     * @param entity Array<out AnfImageEntity>
     * @return Flow<ResponseData<Boolean>>
     */
    fun add(vararg entity: AnfImageEntity) = flow {
        anfDao.add(*entity)
        emit(ResponseData.onSuccess(true))
    }.flowOnIO()

    /**
     * 添加列表
     * @param anfPaths List<AnfImageEntity>
     * @return Flow<ResponseData<Boolean>>
     */
    fun addList(anfPaths: MutableList<AnfImageEntity>) = flow {
        if (anfPaths.isNotEmpty()) {
            anfPaths.forEach {
                anfDao.getAddOrUpdateByThumb(it.mobile, it)
            }
//            anfDao.addList(anfPaths)
            emit(ResponseData.onSuccess(anfPaths))
        } else {
            emit(ResponseData.onSuccess(null))
        }
    }.flowOnIO()

    /**
     * 更新一个或多个
     * @param anfImageEntity Array<out AnfImageEntity>
     * @return Flow<ResponseData<Boolean>>
     */
    fun update(vararg anfImageEntity: AnfImageEntity) = flow {
        anfDao.update(*anfImageEntity)
        emit(ResponseData.onSuccess(true))
    }.flowOnIO()

    /**
     * 更新列表
     * @param entities List<AnfImageEntity>
     * @return Flow<ResponseData<Boolean>>
     */
    fun updateList(entities: List<AnfImageEntity>) = flow {
        if (entities.isNotEmpty()) {
            anfDao.updateList(entities)
            emit(ResponseData.onSuccess(entities))
        } else {
            emit(ResponseData.onSuccess(null))
        }
    }.flowOnIO()

    /**
     * 获取所有(本地/云端)缓存数据
     * @param mobile String?
     * @param isLocal Boolean?
     * @return Flow<ResponseData<List<AnfImageEntity?>>>
     */
    fun getAllByType(mobile: String?, isLocal: Boolean = true) = flow {
        emit(ResponseData.onSuccess(anfDao.getAllByType(mobile, isLocal)))
    }.flowOnIO()

    /**
     * 通过手机号查询
     * @param mobile String?
     * @return Flow<ResponseData<List<AnfImageEntity?>>>
     */
    fun getAll(mobile: String?) = flow {
        emit(ResponseData.onSuccess(anfDao.getAll(mobile)))
    }.flowOnIO()

    /**
     * 根据anf获取数据
     * @param mobile String?
     * @param anfPath String?
     * @return Flow<ResponseData<AnfImageEntity>>
     */
    fun getByAnf(mobile: String?, anfPath: String?) = flow {
        emit(ResponseData.onSuccess(anfDao.getByAnf(mobile, anfPath)))
    }.flowOnIO()

    /**
     * 根据缩略图获取数据
     * @param mobile String?
     * @param originalPath String
     * @return Flow<ResponseData<AnfImageEntity>>
     */
    fun getByOriginal(mobile: String?, originalPath: String) = flow {
        emit(ResponseData.onSuccess(anfDao.getByOriginal(mobile, originalPath)))
    }.flowOnIO()


    /**
     * 通过缩略图获取数据
     * @param thumbImage String?
     * @return Flow<ResponseData<AnfImageEntity>>
     */
    fun getByThumb(mobile: String?, thumbImage: String?) = flow {
        emit(ResponseData.onSuccess(anfDao.getByThumb(mobile, thumbImage)))
    }.flowOnIO()

    /**
     * 通过缩略图获取数据,有则修改，无则添加
     * @param newData AnfImageEntity
     * @return Flow<ResponseData<Boolean>>
     */
    fun getAddOrUpdateByThumb(mobile: String?, newData: AnfImageEntity) = flow {
        anfDao.getAddOrUpdateByThumb(mobile, newData)
        emit(ResponseData.onSuccess(true))
    }.flowOnIO()

    /**
     * 删除指定ANF数据和文件
     * @param anfImageEntity AnfImageEntity
     * @return Flow<ResponseData<Boolean>>
     */
    fun delete(anfImageEntity: AnfImageEntity) = flow {
        anfDao.deleteEntity(anfImageEntity)
        emit(ResponseData.onSuccess(true))
    }.flowOnIO()


    /**
     * 通过缩略图删除数据文件
     * @param mobile String?
     * @param thumbImage String?
     * @return Flow<ResponseData<Boolean>>
     */
    fun deleteByThumb(mobile: String?, thumbImage: String?) = flow {
        anfDao.deleteByThumb(mobile, thumbImage)
        emit(ResponseData.onSuccess(true))
    }.flowOnIO()

    /**
     * 批量删除数据和文件
     * @param anfImageEntity List<AnfImageEntity>
     * @return Flow<ResponseData<Boolean>>
     */
    fun deleteList(anfImageEntity: List<AnfImageEntity>) = flow {
        anfDao.deleteEntities(anfImageEntity)
        emit(ResponseData.onSuccess(true))
    }.flowOnIO()

    /**
     * 删除指定ANF数据和文件
     * @param mobile String?
     * @param anfImage String?
     * @return Flow<ResponseData<Boolean>>
     */
    fun deleteByAnf(mobile: String?, anfImage: String?) = flow {
        anfDao.deleteByAnfPath(mobile, anfImage)
        emit(ResponseData.onSuccess(true))
    }.flowOnIO()

    /**
     * 根据AND批量删除
     * @param mobile String?
     * @param anfImage List<String>
     * @return Flow<ResponseData<Boolean>>
     */
    fun deleteAnfs(mobile: String?, anfImage: List<String>) = flow {
        anfDao.deleteByAnfPaths(mobile, anfImage)
        emit(ResponseData.onSuccess(true))
    }.flowOnIO()

    /**
     * 删除本地或者云端缓存数据
     * @param mobile String?
     * @param isLocal Boolean?
     * @return Flow<ResponseData<Boolean>>
     */
    fun deleteType(mobile: String?, isLocal: Boolean = true) = flow {
        anfDao.deleteByType(mobile, isLocal)
        emit(ResponseData.onSuccess(true))
    }.flowOnIO()

    /**
     * 清空指定用户的所有数据
     * @param mobile String?
     * @return Flow<ResponseData<Boolean>>
     */
    fun clearByToken(mobile: String?) = flow {
        anfDao.clearAll(mobile)
        emit(ResponseData.onSuccess(true))
    }.flowOnIO()

    /**
     * 清空数据库所有数据
     * @return Flow<ResponseData<Boolean>>
     */
    fun clear() = flow {
        anfDao.clearDb()
        emit(ResponseData.onSuccess(true))
    }.flowOnIO()


    /**
     * ---------------------------------------------------------------------------------------------
     *                                          优美的分割线
     * ---------------------------------------------------------------------------------------------
     * @description：
     */

    /**
     * 用于统计本地压缩anf数据
     * @param entity LocalAnfDataEntity
     * @return Flow<ResponseData<Boolean>>
     */
    fun addLocalData(entity: LocalAnfDataEntity) = flow {
        getLocalAnfDao().addData(entity)
        emit(ResponseData.onSuccess(true))
    }.flowOnIO()

    /**
     * 通过手机号查询
     * @param mobile String?
     * @return Flow<ResponseData<LocalAnfDataEntity>>
     */
    fun getLocalData(mobile: String?) = flow {
        emit(ResponseData.onSuccess(getLocalAnfDao().getData(mobile)))
    }.flowOnIO()

    /**
     * ---------------------------------------------------------------------------------------------
     *                                          优美的分割线
     * ---------------------------------------------------------------------------------------------
     * @description： 慎重操作 重置表自增索引
     */

    fun reset() = flow {
        anfDao.clearDb()
        anfDao.reset()
        emit(ResponseData.onSuccess(true))
    }.flowOnIO()
}
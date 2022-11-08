package com.attrsense.database.repository

import com.attrsense.android.baselibrary.base.open.repository.SkeletonRepository
import com.attrsense.database.db.AttrSenseRoomDatabase
import com.attrsense.database.db.dao.LocalAnfDataDao
import com.attrsense.database.db.dao.UserDao
import com.attrsense.database.db.entity.AnfImageEntity
import com.attrsense.database.db.entity.LocalAnfDataEntity
import javax.inject.Inject

/**
 * 数据库操作管理类
 */
class DatabaseRepository @Inject constructor(
    private val db: AttrSenseRoomDatabase,
) : SkeletonRepository() {

    private val anfDao = db.getAnfImageDao()

    fun getUserDao(): UserDao = db.getUserDao()

    fun getLocalAnfDao(): LocalAnfDataDao = db.getLocalAnfData()


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
    fun add(vararg entity: AnfImageEntity) = request {
        anfDao.add(*entity)
        true
    }

    /**
     * 添加列表
     * @param anfPaths List<AnfImageEntity>
     * @return Flow<ResponseData<Boolean>>
     */
    fun addList(anfPaths: MutableList<AnfImageEntity>) = request {
        if (anfPaths.isNotEmpty()) {
            anfPaths.forEach {
                anfDao.getAddOrUpdateByThumb(it.mobile, it)
            }
            anfPaths
        } else {
            mutableListOf()
        }
    }

    /**
     * 更新一个或多个
     * @param anfImageEntity Array<out AnfImageEntity>
     * @return Flow<ResponseData<Boolean>>
     */
    fun update(vararg anfImageEntity: AnfImageEntity) = request {
        anfDao.update(*anfImageEntity)
        true
    }

    /**
     * 更新列表
     * @param entities List<AnfImageEntity>
     * @return Flow<ResponseData<Boolean>>
     */
    fun updateList(entities: List<AnfImageEntity>) = request {
        if (entities.isNotEmpty()) {
            anfDao.updateList(entities)
            entities
        } else {
            null
        }
    }

    /**
     * 获取所有(本地/云端)缓存数据
     * @param mobile String?
     * @param isLocal Boolean?
     * @return Flow<ResponseData<List<AnfImageEntity?>>>
     */
    fun getAllByType(mobile: String?, isLocal: Boolean = true) = request {
        anfDao.getAllByType(mobile, isLocal)
    }

    /**
     * 通过手机号查询
     * @param mobile String?
     * @return Flow<ResponseData<List<AnfImageEntity?>>>
     */
    fun getAll(mobile: String?) = request {
        anfDao.getAll(mobile)
    }

    /**
     * 根据anf获取数据
     * @param mobile String?
     * @param anfPath String?
     * @return Flow<ResponseData<AnfImageEntity>>
     */
    fun getByAnf(mobile: String?, anfPath: String?) = request {
        anfDao.getByAnf(mobile, anfPath)
    }

    /**
     * 根据缩略图获取数据
     * @param mobile String?
     * @param originalPath String
     * @return Flow<ResponseData<AnfImageEntity>>
     */
    fun getByOriginal(mobile: String?, originalPath: String) = request {
        anfDao.getByOriginal(mobile, originalPath)
    }


    /**
     * 通过缩略图获取数据
     * @param thumbImage String?
     * @return Flow<ResponseData<AnfImageEntity>>
     */
    fun getByThumb(mobile: String?, thumbImage: String?) = request {
        anfDao.getByThumb(mobile, thumbImage)
    }

    /**
     * 通过缩略图获取数据,有则修改，无则添加
     * @param newData AnfImageEntity
     * @return Flow<ResponseData<Boolean>>
     */
    fun getAddOrUpdateByThumb(mobile: String?, newData: AnfImageEntity) = request {
        anfDao.getAddOrUpdateByThumb(mobile, newData)
        true
    }

    /**
     * 删除指定ANF数据和文件
     * @param anfImageEntity AnfImageEntity
     * @return Flow<ResponseData<Boolean>>
     */
    fun delete(anfImageEntity: AnfImageEntity) = request {
        anfDao.deleteEntity(anfImageEntity)
        true
    }


    /**
     * 通过缩略图删除数据文件
     * @param mobile String?
     * @param thumbImage String?
     * @return Flow<ResponseData<Boolean>>
     */
    fun deleteByThumb(mobile: String?, thumbImage: String?) = request {
        anfDao.deleteByThumb(mobile, thumbImage)
        true
    }

    /**
     * 批量删除数据和文件
     * @param anfImageEntity List<AnfImageEntity>
     * @return Flow<ResponseData<Boolean>>
     */
    fun deleteList(anfImageEntity: List<AnfImageEntity>) = request {
        anfDao.deleteEntities(anfImageEntity)
        true
    }

    /**
     * 删除指定ANF数据和文件
     * @param mobile String?
     * @param anfImage String?
     * @return Flow<ResponseData<Boolean>>
     */
    fun deleteByAnf(mobile: String?, anfImage: String?) = request {
        anfDao.deleteByAnfPath(mobile, anfImage)
        true
    }

    /**
     * 根据AND批量删除
     * @param mobile String?
     * @param anfImage List<String>
     * @return Flow<ResponseData<Boolean>>
     */
    fun deleteAnfs(mobile: String?, anfImage: List<String>) = request {
        anfDao.deleteByAnfPaths(mobile, anfImage)
        true
    }

    /**
     * 删除本地或者云端缓存数据
     * @param mobile String?
     * @param isLocal Boolean?
     * @return Flow<ResponseData<Boolean>>
     */
    fun deleteType(mobile: String?, isLocal: Boolean = true) = request {
        anfDao.deleteByType(mobile, isLocal)
        true
    }

    /**
     * 清空指定用户的所有数据
     * @param mobile String?
     * @return Flow<ResponseData<Boolean>>
     */
    fun clearByMobile(mobile: String?) = request {
        anfDao.clearAll(mobile)
        true
    }

    /**
     * 清空数据库所有数据
     * @return Flow<ResponseData<Boolean>>
     */
    fun clear() = request {
        anfDao.clearDb()
        true
    }


    /**
     * ---------------------------------------------------------------------------------------------
     *                                          优美的分割线
     * ---------------------------------------------------------------------------------------------
     * @description： 永久区：数据统计表
     */

    /**
     * 用于统计本地压缩anf数据
     * @param entity LocalAnfDataEntity
     * @return Flow<ResponseData<Boolean>>
     */
    fun addLocalData(entity: LocalAnfDataEntity) = request {
        getLocalAnfDao().addData(entity)
        true
    }

    /**
     * 通过手机号查询
     * @param mobile String?
     * @return Flow<ResponseData<LocalAnfDataEntity>>
     */
    fun getLocalData(mobile: String?) = request {
        getLocalAnfDao().getData(mobile)
    }

    /**
     * ---------------------------------------------------------------------------------------------
     *                                          优美的分割线
     * ---------------------------------------------------------------------------------------------
     * @description： 慎重操作 重置表自增索引
     */

    fun reset() = request {
        anfDao.clearDb()
        anfDao.reset()
        true
    }
}
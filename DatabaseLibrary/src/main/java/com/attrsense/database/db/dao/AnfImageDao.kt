package com.attrsense.database.db.dao

import androidx.room.*
import com.attrsense.database.db.entity.AnfImageEntity
import com.blankj.utilcode.util.FileUtils

/**
 * author : zhangshuai@attrsense.com
 * date : 2022/10/20 15:11
 * mark : custom something
 */
@Dao
interface AnfImageDao {
    /**
     * 添加
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun add(vararg anfImageEntity: AnfImageEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addList(anfImageEntity: List<AnfImageEntity>)

    /**
     * 更新
     */
    @Update
    suspend fun update(vararg anfImageEntity: AnfImageEntity)

    @Update
    suspend fun updateList(anfImageEntity: List<AnfImageEntity>)

    /**
     * 查询
     */
    @Query("SELECT * FROM ANF_IMAGE_TABLE WHERE user_mobile=:mobile AND is_local=:isLocal")
    suspend fun getAllByType(mobile: String?, isLocal: Boolean = true): MutableList<AnfImageEntity>

    /**
     * 查询
     */
    @Query("SELECT * FROM ANF_IMAGE_TABLE WHERE user_mobile=:mobile")
    suspend fun getAll(mobile: String?): List<AnfImageEntity?>

    @Query("SELECT * FROM ANF_IMAGE_TABLE WHERE user_mobile=:mobile AND anf_image=:anfImage")
    suspend fun getByAnf(mobile: String?, anfImage: String?): AnfImageEntity?

    @Query("SELECT * FROM ANF_IMAGE_TABLE WHERE user_mobile=:mobile AND original_image=:originalPath")
    suspend fun getByOriginal(mobile: String?, originalPath: String): AnfImageEntity?

    @Query("SELECT * FROM ANF_IMAGE_TABLE WHERE user_mobile=:mobile AND thumb_image=:thumbImage")
    suspend fun getByThumb(mobile: String?, thumbImage: String?): AnfImageEntity?


    /**
     * 删除
     * 使用需谨慎
     */
    @Delete
    suspend fun delete(vararg anfImageEntity: AnfImageEntity): Int

    @Delete
    suspend fun deleteList(anfImageEntity: List<AnfImageEntity>): Int

    @Query("DELETE FROM ANF_IMAGE_TABLE WHERE user_mobile=:mobile AND anf_image=:anfImage")
    suspend fun deleteAnf(mobile: String?, vararg anfImage: String?): Int

    @Query("DELETE FROM ANF_IMAGE_TABLE WHERE user_mobile=:mobile AND anf_image=:anfImage")
    suspend fun deleteAnfs(mobile: String?, anfImage: List<String>): Int

    @Query("DELETE FROM ANF_IMAGE_TABLE WHERE user_mobile=:mobile AND is_local=:isLocal")
    suspend fun deleteType(mobile: String?, isLocal: Boolean? = true)

    @Query("DELETE FROM ANF_IMAGE_TABLE WHERE user_mobile=:mobile")
    suspend fun clearByToken(mobile: String?)

    @Query("DELETE FROM ANF_IMAGE_TABLE")
    suspend fun clearDb()

    @Query("UPDATE sqlite_sequence SET seq =0 WHERE name ='Content'")
    suspend fun reset()

    /**
     * ---------------------------------------------------------------------------------------------
     *                                          优美的分割线
     * ---------------------------------------------------------------------------------------------
     * @description：业务上层操作文件删除相关语句
     *
     */

    @Transaction
    suspend fun updateByThumb(mobile: String?, entity: AnfImageEntity) {
        val oldEntity = getByThumb(mobile, entity.thumbImage)
        if (oldEntity != null) {
            oldEntity.mobile = entity.mobile
            oldEntity.token = entity.token
            oldEntity.originalImage = entity.originalImage
            oldEntity.thumbImage = entity.thumbImage
            oldEntity.anfImage = entity.anfImage
            oldEntity.cacheImage = entity.cacheImage
            oldEntity.isLocal = entity.isLocal
            update(oldEntity)
        }
    }

    @Transaction
    suspend fun getAddOrUpdateByThumb(mobile: String?, newData: AnfImageEntity) {
        val entity = getByThumb(mobile, newData.thumbImage)
        if (entity == null) {
            add(newData)
        } else {
//            entity.mobile = newData.mobile
//            entity.token = newData.token
//            entity.originalImage = newData.originalImage
//            entity.thumbImage = newData.thumbImage
//            entity.anfImage = newData.anfImage
//            if (newData.isDownloadHttpAnf) {
//                entity.cacheImage = newData.cacheImage
//            }
//            entity.isLocal = newData.isLocal
//            update(entity)
        }
    }

    //单个删除
    @Transaction
    suspend fun deleteEntity(entity: AnfImageEntity) {
        removeFile(entity)
        delete(entity)
    }

    //多个删除
    @Transaction
    suspend fun deleteEntities(entities: List<AnfImageEntity>) {
        if (entities.isNotEmpty()) {
            entities.forEach { entity ->
                removeFile(entity)
            }
            deleteList(entities)
        }
    }


    /**
     * 通过缩略图删除数据
     */
    @Transaction
    suspend fun deleteByThumb(mobile: String?, thumbImage: String?) {
        getByThumb(mobile, thumbImage)?.let {
            removeFile(it)
            delete(it)
        }
    }


    /**
     * 通过单Anf路径单个删除
     */
    @Transaction
    suspend fun deleteByAnfPath(mobile: String?, anfImage: String?) {
        getByAnf(mobile, anfImage)?.let {
            removeFile(it)
        }
        deleteAnf(mobile, anfImage)
    }

    /**
     * 通过单Anf路径多个删除
     */
    @Transaction
    suspend fun deleteByAnfPaths(mobile: String?, anfImages: List<String>) {
        anfImages.forEach {
            getByAnf(mobile, it)?.let { entity ->
                removeFile(entity)
            }
        }
        deleteAnfs(mobile, anfImages)
    }

    /**
     * 通过mobile删除本地/云端的数据库数据和文件
     */
    @Transaction
    suspend fun deleteByType(mobile: String?, isLocal: Boolean = true) {
        getAll(mobile).let {
            if (it.isNotEmpty()) {
                it.forEach { entity ->
                    if (entity != null) {
                        removeFile(entity)
                    }
                }
                deleteType(mobile, isLocal)
            }
        }
    }

    @Transaction
    suspend fun clearAll(mobile: String?) {
        getAll(mobile).let {
            if (it.isNotEmpty()) {
                it.forEach { entity ->
                    if (entity != null) {
                        removeFile(entity)
                    }
                }
                clearByToken(mobile)
            }
        }
    }

    /**
     * 删除本地文件
     * @param entity AnfImageEntity
     */
    suspend fun removeFile(entity: AnfImageEntity) {
        FileUtils.delete(entity.thumbImage)
        FileUtils.delete(entity.anfImage)
        FileUtils.delete(entity.cacheImage)
    }
}
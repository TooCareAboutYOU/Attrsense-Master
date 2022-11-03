package com.attrsense.database.db.dao

import android.util.Log
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
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
    @Query("SELECT * FROM ANF_IMAGE_TABLE WHERE userMobile=:mobile AND isLocal=:isLocal")
    suspend fun getAllByType(mobile: String?, isLocal: Boolean = true): List<AnfImageEntity>

    /**
     * 查询
     */
    @Query("SELECT * FROM ANF_IMAGE_TABLE WHERE userMobile=:mobile")
    suspend fun getAll(mobile: String?): List<AnfImageEntity?>

    @Query("SELECT * FROM ANF_IMAGE_TABLE WHERE userMobile=:mobile AND anfImage=:anfImage")
    suspend fun getByAnf(mobile: String?, anfImage: String?): AnfImageEntity?

    @Query("SELECT * FROM ANF_IMAGE_TABLE WHERE userMobile=:mobile AND originalImage=:originalPath")
    suspend fun getByOriginal(mobile: String?, originalPath: String): AnfImageEntity?

    @Query("SELECT * FROM ANF_IMAGE_TABLE WHERE userMobile=:mobile AND thumbImage=:thumbImage")
    suspend fun getByThumb(mobile: String?, thumbImage: String?): AnfImageEntity?


    @Transaction
    suspend fun getAddOrUpdateByThumb(mobile: String?, newData: AnfImageEntity) {
        val entity = getByThumb(mobile, newData.thumbImage)
        if (entity == null) {
            add(newData)
        } else {
            entity.mobile=newData.mobile
            entity.token = newData.token
            entity.originalImage = newData.originalImage
            entity.thumbImage = newData.thumbImage
            if (!entity.isDownload) {
                entity.anfImage = newData.anfImage
            }
            entity.isLocal = newData.isLocal
            entity.cacheImage = newData.cacheImage
            update(entity)
        }
    }


    /**
     * 删除
     * 使用需谨慎
     */
    @Delete
    suspend fun delete(vararg anfImageEntity: AnfImageEntity): Int

    @Delete
    suspend fun deleteList(anfImageEntity: List<AnfImageEntity>): Int

    @Query("DELETE FROM ANF_IMAGE_TABLE WHERE userMobile=:mobile AND anfImage=:anfImage")
    suspend fun deleteAnf(mobile: String?, vararg anfImage: String?): Int

    @Query("DELETE FROM ANF_IMAGE_TABLE WHERE userMobile=:mobile AND anfImage=:anfImage")
    suspend fun deleteAnfs(mobile: String?, anfImage: List<String>): Int

    @Query("DELETE FROM ANF_IMAGE_TABLE WHERE userMobile=:mobile AND isLocal=:isLocal")
    suspend fun deleteType(mobile: String?, isLocal: Boolean? = true)

    @Query("DELETE FROM ANF_IMAGE_TABLE WHERE userMobile=:mobile")
    suspend fun clearByToken(mobile: String?)

    @Query("DELETE FROM ANF_IMAGE_TABLE")
    suspend fun clearDb()

    /**
     * ---------------------------------------------------------------------------------------------
     *                                          优美的分割线
     * ---------------------------------------------------------------------------------------------
     * @description：业务上层操作文件删除相关语句
     *
     */



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
    suspend fun deleteByThumb(mobile: String?,thumbImage: String?){
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
    suspend fun deleteByType(mobile: String?, isLocal: Boolean? = true) {
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
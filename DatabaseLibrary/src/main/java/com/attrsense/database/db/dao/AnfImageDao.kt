package com.attrsense.database.db.dao

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
     * 查询
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
    @Query("SELECT * FROM ANF_IMAGE_TABLE WHERE userToken=:token AND isLocal=:isLocal")
    suspend fun getAll(token: String?, isLocal: Boolean? = true): List<AnfImageEntity?>

    @Query("SELECT * FROM ANF_IMAGE_TABLE WHERE userToken=:token AND anfImage=:anfPath")
    suspend fun getByAnf(token: String?, anfPath: String?): AnfImageEntity?

    @Query("SELECT * FROM ANF_IMAGE_TABLE WHERE userToken=:token AND originalImage=:originalPath")
    suspend fun getByOriginal(token: String?, originalPath: String): AnfImageEntity?


    /**
     * 删除
     * 使用需谨慎
     */
    @Delete
    suspend fun delete(vararg anfImageEntity: AnfImageEntity):Int

    @Delete
    suspend fun deleteList(anfImageEntity: List<AnfImageEntity>):Int

    @Query("DELETE FROM ANF_IMAGE_TABLE WHERE userToken=:token AND anfImage=:anfImage")
    suspend fun deleteAnf(token: String?, vararg anfImage: String?): Int

    @Query("DELETE FROM ANF_IMAGE_TABLE WHERE userToken=:token AND anfImage=:anfImage")
    suspend fun deleteAnfs(token: String?, anfImage: List<String>): Int

    @Query("DELETE FROM ANF_IMAGE_TABLE WHERE userToken=:token AND isLocal=:isLocal")
    suspend fun deleteType(token: String?, isLocal: Boolean? = true)

    @Query("DELETE FROM ANF_IMAGE_TABLE WHERE userToken=:token")
    suspend fun clearByToken(token: String?)

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
    suspend fun deleteEntity(entity: AnfImageEntity){
        removeFile(entity)
        delete(entity)
    }

    //多个删除
    @Transaction
    suspend fun deleteEntities(entities: List<AnfImageEntity>){
        if (entities.isNotEmpty()) {
            entities.forEach { entity ->
                removeFile(entity)
            }
            deleteList(entities)
        }
    }


    /**
     * 通过单Anf路径单个删除
     */
    @Transaction
    suspend fun deleteByAnfPath(token: String?,anfPath: String?){
        getByAnf(token,anfPath)?.let {
            removeFile(it)
        }
        deleteAnf(token, anfPath)
    }

    /**
     * 通过单Anf路径多个删除
     */
    @Transaction
    suspend fun deleteByAnfPaths(token: String?,anfPaths: List<String>){
        anfPaths.forEach {
            getByAnf(token,it)?.let { entity->
                removeFile(entity)
            }
        }
        deleteAnfs(token,anfPaths)
    }

    /**
     * 通过token删除本地/云端的数据库数据和文件
     */
    @Transaction
    suspend fun deleteByType(token: String?,isLocal: Boolean?=true){
        getAll(token).let {
            if (it.isNotEmpty()) {
                it.forEach { entity->
                    if (entity != null) {
                        removeFile(entity)
                    }
                }
                deleteType(token,isLocal)
            }
        }
    }

    @Transaction
    suspend fun clearAll(token: String?){
        getAll(token).let {
            if (it.isNotEmpty()) {
                it.forEach { entity->
                    if (entity != null) {
                        removeFile(entity)
                    }
                }
                clearByToken(token)
            }
        }
    }

    suspend fun removeFile(entity: AnfImageEntity){
        FileUtils.delete(entity.anfImage)
        FileUtils.delete(entity.cacheImage)
    }
}
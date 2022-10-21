package com.attrsense.database.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.attrsense.database.db.entity.AnfImageEntity

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
     * 删除
     */
    @Delete
    suspend fun delete(vararg anfImageEntity: AnfImageEntity)

    @Delete
    suspend fun deleteList(anfImageEntity: List<AnfImageEntity>)

    @Query("DELETE FROM ANF_IMAGE_TABLE WHERE anfImage=:anfImage")
    suspend fun deleteByAnfPath(vararg anfImage: String?): Int

    @Query("DELETE FROM ANF_IMAGE_TABLE WHERE anfImage=:anfImage")
    suspend fun deleteByAnfPaths(anfImage: List<String?>)

    @Query("DELETE FROM ANF_IMAGE_TABLE WHERE userToken=:token AND isLocal=:isLocal")
    suspend fun clearRemote(token: String?, isLocal: Boolean? = false)

    @Query("DELETE FROM ANF_IMAGE_TABLE")
    suspend fun clear()

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
    @Query("SELECT * FROM ANF_IMAGE_TABLE WHERE isLocal=:isLocal")
    suspend fun getAll(isLocal: Boolean? = true): List<AnfImageEntity?>

    @Query("SELECT * FROM ANF_IMAGE_TABLE WHERE anfImage=:anfPath")
    suspend fun getByAnf(anfPath: String?): AnfImageEntity?

    @Query("SELECT * FROM ANF_IMAGE_TABLE WHERE originalImage=:originalPath")
    suspend fun getByOriginal(originalPath: String?): AnfImageEntity?
}
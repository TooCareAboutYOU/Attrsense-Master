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
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun add(vararg anfImageEntity: AnfImageEntity)

    @Delete
    suspend fun delete(vararg anfImageEntity: AnfImageEntity)

    @Update
    suspend fun update(vararg anfImageEntity: AnfImageEntity)

    @Transaction
    suspend fun addList(list: List<AnfImageEntity>) {
        if (list.isNotEmpty()) {
            list.forEach {
                add(it)
            }
        }
    }

    @Query("SELECT * FROM ANF_IMAGE_TABLE")
    suspend fun getAll(): List<AnfImageEntity?>

    @Query("SELECT * FROM ANF_IMAGE_TABLE WHERE anfImage=:anfPath")
    suspend fun queryByOriginal(anfPath: String?): AnfImageEntity?

    @Query("SELECT * FROM ANF_IMAGE_TABLE WHERE originalImage=:originalPath")
    suspend fun queryByAnf(originalPath: String?): AnfImageEntity?

    @Query("DELETE FROM ANF_IMAGE_TABLE")
    suspend fun clear()
}
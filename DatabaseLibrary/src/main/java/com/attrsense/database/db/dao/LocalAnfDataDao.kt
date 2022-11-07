package com.attrsense.database.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.attrsense.database.db.entity.LocalAnfDataEntity

/**
 * @author zhangshuai
 * @date 2022/11/7 11:31
 * @description
 */
@Dao
interface LocalAnfDataDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun add(vararg localAnfDataEntity: LocalAnfDataEntity)

    @Update
    suspend fun update(vararg localAnfDataEntity: LocalAnfDataEntity)

    @Query("SELECT * FROM LOCAL_ANF_DATA_TABLE  WHERE user_mobile=:mobile")
    suspend fun getData(mobile: String?): LocalAnfDataEntity?



    /**
     * ---------------------------------------------------------------------------------------------
     *                                          优美的分割线
     * ---------------------------------------------------------------------------------------------
     * @description：整合操作数据
     */

    @Transaction
    suspend fun addData(entity: LocalAnfDataEntity) {
        val item = getData(entity.mobile)
        if (item == null) {
            entity.count = 1
            add(entity)
        } else {
            item.originalAllSize += entity.originalAllSize
            item.anfAllSize += entity.anfAllSize
            item.count += 1
            update(item)
        }
    }
}
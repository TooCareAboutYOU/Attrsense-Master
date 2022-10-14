package com.attrsense.database.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.attrsense.database.db.entity.UserEntity

/**
 * author : zhangshuai@attrsense.com
 * date : 2022/10/14 11:04
 * mark : custom something
 */
@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun add(userEntity: UserEntity)

    @Delete
    fun delete(userEntity: UserEntity)

    @Update
    fun update(userEntity: UserEntity)

    @Query("SELECT * FROM USER WHERE mobile =:mobile")
    suspend fun queryByMobile(mobile: String?): List<UserEntity>?

    @Query("SELECT * FROM USER WHERE token =:token")
    suspend fun queryByToken(token: String?): List<UserEntity>?

    @Query("DELETE FROM USER")
    fun clear()
}
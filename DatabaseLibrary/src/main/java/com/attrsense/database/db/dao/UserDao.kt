package com.attrsense.database.db.dao

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
    suspend fun add(userEntity: UserEntity)

    @Delete
    suspend fun delete(userEntity: UserEntity)

    @Update
    suspend fun update(userEntity: UserEntity)

    @Query("SELECT * FROM USER_TABLE WHERE mobile =:mobile")
    suspend fun queryByMobile(mobile: String?): List<UserEntity>?

    @Query("SELECT * FROM USER_TABLE WHERE token =:token")
    suspend fun queryByToken(token: String?): List<UserEntity>?

    @Query("DELETE FROM USER_TABLE WHERE mobile=:mobile")
    suspend fun deleteByMobile(mobile: String?)

    @Query("DELETE FROM USER_TABLE")
    suspend fun clear()

    @Query("UPDATE sqlite_sequence SET seq =0 WHERE name ='Content'")
    suspend fun reset()

    /**
     * ---------------------------------------------------------------------------------------------
     *                                          优美的分割线
     * ---------------------------------------------------------------------------------------------
     * @description：
     */

    /**
     * 新增用户
     * @param userEntity UserEntity
     */
    @Transaction
    suspend fun addUser(userEntity: UserEntity) {
        queryByMobile(userEntity.mobile)?.let {
            if (it.isNotEmpty()) {
                it[0].token = userEntity.token
                it[0].refreshToken = userEntity.refreshToken
                update(it[0])
                return
            }
        }
        add(userEntity)
    }
}
package com.attrsense.android.repository

import com.attrsense.android.baselibrary.base.open.repository.SkeletonRepository
import com.attrsense.database.db.dao.UserDao
import com.attrsense.database.repository.DatabaseRepository
import javax.inject.Inject

/**
 * 数据库操作管理类
 */
class UserRepository @Inject constructor(
    private val databaseRepository: DatabaseRepository
) : SkeletonRepository() {

    fun getUserDao(): UserDao = databaseRepository.getUserDao()
}



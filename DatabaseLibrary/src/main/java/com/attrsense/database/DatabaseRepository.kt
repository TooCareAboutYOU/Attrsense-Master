package com.attrsense.database

import com.attrsense.android.baselibrary.base.open.repository.SkeletonRepository
import com.attrsense.android.baselibrary.util.MMKVUtils
import com.attrsense.database.db.AttrSenseRoomDatabase
import javax.inject.Inject

/**
 * 数据库操作管理类
 */
class DatabaseRepository @Inject constructor(
    private val db: AttrSenseRoomDatabase,
    private val mmkv: MMKVUtils
) :
    SkeletonRepository() {

    fun getUserDao() = db.getUserDao()
}
package com.attrsense.database

import com.attrsense.android.baselibrary.base.open.repository.BaseRepository
import com.attrsense.database.db.AttrSenseRoomDatabase
import javax.inject.Inject

/**
 * 数据库操作管理类
 */
class DatabaseRepository @Inject constructor(private val db: AttrSenseRoomDatabase) :
    BaseRepository() {


}
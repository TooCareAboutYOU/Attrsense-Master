package com.attrsense.database.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.attrsense.database.base.Converter
import com.attrsense.database.db.dao.AnfImageDao
import com.attrsense.database.db.dao.UserDao
import com.attrsense.database.db.entity.AnfImageEntity
import com.attrsense.database.db.entity.UserEntity

/**
 * author : zhangshuai@attrsense.com
 * date : 2022/10/14 10:53
 * mark : custom something
 */
@Database(
    entities = [
        UserEntity::class,
        AnfImageEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converter::class)
abstract class AttrSenseRoomDatabase : RoomDatabase() {

    abstract fun getUserDao(): UserDao

    abstract fun getAnfImageDao(): AnfImageDao

}
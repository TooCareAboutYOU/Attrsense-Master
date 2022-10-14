package com.attrsense.database.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.attrsense.database.db.AttrSenseRoomDatabase
import com.attrsense.database.db.dao.UserDao
import com.orhanobut.logger.Logger
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * author : zhangshuai@attrsense.com
 * date : 2022/10/14 11:34
 * mark : custom something
 */
@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    /**
     * 监听通知
     */
    private object RoomCallBack : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            //在新装app时会调用，调用时机为数据库build()之后，数据库升级时不调用此函数
            Logger.d("RoomCallBack is onCreated")
        }

        override fun onOpen(db: SupportSQLiteDatabase) {
            super.onOpen(db)
            Logger.d("RoomCallBack is onOpened")
        }

        override fun onDestructiveMigration(db: SupportSQLiteDatabase) {
            super.onDestructiveMigration(db)
            Logger.d("RoomCallBack is onDestructiveMigration")
        }
    }

    @Singleton
    @Provides
    fun provideAttrSenseRoomDatabase(@ApplicationContext context: Context): AttrSenseRoomDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            AttrSenseRoomDatabase::class.java,
            "attrSense.db"
        )
            .addCallback(RoomCallBack)
            .build()
    }

    @Singleton
    @Provides
    fun provideUserDao(attrSenseRoomDatabase: AttrSenseRoomDatabase): UserDao {
        return attrSenseRoomDatabase.getUserDao()
    }


}
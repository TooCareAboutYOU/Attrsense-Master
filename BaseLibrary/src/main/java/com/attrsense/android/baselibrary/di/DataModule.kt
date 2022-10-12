package com.attrsense.android.baselibrary.di

import android.content.Context
import com.attrsense.android.baselibrary.util.MMKVUtils
import com.attrsense.android.baselibrary.util.MMKVUtilsImpl
import com.tencent.mmkv.MMKV
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * author : zhangshuai@attrsense.com
 * date : 2022/10/8
 * mark : custom something
 */
@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Singleton
    @Provides
    fun provideMMKV(@ApplicationContext context: Context): MMKV {
        MMKV.initialize(context)
        return MMKV.defaultMMKV()
    }

    @Singleton
    @Provides
    fun provideMMKVUtils(mmkv: MMKV): MMKVUtils = MMKVUtilsImpl(mmkv)
    //或者方式二
//    @Singleton
//    @Binds
//    abstract fun provideMMKVUtils(mmkvUtils: MMKVUtilsImpl):MMKVUtils

}
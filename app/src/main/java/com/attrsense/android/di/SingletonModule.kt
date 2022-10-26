package com.attrsense.android.di

import com.attrsense.android.baselibrary.util.MMKVUtils
import com.attrsense.android.api.ApiService
import com.attrsense.android.util.UserDataManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

/**
 * author : zhangshuai@attrsense.com
 * date : 2022/10/8 18:52
 * mark : custom something
 */
@Module
@InstallIn(SingletonComponent::class)
class SingletonModule {

    @Singleton
    @Provides
    fun provideApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }

    @Singleton
    @Provides
    fun provideUserManager(mmkvUtils: MMKVUtils): UserDataManager {
        return UserDataManager(mmkvUtils)
    }
}
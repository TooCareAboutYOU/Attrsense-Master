package com.attrsense.android.baselibrary.di

import android.app.Application
import com.attrsense.android.baselibrary.view.LoadingDialog
import com.blankj.utilcode.util.Utils
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
object UtilModule {

    @Singleton
    @Provides
    fun provideUtilsCode(@ApplicationContext context: Application): Boolean {
        Utils.init(context)
        return true
    }
}
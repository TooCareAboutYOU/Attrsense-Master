package com.attrsense.android.baselibrary.test

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

/**
 * author : zhangshuai@attrsense.com
 * date : 2022/10/11 12:42
 * mark : custom something
 *
 * 使用：
 *   @Inject lateinit var event: Event
 *
 */
@Module
@InstallIn(ViewModelComponent::class)
abstract class ViewModelModule {

    @ViewModelScoped
    @Binds
    abstract fun bindTestEvent(testModelImpl: TestModelImpl): Event

}
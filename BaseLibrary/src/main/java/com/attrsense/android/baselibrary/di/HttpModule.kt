package com.attrsense.android.baselibrary.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Singleton
import android.util.Log
import com.attrsense.android.baselibrary.config.AppConfig
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * author : zhangshuai@attrsense.com
 * date : 2022/10/8 14:02
 * mark : custom something
 */
@Module
@InstallIn(SingletonComponent::class)
object HttpModule {

    private const val TIME_OUT = 60L

    @Singleton
    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        val interceptorLogger = HttpLoggingInterceptor() {
            Log.i("printInfo", "provideOkHttpClient: $it")
        }.apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        return OkHttpClient.Builder()
            .writeTimeout(TIME_OUT, TimeUnit.SECONDS)
            .connectTimeout(TIME_OUT, TimeUnit.SECONDS)
            .readTimeout(TIME_OUT, TimeUnit.SECONDS)
            .addInterceptor(interceptorLogger)
            .build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(AppConfig.BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createAsync())
            .addCallAdapterFactory(CoroutineCallAdapterFactory.invoke())
            .build()
    }
}
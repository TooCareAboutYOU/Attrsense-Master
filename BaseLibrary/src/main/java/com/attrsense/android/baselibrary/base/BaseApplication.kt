package com.attrsense.android.baselibrary.base

import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex

/**
 * author : zhangshuai@attrsense.com
 * date : 2022/10/8
 * mark : custom something
 */
open class BaseApplication : Application() {

    companion object {
        lateinit var INSTANCE: Context
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    override fun onCreate() {
        super.onCreate()
        INSTANCE = applicationContext
    }

    override fun onTerminate() {
        super.onTerminate()
    }
}
package com.attrsense.android.baselibrary.app

import android.app.Application
import android.content.Context
import android.os.Looper
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.multidex.MultiDex
import com.attrsense.android.baselibrary.BuildConfig
import com.attrsense.android.baselibrary.base.internal.SkeletonApplicationObserver
import com.attrsense.android.baselibrary.crash.CrashHandler
import com.bumptech.glide.Glide
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.FormatStrategy
import com.orhanobut.logger.Logger
import com.orhanobut.logger.PrettyFormatStrategy
import com.tencent.mmkv.MMKV
import com.yuyh.library.imgsel.ISNav

/**
 * author : zhangshuai@attrsense.com
 * date : 2022/10/8
 * mark : custom something
 */
open class SkeletonApplication : Application() {

    companion object {
        private lateinit var instance: Application
        fun getInstance() = instance
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        CrashHandler.init(this)

        ProcessLifecycleOwner.get().lifecycle.addObserver(SkeletonApplicationObserver(this))

        initLogger()

        Looper.myQueue().addIdleHandler {
            try {
                ISNav.getInstance().init { context, path, imageView ->
                    Glide.with(context).load(path).into(imageView)
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }
            false
        }
    }

    private fun initLogger() {
        val formatStrategy: FormatStrategy = PrettyFormatStrategy.newBuilder()
            .showThreadInfo(true) // (Optional) Whether to show thread info or not. Default true
            .methodCount(5) // (Optional) How many method line to show. Default 2
            .methodOffset(7) // (Optional) Hides internal method calls up to offset. Default 5
//            .logStrategy(customLog) // (Optional) Changes the log strategy to print out. Default LogCat
            .tag("Logger") // (Optional) Global tag for every log. Default PRETTY_LOGGER
            .build()
        Logger.addLogAdapter(object : AndroidLogAdapter(formatStrategy) {
            override fun isLoggable(priority: Int, tag: String?): Boolean {
                return BuildConfig.DEBUG
            }
        })
    }

    override fun onTerminate() {
        super.onTerminate()
        Logger.clearLogAdapters()
    }
}
package com.attrsense.android.app


import android.util.Log
import com.attrsense.android.BuildConfig
import com.attrsense.android.baselibrary.app.SkeletonApplication
import com.example.snpetest.JniInterface
import dagger.hilt.android.HiltAndroidApp

/**
 * author : zhangshuai@attrsense.com
 * date : 2022/10/8 10:33
 * mark : custom something
 */
@HiltAndroidApp
class AttrSenseApplication : SkeletonApplication() {

    override fun onCreate() {
        super.onCreate()
        JniInterface.initialAll(applicationContext)
    }
}
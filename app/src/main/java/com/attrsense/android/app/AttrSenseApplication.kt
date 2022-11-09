package com.attrsense.android.app

import android.os.Looper
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

        Looper.myQueue().addIdleHandler {
            try {
                JniInterface.initialAll(applicationContext)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            false
        }
    }
}
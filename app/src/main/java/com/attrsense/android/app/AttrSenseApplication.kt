package com.attrsense.android.app

import com.attrsense.android.baselibrary.base.internal.SkeletonApplication
import com.attrsense.android.baselibrary.test.Event
import com.example.snpetest.JniInterface
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

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
package com.attrsense.android.app

import android.util.Log
import com.attrsense.android.baselibrary.base.internal.SkeletonApplication
import com.attrsense.android.baselibrary.util.MMKVUtils
import com.attrsense.android.util.UserManger
import com.bumptech.glide.Glide
import com.example.snpetest.JNI
import com.yuyh.library.imgsel.ISNav
import com.yuyh.library.imgsel.common.ImageLoader
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

/**
 * author : zhangshuai@attrsense.com
 * date : 2022/10/8 10:33
 * mark : custom something
 */
@HiltAndroidApp
class AttrSenseApplication : SkeletonApplication() {


    private lateinit var jni: JNI

    companion object {
        lateinit var mInstance: AttrSenseApplication
    }

    override fun onCreate() {
        super.onCreate()
        mInstance = this
        ISNav.getInstance().init(ImageLoader { context, path, imageView ->
            Glide.with(context).load(path).into(imageView)
        })

        jni = JNI().apply {
//            this.initDecoder("","","")
//            this.initEncoder("","","")
        }
    }
}
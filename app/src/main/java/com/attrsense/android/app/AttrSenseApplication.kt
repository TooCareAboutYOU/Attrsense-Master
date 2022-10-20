package com.attrsense.android.app

import android.os.Environment
import android.util.Log
import androidx.core.content.ContentProviderCompat.requireContext
import com.attrsense.android.baselibrary.base.internal.SkeletonApplication
import com.bumptech.glide.Glide
import com.example.snpetest.FileUtils
import com.example.snpetest.JNI
import com.yuyh.library.imgsel.ISNav
import dagger.hilt.android.HiltAndroidApp
import java.io.File
import javax.inject.Inject

/**
 * author : zhangshuai@attrsense.com
 * date : 2022/10/8 10:33
 * mark : custom something
 */
@HiltAndroidApp
class AttrSenseApplication : SkeletonApplication() {

    @Inject
    lateinit var jni: JNI

    companion object {
        lateinit var mInstance: AttrSenseApplication
    }

    override fun onCreate() {
        super.onCreate()
        mInstance = this
        ISNav.getInstance().init { context, path, imageView ->
            Glide.with(context).load(path).into(imageView)
        }

        Thread {
            jni.apply {
                val data_path =
                    FileUtils.getDataFilePath(applicationContext, "engine/data/bias1.bin")
                FileUtils.getDataFilePath(applicationContext, "engine/data/bias1_i.bin")
                FileUtils.getDataFilePath(applicationContext, "engine/data/bias2.bin")
                FileUtils.getDataFilePath(applicationContext, "engine/data/bias2_i.bin")
                FileUtils.getDataFilePath(applicationContext, "engine/data/bias3.bin")
                FileUtils.getDataFilePath(applicationContext, "engine/data/bias3_i.bin")
                FileUtils.getDataFilePath(applicationContext, "engine/data/cvt_float_int.py")
                FileUtils.getDataFilePath(applicationContext, "engine/data/f_table.bin")
                FileUtils.getDataFilePath(applicationContext, "engine/data/M0_1.txt")
                FileUtils.getDataFilePath(applicationContext, "engine/data/M0_2.txt")
                FileUtils.getDataFilePath(applicationContext, "engine/data/M0_3.txt")
                FileUtils.getDataFilePath(applicationContext, "engine/data/weight1.bin")
                FileUtils.getDataFilePath(applicationContext, "engine/data/weight2.bin")
                FileUtils.getDataFilePath(applicationContext, "engine/data/weight3.bin")
                FileUtils.getDataFilePath(applicationContext, "engine/data/z_table.bin")

                val decode_model_path =
                    FileUtils.getModelFilePath(applicationContext, "engine/decode.dlc")
                val encode_model_path =
                    FileUtils.getModelFilePath(applicationContext, "engine/encode.dlc")
                val decode_out_path = externalCacheDir?.absolutePath + File.separator + "anf"
                val encode_out_path = externalCacheDir?.absolutePath + File.separator + "image"
                val so_path = applicationContext.applicationInfo.nativeLibraryDir

                setAdspLibraryPath(so_path)
                this.initDecoder(decode_model_path, data_path, decode_out_path)
                this.initEncoder(encode_model_path, data_path, encode_out_path)

                Log.i(
                    "printInfo", "初始化成功！"
                )
            }
        }.start()
    }
}
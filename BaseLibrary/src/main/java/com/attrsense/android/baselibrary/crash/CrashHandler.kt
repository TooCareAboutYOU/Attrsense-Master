package com.attrsense.android.baselibrary.crash

import android.app.Application
import android.content.Context
import java.io.File
import java.io.FileWriter
import java.io.PrintWriter
import java.text.SimpleDateFormat
import java.util.*

/**
 * @author zhangshuai@attrsense.com
 * @date 2022/10/26 12:44
 * @description
 */
class CrashHandler : Thread.UncaughtExceptionHandler {
    override fun uncaughtException(t: Thread, e: Throwable) {
        val dir = File(mContext!!.externalCacheDir, "crash_info")
        if (!dir.exists()) {
            dir.mkdirs()
        }

        val time = System.currentTimeMillis()

        val simpleDateFormat=SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒 E", Locale.CHINA)
        val date=Date(time)
        val result=simpleDateFormat.format(date)
        val file = File(dir, "$time.txt")
        try {
            PrintWriter(FileWriter(file)).apply {
                println("time: $result")
                println("thread：${t.name}")
                println("error：$e")
                close()
            }
            e.printStackTrace()
        } catch (ex: Exception) {
            ex.printStackTrace()
        } finally {
            if (mDefaultCrashHandler != null) {
                mDefaultCrashHandler!!.uncaughtException(t, e)
            }
        }
    }

    companion object {
        private const val FILE_NAME_SUFFIX = ".trace"
        private var mDefaultCrashHandler: Thread.UncaughtExceptionHandler? = null
        private var mContext: Context? = null
        fun init(context: Application?) {
            mContext = context

            //,获取该线程的默认异常处理器。默认为：RuntimeInit#KillApplicationHandler
            mDefaultCrashHandler = Thread.getDefaultUncaughtExceptionHandler()
            Thread.setDefaultUncaughtExceptionHandler(CrashHandler())
        }
    }
}
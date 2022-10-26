package com.attrsense.android.baselibrary.crash

import android.content.Context
import java.io.File
import java.io.FileWriter
import java.io.PrintWriter

/**
 * @author zhangshuai
 * @date 2022/10/26 12:44
 * @description
 */
class CrashHandler : Thread.UncaughtExceptionHandler {
    override fun uncaughtException(t: Thread, e: Throwable) {
        val dir = File(mContext!!.externalCacheDir, "crash_info")
        if (!dir.exists()) {
            dir.mkdirs()
        }
        val l = System.currentTimeMillis()
        val file = File(dir, "$l.txt")
        try {
            val printWriter = PrintWriter(FileWriter(file))
            printWriter.println("time: ${System.currentTimeMillis()}")
            printWriter.println("thread：${t.name}")
            printWriter.close()
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
        fun init(context: Context?) {
            mContext = context

            //,获取该线程的默认异常处理器。默认为：RuntimeInit#KillApplicationHandler
            mDefaultCrashHandler = Thread.getDefaultUncaughtExceptionHandler()
            Thread.setDefaultUncaughtExceptionHandler(CrashHandler())
        }
    }
}
package com.attrsense.android.baselibrary.service

import android.app.IntentService
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.attrsense.android.baselibrary.BuildConfig
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.FormatStrategy
import com.orhanobut.logger.Logger
import com.orhanobut.logger.PrettyFormatStrategy


internal class AppInitIntentService : IntentService("AppInitIntentService") {

    companion object {
        @JvmStatic
        fun startService(context: Context) {
            val intent = Intent(context, AppInitIntentService::class.java)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(intent)
            } else {
                context.startService(intent)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotification() {
        val nm =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = nm.javaClass.simpleName + this.javaClass.simpleName
        val channel =
            NotificationChannel(channelId, "channel_test", NotificationManager.IMPORTANCE_DEFAULT)
        nm.createNotificationChannel(channel)
        val notification = Notification.Builder(this).setChannelId(channelId)
            .build()
        startForeground(1, notification)
    }

    override fun onHandleIntent(intent: Intent?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotification()
        }
//        initLogger()
    }

    private fun initLogger() {
        val formatStrategy: FormatStrategy = PrettyFormatStrategy.newBuilder()
            //.showThreadInfo(true) // (Optional) Whether to show thread info or not. Default true
            //.methodCount(0) // (Optional) How many method line to show. Default 2
            //.methodOffset(7) // (Optional) Hides internal method calls up to offset. Default 5
            //.logStrategy(customLog) // (Optional) Changes the log strategy to print out. Default LogCat
            .tag("PrintLog") // (Optional) Global tag for every log. Default PRETTY_LOGGER
            .build()
        Logger.addLogAdapter(object : AndroidLogAdapter(formatStrategy) {
            override fun isLoggable(priority: Int, tag: String?): Boolean {
                return BuildConfig.DEBUG
            }
        })
        Logger.i("AppInitIntentService::initLogger: Init success!")
    }


}
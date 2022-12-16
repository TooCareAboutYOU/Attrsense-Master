package com.attrsense.android.app

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Looper
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager
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

    companion object {
        const val ACTION = "com.attrsense.android.local.action"

        private lateinit var instance: AttrSenseApplication
        fun getInstance() = instance
    }


    override fun onCreate() {
        super.onCreate()
        instance = this
        Looper.myQueue().addIdleHandler {
            try {
                JniInterface.initialAll(applicationContext)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            false
        }

        registerReceiver()
    }


    /**
     * ---------------------------------------------------------------------------------------------
     *                                          优美的分割线
     * ---------------------------------------------------------------------------------------------
     * @description：本地广播
     */
    private val getReceiver: LocalBroadcastManager by lazy { LocalBroadcastManager.getInstance(this) }

    private fun registerReceiver() {
        val intentFilter = IntentFilter().apply {
            addAction(ACTION)
        }
        getReceiver.registerReceiver(localBroadcastReceiver, intentFilter)
    }

    /**
     * AttrSenseApplication.getInstance().unRegisterReceiver()
     */
    fun unRegisterReceiver() {
        getReceiver.unregisterReceiver(localBroadcastReceiver)
    }

    /**
     * val intent=Intent(AttrSenseApplication.ACTION).apply {
     *      putExtra("test","本地广播接收成功！")
     * }
     * AttrSenseApplication.getInstance().sendReceiver(intent)
     */
    fun sendReceiver(intent: Intent, isSync: Boolean = false) {
        getReceiver.apply {
            intent.action = ACTION
            if (isSync) {
                sendBroadcastSync(intent)
            } else {
                sendBroadcast(intent)
            }
        }
    }

    private val localBroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            intent?.apply {
                val data = intent.getStringExtra("test")
                Log.i("print_logs", "传输成功: $data")
            }
        }
    }
}
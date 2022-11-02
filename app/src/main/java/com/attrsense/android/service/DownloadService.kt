package com.attrsense.android.service

import android.app.DownloadManager
import android.app.Service
import android.content.Intent
import android.content.Context
import android.content.IntentFilter
import android.net.Uri
import android.os.IBinder
import android.util.Log
import com.attrsense.android.baselibrary.base.open.model.ResponseData
import com.attrsense.android.manager.UserDataManager
import com.attrsense.android.receiver.DownloadBroadcastReceiver
import com.attrsense.database.repository.DatabaseRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.runBlocking
import javax.inject.Inject


@AndroidEntryPoint
class DownloadService : Service() {

    @Inject
    lateinit var databaseRepository: DatabaseRepository

    @Inject
    lateinit var userDataManager: UserDataManager

    private lateinit var downManager: DownloadManager
    private var downloadBroadcastReceiver: DownloadBroadcastReceiver? = null

    override fun onBind(intent: Intent?): IBinder? = null

    companion object {
        //用与判断是否下载成功查询的索引
        const val KEY_THUMB_PATH = "key_thumb_path"
        const val KEY_ANF_PATH = "key_anf_path"

        @JvmStatic
        fun start(context: Context, thumbPath: String?, anfHttpPath: String?) {
            val intent = Intent(context, DownloadService::class.java).apply {
                putExtra(KEY_THUMB_PATH, thumbPath)
                putExtra(KEY_ANF_PATH, anfHttpPath)
            }
            context.startService(intent)
        }
    }

    override fun onCreate() {
        super.onCreate()
        downManager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        downloadBroadcastReceiver = DownloadBroadcastReceiver()
        registerReceiver(
            downloadBroadcastReceiver,
            IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
        )
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.apply {
            getStringExtra(KEY_THUMB_PATH)?.let { thumb ->
                runBlocking {
                    databaseRepository.getByThumb(userDataManager.getMobile(), thumb).collect {
                        when (it) {
                            is ResponseData.onFailed -> {
                                Log.e(
                                    "print_logs",
                                    "DownloadService::onStartCommand: ${it.throwable}"
                                )
                            }
                            is ResponseData.onSuccess -> {
                                getStringExtra(KEY_ANF_PATH)?.also { anfHttpPath ->
                                    if (it.value?.isDownload == false) {
                                        download(anfHttpPath)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        return super.onStartCommand(intent, flags, startId)
    }

    private fun download(anfPath: String) {
        val request = DownloadManager.Request(Uri.parse(anfPath))
        //设置在什么网络情况下进行下载
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
        //用于设置漫游状态下是否可以下载

        request.setAllowedOverMetered(true)
        //设置通知栏标题
//        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
//        request.setTitle("下载ANF文件中")
//        request.setDescription("正在下载...")
        //设置文件存放目录
        request.setDestinationInExternalFilesDir(
            this,
            "Pictures/anf",
            anfPath.substringAfterLast("/")
        )
        val downloadId = downManager.enqueue(request)
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(downloadBroadcastReceiver)
    }
}
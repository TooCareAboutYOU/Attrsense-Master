package com.attrsense.android.receiver

import android.annotation.SuppressLint
import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.util.Log
import com.attrsense.android.baselibrary.base.open.model.ResponseData
import com.attrsense.android.manager.UserDataManager
import com.attrsense.database.repository.DatabaseRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import javax.inject.Inject

@SuppressLint("Range")
@AndroidEntryPoint
class DownloadBroadcastReceiver : BroadcastReceiver() {

    @Inject
    lateinit var databaseRepository: DatabaseRepository

    @Inject
    lateinit var userDataManager: UserDataManager

//    private var executorService: ExecutorService = Executors.newSingleThreadExecutor()

    private var downloadId: Long = -1L
    private val PATH_BEFORE = "file://"

    override fun onReceive(context: Context, intent: Intent?) {
        val downManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        intent?.let {
            if (it.action == DownloadManager.ACTION_DOWNLOAD_COMPLETE) {

                downloadId = it.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1L)

                if (downloadId == -1L) {
                    return
                }

//                executorService.execute {
                val query = DownloadManager.Query()
                //通过下载的id查找
                query.setFilterById(downloadId)
                val cursor: Cursor = downManager.query(query)
                try {
                    if (cursor.moveToFirst()) {
                        when (cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))) {
                            DownloadManager.STATUS_PAUSED -> {}
                            DownloadManager.STATUS_PENDING -> {}
                            DownloadManager.STATUS_RUNNING -> {}
                            DownloadManager.STATUS_FAILED -> {
                                cursor.close()
                            }
                            DownloadManager.STATUS_SUCCESSFUL -> {
                                var localAnfUri =
                                    cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI))

                                if (localAnfUri.contains(PATH_BEFORE)) {
                                    localAnfUri = localAnfUri.substringAfter(PATH_BEFORE)

                                    val remoteUri =
                                        cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_URI))

                                    runBlocking {
                                        databaseRepository.getByAnf(
                                            userDataManager.getMobile(),
                                            remoteUri
                                        ).collect { i ->
                                            when (i) {
                                                is ResponseData.onFailed -> {}
                                                is ResponseData.onSuccess -> {
                                                    i.value?.let { entity ->
                                                        entity.anfImage = localAnfUri
                                                        entity.isDownload = true
                                                        databaseRepository.update(entity)
                                                            .collect {
                                                                Log.i(
                                                                    "print_logs",
                                                                    "DownloadBroadcastReceiver::onReceive: 下载成功，写入数据库成功！"
                                                                )
                                                            }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                                cursor.close()
                            }
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
//                }
            }
        }
    }
}
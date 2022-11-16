package com.attrsense.android.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.ThumbnailUtils
import android.os.Environment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.*


/**
 * @author zhangshuai@attrsense.com
 * @date 2022/10/27 15:45
 * @description 文件操作
 */
object FilesHelper {

    private val cacheList: ArrayList<String?> by lazy { arrayListOf() }

    /**
     * 保存缩略图
     * @param context Context
     * @param path String 本地文件路径
     */
    fun saveThumb(context: Context, localPath: String?): String? {
        val result = localPath?.let {
            val rightPath = PhotoBitmapUtils.amendRotatePhoto(it, context)
            cacheList.add(rightPath)
            val bitmap = BitmapFactory.decodeFile(rightPath)
            val thumb = ThumbnailUtils.extractThumbnail(bitmap, bitmap.width, bitmap.height)
            val filename = it.substringAfterLast("/")
            saveFile(context, thumb, "${System.currentTimeMillis()}_$filename")
        }
        return result
    }


    /**
     * 保存Bitmap
     * @param context Context
     * @param fileName String 文件名
     */
    fun saveFile(context: Context, bitmap: Bitmap, folderName: String): String {
        val bitmapFile = File(createDir(context, "thumb"), folderName)
        try {
            if (!bitmapFile.exists()) {
                bitmapFile.createNewFile()
            }
            val fileOutputStream = FileOutputStream(bitmapFile)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, fileOutputStream)

            fileOutputStream.flush()
            fileOutputStream.close()
            if (!bitmap.isRecycled) {
                bitmap.recycle()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
//            MediaScannerConnection.scanFile(context, arrayOf(bitmapFile.toString()), null, null)
//            Log.i("print_logs", "FilesHelper::saveFile: 通知相册!")
        }
        return bitmapFile.absolutePath
    }


    /**
     * 创建文件夹
     * @param context Context
     * @param folderName String 文件夹名
     * @return String
     */
    fun createDir(context: Context, folderName: String): File {
        //sdk存储路径
//        val sdkPath = Environment.getExternalStoragePublicDirectory("Attrsense").absolutePath + File.separator
        val file = File(
            context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)?.absolutePath + File.separator,
            folderName
        )
        if (!file.exists()) {
            file.mkdirs()
        }
        return file
    }

    /**
     * 清空缓存的纠正图片
     */
    fun clearCache() {
        if (cacheList.isNotEmpty()) {
            CoroutineScope(Dispatchers.Default).launch {
                cacheList.forEach {
                    it?.apply {
                        if (this.isNotBlank()) {
                            val rightFile = File(this)
                            if (rightFile.exists()) {
                                rightFile.delete()
                            }
                        }
                    }
                }
            }
        }
    }

}
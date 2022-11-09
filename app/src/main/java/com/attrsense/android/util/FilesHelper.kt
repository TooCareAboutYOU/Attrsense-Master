package com.attrsense.android.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.ThumbnailUtils
import android.os.Environment
import java.io.File
import java.io.FileOutputStream

/**
 * @author zhangshuai
 * @date 2022/10/27 15:45
 * @description 文件操作
 */
object FilesHelper {

    /**
     * 保存缩略图
     * @param context Context
     * @param path String 本地文件路径
     */
    fun saveThumb(context: Context, localPath: String?): String? {
        val result = localPath?.let {
            val rightPath=PhotoBitmapUtils.amendRotatePhoto(it,context)
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
    fun saveFile(context: Context, bitmap: Bitmap, fileName: String): String {
        val bitmapFile = File(createDir(context, "thumb"), fileName)
        try {
            if (!bitmapFile.exists()) {
                bitmapFile.createNewFile()
            }
            val fileOutputStream = FileOutputStream(bitmapFile)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, fileOutputStream)
            fileOutputStream.flush()
            fileOutputStream.close()
        } catch (e: Exception) {
            e.printStackTrace()
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
        val file = File(
            context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)?.absolutePath + File.separator,
            folderName
        )
        if (!file.exists()) {
            file.mkdirs()
        }
        return file
    }

}
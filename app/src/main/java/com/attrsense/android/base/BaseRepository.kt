package com.attrsense.android.base

import com.attrsense.android.baselibrary.base.open.repository.SkeletonRepository
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody

/**
 * author : zhangshuai@attrsense.com
 * date : 2022/10/12 9:31
 * mark :
 */
open class BaseRepository : SkeletonRepository() {

    protected fun String.toRequestBody(): RequestBody =
        this.toRequestBody("text/plain".toMediaType())


    protected fun List<String>.toMultipartBody(): List<MultipartBody.Part> =
        if (this.isNotEmpty()) {
            ArrayList<MultipartBody.Part>(this.size).also {
                this.forEachIndexed { index, path ->
                    val file = java.io.File(path)
                    val imageFileBody = file.asRequestBody("multipart/form-data".toMediaType())
                    val filePart = MultipartBody.Part.createFormData(
                        "imageFile_$index",
                        file.name,
                        imageFileBody
                    )
                    it.add(filePart)
                }
            }
        } else {
            ArrayList(0)
        }

}
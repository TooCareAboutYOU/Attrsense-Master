package com.attrsense.android.baselibrary.util

import android.database.Cursor
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.FragmentActivity
import androidx.loader.app.LoaderManager
import androidx.loader.content.CursorLoader
import androidx.loader.content.Loader

/**
 * author : zhangshuai@attrsense.com
 * date : 2022/10/12 16:06
 * mark : custom something
 */
class PhotoLoaderUtils(private val activity: FragmentActivity) :
    LoaderManager.LoaderCallbacks<Cursor> {

    interface LoadFinishCallback {
        fun wholeImage(imageWhole: List<String>)
    }

    private var mLoadFinishCallback: LoadFinishCallback? = null
    fun setLoadFinishCallback(callback: LoadFinishCallback?) {
        mLoadFinishCallback = callback
    }


    private val PARAMS_NAME = arrayOf(
        MediaStore.Images.Media.DATA,
        MediaStore.Images.Media.DISPLAY_NAME,
        MediaStore.Images.Media.DATE_ADDED,
        MediaStore.Images.Media._ID
    )

    init {
        LoaderManager.getInstance(activity).initLoader(0, null, this)
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
        return CursorLoader(
            activity,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            PARAMS_NAME,
            null,
            null,
            PARAMS_NAME[2] + " DESC"
        )
    }

    override fun onLoadFinished(loader: Loader<Cursor>, data: Cursor?) {
        data?.let {
            val list = mutableListOf<String>()
            val photoCount = data.columnCount
            if (photoCount > 0) {
                it.moveToFirst()
                do {
                    val photoPath = it.getString(it.getColumnIndexOrThrow(PARAMS_NAME[0]))
                    list.add(photoPath)
                } while (data.moveToNext())

                mLoadFinishCallback?.wholeImage(list)
            }
        }
    }

    override fun onLoaderReset(loader: Loader<Cursor>) {

    }
}
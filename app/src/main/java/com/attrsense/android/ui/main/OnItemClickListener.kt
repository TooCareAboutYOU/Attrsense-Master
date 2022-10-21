package com.attrsense.android.ui.main

import com.attrsense.android.model.ImageInfoBean
import com.attrsense.database.db.entity.AnfImageEntity

/**
 * author : zhangshuai@attrsense.com
 * date : 2022/10/21 16:53
 * mark : custom something
 */
interface OnItemClickListener {

    fun onLocalClickEvent(entity: AnfImageEntity?) {

    }

    fun onRemoteClickEvent(entity: ImageInfoBean?) {

    }

    fun onLongClickEvent(position: Int, anfPath: String?)
}
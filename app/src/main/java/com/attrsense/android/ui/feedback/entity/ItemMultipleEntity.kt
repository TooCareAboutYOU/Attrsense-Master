package com.attrsense.android.ui.feedback.entity

import com.chad.library.adapter.base.entity.MultiItemEntity

/**
 * @author zhangshuai
 * @date 2022/10/25 16:42
 * @description
 */
class ItemMultipleEntity(override val itemType: Int) : MultiItemEntity {

    companion object {
        const val IMAGE = 0
        const val PLACE_HOLDER = 1
    }

    var imageUrl: String? = ""
}
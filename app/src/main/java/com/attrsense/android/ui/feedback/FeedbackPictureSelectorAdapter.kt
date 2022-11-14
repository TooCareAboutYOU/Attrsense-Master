package com.attrsense.android.ui.feedback

import com.attrsense.android.R
import com.attrsense.android.ui.feedback.entity.ItemMultipleEntity
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder

/**
 * @author zhangshuai@attrsense.com
 * @date 2022/10/25 16:37
 * @description
 */
class FeedbackPictureSelectorAdapter(private val list: MutableList<ItemMultipleEntity>) :
    BaseMultiItemQuickAdapter<ItemMultipleEntity, BaseViewHolder>(list) {

    init {
        addItemType(ItemMultipleEntity.IMAGE, R.layout.layout_feedback_item)
        addItemType(ItemMultipleEntity.PLACE_HOLDER, R.layout.layout_feedback_item_placeholder)
    }


    override fun convert(holder: BaseViewHolder, item: ItemMultipleEntity) {
        when (holder.itemViewType) {
            ItemMultipleEntity.IMAGE -> {
                Glide.with(context).load(item.imageUrl).into(holder.getView(R.id.acIv_img))
            }
            ItemMultipleEntity.PLACE_HOLDER -> {
            }
            else -> {}
        }
    }
}


package com.attrsense.android.ui.main.local

import com.attrsense.android.R
import com.attrsense.android.databinding.LayoutLocalItemBinding
import com.attrsense.database.db.entity.AnfImageEntity
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder

/**
 * author : zhangshuai@attrsense.com
 * date : 2022/10/20 15:28
 * mark : custom something
 */
class LocalImageAdapter :
    BaseQuickAdapter<AnfImageEntity, BaseDataBindingHolder<LayoutLocalItemBinding>>(R.layout.layout_local_item) {
    override fun convert(
        holder: BaseDataBindingHolder<LayoutLocalItemBinding>,
        item: AnfImageEntity
    ) {
        Glide.with(context).load(item.originalImage)
            .error(R.mipmap.ic_launcher)
            .into(holder.dataBinding!!.acIvImg)
    }
}
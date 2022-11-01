package com.attrsense.android.ui.main.remote

import com.attrsense.android.R
import com.attrsense.android.model.ImageInfoBean
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder

/**
 * author : zhangshuai@attrsense.com
 * date : 2022/10/20 15:28
 * mark : custom something
 */
class RemoteImageAdapter constructor(list: MutableList<ImageInfoBean>) :
    BaseQuickAdapter<ImageInfoBean, BaseViewHolder>(R.layout.layout_remote_item, list) {

    override fun convert(holder: BaseViewHolder, item: ImageInfoBean) {
        Glide.with(context)
            .load(item.thumbnailUrl)
            .error(R.mipmap.ic_launcher)
            .into(holder.getView(R.id.acIv_img))
    }
}
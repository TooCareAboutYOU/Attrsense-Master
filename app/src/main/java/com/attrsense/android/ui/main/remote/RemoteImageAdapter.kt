package com.attrsense.android.ui.main.remote

import com.attrsense.android.R
import com.attrsense.android.app.GlideApp
import com.attrsense.android.databinding.LayoutRemoteItemBinding
import com.attrsense.android.model.ImageInfoBean
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.BaseLoadMoreModule
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder

/**
 * author : zhangshuai@attrsense.com
 * date : 2022/10/20 15:28
 * mark : custom something
 */
class RemoteImageAdapter constructor(list: MutableList<ImageInfoBean>) :
    BaseQuickAdapter<ImageInfoBean, BaseDataBindingHolder<LayoutRemoteItemBinding>>(
        R.layout.layout_remote_item, list
    ), LoadMoreModule {

    override fun convert(
        holder: BaseDataBindingHolder<LayoutRemoteItemBinding>,
        item: ImageInfoBean
    ) {
        GlideApp.with(context)
            .load(item.thumbnailUrl)
            .error(R.mipmap.ic_launcher)
            .into(holder.dataBinding!!.acIvImg)
    }

}
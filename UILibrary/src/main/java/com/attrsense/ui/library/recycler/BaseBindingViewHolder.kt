package com.attrsense.ui.library.recycler

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

/**
 * author : zhangshuai@attrsense.com
 * date : 2022/10/19 11:21
 * mark : custom something
 */
open class BaseBindingViewHolder(
    val binding: ViewBinding
) : RecyclerView.ViewHolder(binding.root) {

}
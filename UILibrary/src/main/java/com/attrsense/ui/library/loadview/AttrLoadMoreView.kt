package com.attrsense.ui.library.loadview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import com.attrsense.ui.library.R
import com.chad.library.adapter.base.loadmore.BaseLoadMoreView
import com.chad.library.adapter.base.viewholder.BaseViewHolder

/**
 * @author zhangshuai
 * @date 2022/11/1 14:26
 * @description
 */
class AttrLoadMoreView : BaseLoadMoreView() {


    override fun getRootView(parent: ViewGroup): View = LayoutInflater.from(parent.context)
        .inflate(R.layout.layout_load_more_view, parent, false)

    override fun getLoadingView(holder: BaseViewHolder): View =
        holder.getView<AppCompatTextView>(R.id.acTv_more).apply {
            setText(R.string.string_load_more_loading)
        }

    override fun getLoadComplete(holder: BaseViewHolder): View =
        holder.getView<AppCompatTextView>(R.id.acTv_more).apply {
            setText(R.string.string_load_more_complete)
        }

    override fun getLoadEndView(holder: BaseViewHolder): View =
        holder.getView<AppCompatTextView>(R.id.acTv_more).apply {
            setText(R.string.string_load_more_end)
        }

    override fun getLoadFailView(holder: BaseViewHolder): View =
        holder.getView<AppCompatTextView>(R.id.acTv_more).apply {
            setText(R.string.string_load_more_failed)
        }
}
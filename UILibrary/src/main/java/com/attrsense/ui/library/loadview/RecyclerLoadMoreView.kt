package com.attrsense.ui.library.loadview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.appcompat.widget.AppCompatTextView
import com.attrsense.ui.library.R
import com.chad.library.adapter.base.loadmore.BaseLoadMoreView
import com.chad.library.adapter.base.viewholder.BaseViewHolder

/**
 * @author zhangshuai
 * @date 2022/11/1 14:26
 * @description
 */
class RecyclerLoadMoreView : BaseLoadMoreView() {


    override fun getRootView(parent: ViewGroup): View =
        LayoutInflater.from(parent.context).inflate(R.layout.layout_load_more_view, parent, false)

    override fun getLoadingView(holder: BaseViewHolder): View =
        createView(0, holder, R.string.string_load_more_loading)

    override fun getLoadComplete(holder: BaseViewHolder): View =
        createView(1, holder, R.string.string_load_more_complete)

    override fun getLoadEndView(holder: BaseViewHolder): View =
        createView(2, holder, R.string.string_load_more_end)

    override fun getLoadFailView(holder: BaseViewHolder): View =
        createView(3, holder, R.string.string_load_more_failed)

    private fun createView(type: Int, holder: BaseViewHolder, @StringRes resId: Int): View {
        return when (type) {
            0 -> {
                holder.getView<AppCompatTextView>(R.id.acTv_loading)
            }
            1 -> {
                holder.getView<AppCompatTextView>(R.id.acTv_complete)
            }
            2 -> {
                holder.getView<AppCompatTextView>(R.id.acTv_failed)
            }
            else -> {
                holder.getView<AppCompatTextView>(R.id.acTv_end)
            }
        }
    }
}
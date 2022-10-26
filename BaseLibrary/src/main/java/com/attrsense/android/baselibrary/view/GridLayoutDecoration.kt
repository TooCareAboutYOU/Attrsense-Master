package com.attrsense.android.baselibrary.view

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView


/**
 * @author zhangshuai
 * @date 2022/10/26 15:49
 * @description
 */
class GridLayoutDecoration(private val space: Int=5) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
//        super.getItemOffsets(outRect, view, parent, state)
        outRect.left = space
        outRect.bottom = space
        if (parent.getChildLayoutPosition(view) % 3 == 0) {
            outRect.left = 0
        }
    }
}
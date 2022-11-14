package com.attrsense.ui.library.recycler

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView


/**
 * @author zhangshuai@attrsense.com
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
        outRect.left = space
        outRect.top=space
        outRect.right=space
        outRect.bottom = space

//        if (parent.getChildLayoutPosition(view) % 3 == 0) {
//            outRect.left = 0
//        }
    }
}
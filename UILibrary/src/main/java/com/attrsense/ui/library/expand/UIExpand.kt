package com.attrsense.ui.library.expand

import android.util.Log
import android.view.View
import android.widget.Checkable

/**
 * @author zhangshuai@attrsense.com
 * @date 2022/11/2 09:40
 * @description
 */

internal var <T : View> T.lastClickTime: Long
    set(value) = setTag(1766613352, value)
    get() = getTag(1766613352) as? Long ?: 0

/**
 * 设置防重复点击的监听
 */
fun <T : View> T.singleClick(time: Long = 1000, block: () -> Unit) {
    this.setOnClickListener {
        val currentTimeMillis = System.currentTimeMillis()
        if (currentTimeMillis - lastClickTime > time || this is Checkable) {
            lastClickTime = currentTimeMillis
            block()
        }
    }
}
package com.attrsense.android.baselibrary.util

import android.view.View
import android.widget.Checkable
import com.jakewharton.rxbinding4.view.clicks

/**
 * @author zhangshuai
 * @date 2022/11/2 09:40
 * @description
 */

private var <T : View> T.lastClickTime: Long
    set(value) = setTag(1766613352, value)
    get() = getTag(1766613352) as? Long ?: 0

/**
 * 设置不能快速点击的监听
 */
fun <T : View> T.singleClick(onClickListener: View.OnClickListener, time: Long = 800) {
    clicks().subscribe {
        val currentTimeMillis = System.currentTimeMillis()
        if (currentTimeMillis - lastClickTime > time || this is Checkable) {
            lastClickTime = currentTimeMillis
            onClickListener.onClick(this)
        }
    }
}
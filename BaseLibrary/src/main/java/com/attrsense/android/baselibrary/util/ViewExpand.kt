package com.attrsense.android.baselibrary.util

import android.view.View
import android.widget.Checkable

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
    setOnClickListener {
        val currentTimeMillis = System.currentTimeMillis()
        if (currentTimeMillis - lastClickTime > time || this is Checkable) {
            lastClickTime = currentTimeMillis
            onClickListener.onClick(this)
        }
    }
}

//var <T : View> T.lastClickTime: Long
//    set(value) = setTag(1766613352, value)
//    get() = getTag(1766613352) as? Long ?: 0
//
//inline fun <T : View> T.singleClick(needLogin: Boolean = true, time: Long = 200, crossinline block: (T) -> Unit) {
//    setOnClickListener {
//        val isLogin = HDUserManager.getInstance().isLoggedIn
//        if (needLogin && !isLogin) {
//            context.getActivity()?.apply {
//                ARouter.getInstance()
//                    .build(CommentConstant.Route.ACTIVITY_LOGIN)
//                    .withBoolean(IS_NEW_ONE_LOGIN, false)
//                    .navigation(this, 0)
//            }
//        } else {
//            val currentTimeMillis = System.currentTimeMillis()
//            if (currentTimeMillis - lastClickTime > time || this is Checkable) {
//                lastClickTime = currentTimeMillis
//                block(this)
//            }
//        }
//    }
//}
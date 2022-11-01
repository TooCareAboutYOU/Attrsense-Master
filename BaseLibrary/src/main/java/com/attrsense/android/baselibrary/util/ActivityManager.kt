package com.attrsense.android.baselibrary.util

import android.text.TextUtils
import androidx.appcompat.app.AppCompatActivity
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity
import java.util.concurrent.CopyOnWriteArrayList

/**
 * @author zhangshuai
 * @date 2022/10/31 15:26
 * @description
 */
class ActivityManager {

    private val activityStack: CopyOnWriteArrayList<AppCompatActivity> =
        CopyOnWriteArrayList<AppCompatActivity>()

    companion object {
        private object InnerClass {
            var holder = ActivityManager()
        }

        @JvmStatic
        fun getInstance(): ActivityManager {
            return InnerClass.holder
        }
    }


    /**
     * 移除指定Activity之外的所有Activity
     *
     * @param cls 指定界面Class
     */
    @Synchronized
    fun <T : RxAppCompatActivity?> removeAllExceptByClass(
        cls: Class<T>
    ) {
        if (activityStack.size > 0) {
            val iterator: Iterator<AppCompatActivity> = activityStack.iterator()
            while (iterator.hasNext()) {
                val activity = iterator.next()
                if (activity
                        .javaClass != cls
                ) {
                    remove(activity)
                }
            }
        }
    }

    /**
     * 获取堆栈实例化对象
     *
     * @return 堆栈对象
     */
    fun getActivityStack(): CopyOnWriteArrayList<AppCompatActivity> {
        return activityStack
    }

    /**
     * 加入stack栈
     *
     * @param item 传入上层生成Disposable
     */
    @Synchronized
    fun add(item: AppCompatActivity) {
        activityStack.add(item)
    }

    /**
     * 移除stack栈
     */
    @Synchronized
    fun remove(item: AppCompatActivity): Boolean {
        var item = item
        if (activityStack.contains(item)) {
            item.finish()
            activityStack.remove(item)
            return true
        }
        return false
    }

    /**
     * 指定删除多个Activity
     *
     * @param activities 指定删除Activity数组
     */
    @Synchronized
    fun removeMore(vararg activities: AppCompatActivity) {
        for (i in activities.indices) {
            val iterator: Iterator<AppCompatActivity> = activityStack.iterator()
            while (iterator.hasNext()) {
                if (iterator
                        .next() != activities[1]
                ) {
                    remove(iterator.next())
                }
            }
        }
    }

    /**
     * 清空stack栈
     */
    @Synchronized
    fun clear() {
        if (activityStack.size > 0) {
            val iterator: Iterator<AppCompatActivity> = activityStack.iterator()
            while (iterator.hasNext()) {
                remove(iterator.next())
            }
        }
        activityStack.clear()
    }

    @Synchronized
    fun removeAllExceptByName(classNames: ArrayList<String>) {
        if (activityStack.size > 0) {
            val iterator: Iterator<AppCompatActivity> = activityStack.iterator()
            while (iterator.hasNext()) {
                val activity = iterator.next()
                if (!isExistActivity(activity, classNames)) {
                    remove(activity)
                }
            }
        }
    }

    /**
     * 移除activity通过类名称
     */
    @Synchronized
    fun removeActivityByName(className: String) {
        if (activityStack.size > 0) {
            val iterator: Iterator<AppCompatActivity> = activityStack.iterator()
            while (iterator.hasNext()) {
                val activity = iterator.next()
                if (isExistActivity(activity, className)) {
                    remove(activity)
                    return
                }
            }
        }
    }

    /**
     * 根据类名称判断是否存在activity
     */
    fun isExistActivity(activity: AppCompatActivity?, className: String): Boolean {
        if (activity == null || TextUtils.isEmpty(className)) {
            return false
        }
        return activity.javaClass.simpleName == className
    }

    fun isExistActivity(activity: AppCompatActivity?, classNames: ArrayList<String>?): Boolean {
        if (activity == null || classNames == null) {
            return false
        }
        for (str in classNames) {
            if (activity.javaClass
                    .simpleName
                == str
            ) {
                return true
            }
        }
        return false
    }

    /**
     * 清空除首页之外的所有页面
     */
    fun removeNonMainActivities() {
        val classNames = ArrayList<String>()
        classNames.add("MainActivity")
        removeAllExceptByName(classNames)
    }

    /**
     * 获取栈顶activity(activeActivities维护的栈)
     */
    fun getTopActivity(): AppCompatActivity? {
        val size = activityStack.size
        return if (size == 0) {
            null
        } else activityStack[size - 1]
    }
}
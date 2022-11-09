package com.attrsense.android.baselibrary.util

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import java.util.concurrent.ConcurrentHashMap

/**
 * author : zhangshuai@attrsense.com
 * date : 2022/10/11 16:25
 * mark : 消息总线
 */
object SharedFlowBus {
    private var events = ConcurrentHashMap<Any, MutableSharedFlow<Any>>()
    private var stickyEvents = ConcurrentHashMap<Any, MutableSharedFlow<Any>>()

    /**
     * 发送消息
     */
    fun <T> send(objectKey: Class<T>): MutableSharedFlow<T> {
        if (!events.containsKey(objectKey)) {
            events[objectKey] = MutableSharedFlow(0, 1, BufferOverflow.DROP_OLDEST)
        }
        return events[objectKey] as MutableSharedFlow<T>
    }

    /**
     * 发送粘性消息
     */
    fun <T> sendSticky(objectKey: Class<T>): MutableSharedFlow<T> {
        if (!stickyEvents.containsKey(objectKey)) {
            stickyEvents[objectKey] = MutableSharedFlow(1, 1, BufferOverflow.DROP_OLDEST)
        }
        return stickyEvents[objectKey] as MutableSharedFlow<T>
    }

    /**
     * LiveData 订阅消息
     */
    fun <T> on(objectKey: Class<T>): LiveData<T> {
        return send(objectKey).asLiveData()
    }

    /**
     * LiveData 订阅粘性消息
     */
    fun <T> onSticky(objectKey: Class<T>): LiveData<T> {
        return sendSticky(objectKey).asLiveData()
    }

}
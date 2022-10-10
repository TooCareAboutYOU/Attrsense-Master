package com.attrsense.android.baselibrary.base.internal

import android.util.Log
import androidx.lifecycle.*

/**
 * author : zhangshuai@attrsense.com
 * date : 2022/10/10 15:17
 * mark : custom something
 */
internal class BaseApplicationObserver : DefaultLifecycleObserver, LifecycleEventObserver {

    private val TAG = "TAG_ApplicationObserver"

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        Log.i(TAG, "BaseApplicationObserver::onCreate: ")
    }

    override fun onStart(owner: LifecycleOwner) {
        super.onStart(owner)
        Log.i(TAG, "BaseApplicationObserver::onStart: ")
    }

    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)
        Log.i(TAG, "BaseApplicationObserver::onResume: ")
    }

    override fun onPause(owner: LifecycleOwner) {
        super.onPause(owner)
        Log.i(TAG, "BaseApplicationObserver::onPause: ")
    }

    override fun onStop(owner: LifecycleOwner) {
        super.onStop(owner)
        Log.i(TAG, "BaseApplicationObserver::onStop: ")
    }

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        Log.i(TAG, "BaseApplicationObserver::onDestroy: ")
    }

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        Log.i(
            TAG,
            "BaseApplicationObserver::onStateChanged: ${source.lifecycle.currentState}, event= $event"
        )
    }
}
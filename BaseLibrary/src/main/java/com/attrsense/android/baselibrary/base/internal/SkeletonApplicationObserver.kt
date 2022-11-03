package com.attrsense.android.baselibrary.base.internal

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.*
import com.attrsense.android.baselibrary.util.ActivityManager

/**
 * author : zhangshuai@attrsense.com
 * date : 2022/10/10 15:17
 * mark : custom something
 */
open class SkeletonApplicationObserver constructor(private val application: Application) :
    DefaultLifecycleObserver, LifecycleEventObserver {

    private val TAG = "TAG_ApplicationObserver"

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
//        Log.i(TAG, "BaseApplicationObserver::onCreate: ")
        application.registerActivityLifecycleCallbacks(activityCallback)
    }

    override fun onStart(owner: LifecycleOwner) {
        super.onStart(owner)
//        Log.i(TAG, "BaseApplicationObserver::onStart: ")
    }

    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)
//        Log.i(TAG, "BaseApplicationObserver::onResume: ")
    }

    override fun onPause(owner: LifecycleOwner) {
        super.onPause(owner)
//        Log.i(TAG, "BaseApplicationObserver::onPause: ")
    }

    override fun onStop(owner: LifecycleOwner) {
        super.onStop(owner)
//        Log.i(TAG, "BaseApplicationObserver::onStop: ")
    }

    //永远都不会调用
    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        application.unregisterActivityLifecycleCallbacks(activityCallback)
//        Log.i(TAG, "BaseApplicationObserver::onDestroy: ")
    }

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
//        Log.d(TAG, "BaseApplicationObserver::onStateChanged: ${source.lifecycle.currentState}, event= $event")
    }


    private val activityCallback = object : Application.ActivityLifecycleCallbacks {
        override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
            ActivityManager.getInstance().add(activity)
        }

        override fun onActivityStarted(activity: Activity) {

        }

        override fun onActivityResumed(activity: Activity) {

        }

        override fun onActivityPaused(activity: Activity) {

        }

        override fun onActivityStopped(activity: Activity) {

        }

        override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {

        }

        override fun onActivityDestroyed(activity: Activity) {
            ActivityManager.getInstance().remove(activity)
        }
    }
}
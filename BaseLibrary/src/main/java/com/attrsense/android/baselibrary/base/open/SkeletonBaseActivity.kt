package com.attrsense.android.baselibrary.base.open

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.attrsense.android.baselibrary.base.internal.BaseActivity
import kotlin.reflect.KClass

/**
 * author : zhangshuai@attrsense.com
 * date : 2022/10/8 10:07
 * mark : 不包含视图的基类
 */
open class SkeletonBaseActivity : BaseActivity() {

    //手动实例化ViewModel
    protected fun <VM : ViewModel> loadViewModel(vm: Class<VM>): VM {
        return ViewModelProvider(this)[vm]
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onRestart() {
        super.onRestart()
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}
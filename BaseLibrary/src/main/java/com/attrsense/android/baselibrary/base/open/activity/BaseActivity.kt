package com.attrsense.android.baselibrary.base.open.activity

import android.os.Bundle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.attrsense.android.baselibrary.base.internal.SkeletonActivity
import com.attrsense.android.baselibrary.base.open.viewmodel.BaseAndroidViewModel
import com.attrsense.android.baselibrary.base.open.viewmodel.BaseViewModel

/**
 * author : zhangshuai@attrsense.com
 * date : 2022/10/8 10:07
 * mark : 不包含视图的基类
 */
open class BaseActivity : SkeletonActivity() {

    /**
     * 子类需添加注释：@AndroidEntryPoint
     * 手动实例化ViewModel
     */
    fun <VM : ViewModel> loadViewModel(vm: Class<VM>): VM {
        return ViewModelProvider(this)[vm].also {
            when (it) {
                is BaseViewModel -> {
                    it.setOnViewModelCallback(this)
                }
                is BaseAndroidViewModel -> {
                    it.setOnViewModelCallback(this)
                }
                else -> {
                }
            }
        }
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
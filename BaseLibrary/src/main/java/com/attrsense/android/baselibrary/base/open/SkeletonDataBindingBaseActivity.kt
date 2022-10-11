package com.attrsense.android.baselibrary.base.open

import android.content.Intent
import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

/**
 * author : zhangshuai@attrsense.com
 * date : 2022/10/8 10:07
 * mark : 包含视图的基类-样式一
 */
abstract class SkeletonDataBindingBaseActivity<DB : ViewDataBinding> :
    SkeletonBaseActivity() {

    lateinit var mDataBinding: DB

    @LayoutRes
    protected abstract fun setLayoutResId(): Int

    open fun initViewBefore() {}

    protected abstract fun initView()

    override fun onCreate(savedInstanceState: Bundle?) {
        initViewBefore()
        super.onCreate(savedInstanceState)
        this.mDataBinding = DataBindingUtil.setContentView(this, setLayoutResId())
        initView()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
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
        mDataBinding.unbind()
    }
}
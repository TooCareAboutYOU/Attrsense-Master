package com.attrsense.android.baselibrary.base.open.activity

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleOwner
import kotlin.system.exitProcess

/**
 * author : zhangshuai@attrsense.com
 * date : 2022/10/8 10:07
 * mark : 包含视图的基类-样式一
 */
abstract class BaseDataBindingActivity<DB : ViewDataBinding> :
    BaseActivity() {


    lateinit var mDataBinding: DB

    @LayoutRes
    protected abstract fun setLayoutResId(): Int

    open fun initViewBefore(savedInstanceState: Bundle?) {}

    protected abstract fun initView(savedInstanceState: Bundle?)

    override fun onCreate(savedInstanceState: Bundle?) {
        initViewBefore(savedInstanceState)
        super.onCreate(savedInstanceState)
        this.mDataBinding = DataBindingUtil.setContentView(this, setLayoutResId())
        mDataBinding.lifecycleOwner = this
        initView(savedInstanceState)
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


    protected fun addBackPress(owner: LifecycleOwner) {
        onBackPressedDispatcher.addCallback(owner, onBackPress)
    }

    /**
     * 上次点击返回键的时间
     */
    private var lastBackPressTime = -1L

    /**
     * 在需要退出APP的页面添加以下代码：
     * onBackPressedDispatcher.addCallback(this, onBackPress)
     */
    private val onBackPress = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            val currentTIme = System.currentTimeMillis()

            if (lastBackPressTime == -1L || currentTIme - lastBackPressTime >= 2000L) {
                showBackPressTip()
                lastBackPressTime = currentTIme
            } else {
                //退出引用
                finish()
                android.os.Process.killProcess(android.os.Process.myPid())
                exitProcess(0)
//                moveTaskToBack(false)
            }
        }
    }

    protected fun showBackPressTip() {
        showToast("再按一次退出")
    }

}
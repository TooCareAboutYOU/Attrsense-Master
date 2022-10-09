package com.attrsense.android.ui.main

import com.attrsense.android.R
import com.attrsense.android.baselibrary.base.open.SkeletonDataBindingBaseActivity
import com.attrsense.android.databinding.ActivityMainBinding
import com.gyf.immersionbar.ktx.immersionBar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : SkeletonDataBindingBaseActivity<ActivityMainBinding>() {

    external fun stringFromJNI(): String

    companion object {
        // Used to load the 'android' library on application startup.
        init {
            System.loadLibrary("android")
        }
    }

    override fun setLayoutResId(): Int = R.layout.activity_main

    override fun initView() {
        //设置状态栏/导航栏样式
        immersionBar {
            transparentStatusBar()
            transparentNavigationBar()
        }
    }
}
package com.attrsense.android.ui.main

import android.content.Intent
import android.provider.MediaStore
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.viewpager2.widget.ViewPager2
import com.attrsense.android.R
import com.attrsense.android.baselibrary.base.open.activity.BaseDataBindingVMActivity
import com.attrsense.android.databinding.ActivityMainBinding
import com.attrsense.android.ui.main.local.MainLocalFragment
import com.attrsense.android.ui.main.my.MainMyFragment
import com.attrsense.android.ui.main.remote.MainRemoteFragment
import com.attrsense.android.widget.FragmentAdapter
import com.blankj.utilcode.util.ToastUtils
import dagger.hilt.android.AndroidEntryPoint


/**
 *
 */
@AndroidEntryPoint
class MainActivity : BaseDataBindingVMActivity<ActivityMainBinding, MainViewModel>() {

    external fun stringFromJNI(): String

    companion object {
        init {
            System.loadLibrary("android")
        }
    }

    override fun setViewModel(): Class<MainViewModel> = MainViewModel::class.java

    override fun setLayoutResId(): Int = R.layout.activity_main

    override fun initView() {
        super.initView()
        mDataBinding.tabBottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.item_local -> {
                    selectedItem(0)
                }
                R.id.item_remote -> {
                    selectedItem(1)
                }
                R.id.item_my -> {
                    selectedItem(2)
                }
                else -> {}
            }
            false
        }

        mDataBinding.viewpager2.registerOnPageChangeCallback(viewPager2PageChangeCallback)

        val fragmentList =
            arrayListOf(MainLocalFragment(), MainRemoteFragment(), MainMyFragment())

        mDataBinding.viewpager2.adapter = FragmentAdapter(this, fragmentList)
    }

    private fun selectedItem(position: Int) {
        mDataBinding.viewpager2.currentItem = position
        mDataBinding.tabBottomNavigation.menu.getItem(position).isChecked = true
    }

    override fun onDestroy() {
        super.onDestroy()
        mDataBinding.viewpager2.unregisterOnPageChangeCallback(viewPager2PageChangeCallback)
    }

    private val viewPager2PageChangeCallback = object :
        ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            mDataBinding.tabBottomNavigation.menu.getItem(position).isChecked = true
        }
    }

    //设置状态栏/导航栏样式
//        immersionBar {
//            transparentStatusBar()
//            transparentNavigationBar()
//        }
}
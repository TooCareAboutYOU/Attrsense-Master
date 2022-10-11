package com.attrsense.android.ui.main

import androidx.activity.viewModels
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.attrsense.android.R
import com.attrsense.android.baselibrary.base.open.SkeletonDataBindingBaseActivity
import com.attrsense.android.databinding.ActivityMainBinding
import com.attrsense.android.http.ApiService
import com.gyf.immersionbar.ktx.immersionBar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : SkeletonDataBindingBaseActivity<ActivityMainBinding>() {

    @Inject
    lateinit var apiService: ApiService

    private val mainViewModel by lazy {
        viewModelFactory { initializer { MainViewModel(apiService) } }.create(
            MainViewModel::class.java,
            defaultViewModelCreationExtras
        )
    }

    external fun stringFromJNI(): String

    companion object {
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

        mDataBinding.sampleText.setOnClickListener {
            mainViewModel.requestImage("js")
        }
        mainViewModel.github().observe(this) {

        }
    }
}
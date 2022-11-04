package com.attrsense.android.baselibrary.base.open.fragment

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.attrsense.android.baselibrary.base.internal.SkeletonFragment
import com.attrsense.android.baselibrary.base.open.viewmodel.BaseAndroidViewModel
import com.attrsense.android.baselibrary.base.open.viewmodel.BaseViewModel

/**
 * author : zhangshuai@attrsense.com
 * date : 2022/10/8 10:07
 * mark : custom something
 */
open class BaseFragment : SkeletonFragment() {

    //自定义
    protected fun <VM : ViewModel> loadViewModel(vm: Class<VM>): VM {
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

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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

    override fun onDestroyView() {
        super.onDestroyView()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onDetach() {
        super.onDetach()
    }
}
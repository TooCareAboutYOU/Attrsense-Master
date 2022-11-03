package com.attrsense.android.baselibrary.base.open.fragment

import android.os.Bundle
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import com.attrsense.android.baselibrary.base.open.activity.BaseActivity

/**
 * author : zhangshuai@attrsense.com
 * date : 2022/10/8 10:07
 * mark : custom something
 */
abstract class BaseDataBindingVMFragment<DB : ViewDataBinding, VM : ViewModel> :
    BaseDataBindingFragment<DB>() {

    lateinit var mViewModel: VM

    protected abstract fun setViewModel(): Class<VM>


    /**
     * 继承BaseDataBindingVMFragment的子类需要重写onCreateFragment方法，
     */
    override fun onCreateFragment(savedInstanceState: Bundle?) {
        super.onCreateFragment(savedInstanceState)
    }


    /**
     *
     * 继承BaseDataBindingVMFragment的子类需要重写initView()方法，
     * 并添加：super.initView(savedInstanceState)
     */
    override fun initView(savedInstanceState: Bundle?) {
        //也可以加载BaseActivity的loadViewModel方法，效果也一样
//        mViewModel = (requireActivity() as BaseActivity).loadViewModel(setViewModel())

        mViewModel = loadViewModel(setViewModel())
    }
}
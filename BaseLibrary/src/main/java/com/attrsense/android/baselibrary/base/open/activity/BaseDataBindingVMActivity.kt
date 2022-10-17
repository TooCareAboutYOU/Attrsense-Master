package com.attrsense.android.baselibrary.base.open.activity

import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel

/**
 * author : zhangshuai@attrsense.com
 * date : 2022/10/8 10:07
 * mark : 包含视图的基类-样式二
 */
abstract class BaseDataBindingVMActivity<DB : ViewDataBinding, VM : ViewModel> :
    BaseDataBindingActivity<DB>() {

    lateinit var mViewModel: VM

    protected abstract fun setViewModel(): Class<VM>

    /**
     * 子类需要重写该方法,且实现父类方法：super.initView()
     */
    override fun initView() {
        mViewModel = loadViewModel(setViewModel())
    }
}
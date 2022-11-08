package com.attrsense.android.baselibrary.base.open.fragment

import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.lang.reflect.ParameterizedType

/**
 * author : zhangshuai@attrsense.com
 * date : 2022/10/8 10:07
 * mark : custom something
 */
abstract class BaseDataBindingVMFragment<DB : ViewDataBinding, VM : ViewModel> :
    BaseDataBindingFragment<DB>() {


    protected val mViewModel: VM by lazy {
        val type = javaClass.genericSuperclass as ParameterizedType
        val vmClass: Class<VM> = type.actualTypeArguments[1] as Class<VM>
        ViewModelProvider(this)[vmClass].also { loadViewModel(vmClass) }
    }
}
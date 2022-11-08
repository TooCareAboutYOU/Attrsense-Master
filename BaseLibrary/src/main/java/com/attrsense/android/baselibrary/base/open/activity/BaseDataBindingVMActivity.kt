package com.attrsense.android.baselibrary.base.open.activity

import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import java.lang.reflect.ParameterizedType

/**
 * author : zhangshuai@attrsense.com
 * date : 2022/10/8 10:07
 * mark : 包含视图的基类-样式二
 */
abstract class BaseDataBindingVMActivity<DB : ViewDataBinding, VM : ViewModel> :
    BaseDataBindingActivity<DB>() {


    protected val mViewModel: VM by lazy {
        val type = javaClass.genericSuperclass as ParameterizedType
        val vmClass: Class<VM> = type.actualTypeArguments[1] as Class<VM>
        ViewModelProvider(this)[vmClass].also { loadViewModel(vmClass) }
    }
}
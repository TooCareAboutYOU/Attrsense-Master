package com.attrsense.android.ui.register

import android.os.Bundle
import com.attrsense.android.R
import com.attrsense.android.baselibrary.base.open.activity.BaseDataBindingVMActivity
import com.attrsense.android.databinding.ActivityRegisterBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterActivity :
    BaseDataBindingVMActivity<ActivityRegisterBinding, RegisterViewModel>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
    }

    override fun setLayoutResId(): Int = R.layout.activity_register

    override fun setViewModel(): Class<RegisterViewModel> = RegisterViewModel::class.java

    override fun initView() {
        super.initView()
    }
}
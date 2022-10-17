package com.attrsense.android.ui.login

import com.attrsense.android.R
import com.attrsense.android.baselibrary.base.open.activity.BaseDataBindingVMActivity
import com.attrsense.android.databinding.ActivityLoginBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : BaseDataBindingVMActivity<ActivityLoginBinding, LoginViewModel>() {

    override fun setLayoutResId(): Int = R.layout.activity_login

    override fun setViewModel(): Class<LoginViewModel> = LoginViewModel::class.java

    override fun initView() {
        super.initView()
    }
}
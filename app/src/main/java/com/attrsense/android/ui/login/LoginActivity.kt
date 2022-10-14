package com.attrsense.android.ui.login

import com.attrsense.android.R
import com.attrsense.android.baselibrary.base.open.SkeletonDataBindingVMBaseActivity
import com.attrsense.android.databinding.ActivityLoginBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : SkeletonDataBindingVMBaseActivity<ActivityLoginBinding, LoginViewModel>() {

    override fun setLayoutResId(): Int = R.layout.activity_login

    override fun setViewModel(): Class<LoginViewModel> = LoginViewModel::class.java

    override fun initView() {

    }
}
package com.attrsense.android.ui.apply

import com.attrsense.android.R
import com.attrsense.android.baselibrary.base.open.activity.BaseDataBindingVMActivity
import com.attrsense.android.databinding.ActivityApplyBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ApplyActivity : BaseDataBindingVMActivity<ActivityApplyBinding, ApplyViewModel>() {


    override fun setLayoutResId(): Int = R.layout.activity_apply

    override fun setViewModel(): Class<ApplyViewModel> = ApplyViewModel::class.java

    override fun initView() {
        super.initView()
    }
}
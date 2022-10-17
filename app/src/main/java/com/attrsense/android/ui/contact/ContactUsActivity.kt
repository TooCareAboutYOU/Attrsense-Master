package com.attrsense.android.ui.contact

import com.attrsense.android.R
import com.attrsense.android.baselibrary.base.open.activity.BaseDataBindingVMActivity
import com.attrsense.android.databinding.ActivityContactUsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ContactUsActivity :
    BaseDataBindingVMActivity<ActivityContactUsBinding, ContactUsViewModel>() {
    override fun setLayoutResId(): Int = R.layout.activity_contact_us

    override fun setViewModel(): Class<ContactUsViewModel> = ContactUsViewModel::class.java

    override fun initView() {
        super.initView()

    }

}
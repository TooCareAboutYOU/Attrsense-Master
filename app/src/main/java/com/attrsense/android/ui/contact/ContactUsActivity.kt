package com.attrsense.android.ui.contact

import com.attrsense.android.R
import com.attrsense.android.baselibrary.base.open.activity.BaseDataBindingVMActivity
import com.attrsense.android.databinding.ActivityContactUsBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 * 联系我们
 */
@AndroidEntryPoint
class ContactUsActivity :
    BaseDataBindingVMActivity<ActivityContactUsBinding, ContactUsViewModel>() {
    override fun setLayoutResId(): Int = R.layout.activity_contact_us

    override fun setViewModel(): Class<ContactUsViewModel> = ContactUsViewModel::class.java

    override fun initView() {
        super.initView()

        mDataBinding.acIvBack.setOnClickListener {
            finish()
        }

    }

}
package com.attrsense.android.ui.contact

import android.os.Bundle
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

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)

        mDataBinding.toolBarView.load(this).apply {
            this.setCenterTitle(R.string.tab_main_user_contact_title)
            this.setLeftClick {
                this@ContactUsActivity.finish()
            }
        }
    }

}
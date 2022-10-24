package com.attrsense.android.ui.about

import com.attrsense.android.R
import com.attrsense.android.baselibrary.base.open.activity.BaseDataBindingVMActivity
import com.attrsense.android.databinding.ActivityAboutUsBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 * 关于我们
 */
@AndroidEntryPoint
class AboutUsActivity :
    BaseDataBindingVMActivity<ActivityAboutUsBinding, AboutUsViewModel>() {

    override fun setLayoutResId(): Int = R.layout.activity_about_us

    override fun setViewModel(): Class<AboutUsViewModel> = AboutUsViewModel::class.java

    override fun initView() {
        super.initView()

        mDataBinding.acIvBack.setOnClickListener {
            finish()
        }
    }
}
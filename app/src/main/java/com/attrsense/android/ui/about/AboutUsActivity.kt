package com.attrsense.android.ui.about

import android.os.Bundle
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

    override fun initView(savedInstanceState: Bundle?) {
        mDataBinding.toolBarView.load(this).apply {
            this.setCenterTitle(R.string.tab_main_user_about_us_title)
            this.setLeftClick {
                this@AboutUsActivity.finish()
            }
        }
    }
}
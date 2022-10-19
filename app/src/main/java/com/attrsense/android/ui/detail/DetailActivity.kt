package com.attrsense.android.ui.detail


import com.attrsense.android.R
import com.attrsense.android.baselibrary.base.open.activity.BaseDataBindingActivity
import com.attrsense.android.baselibrary.base.open.activity.BaseDataBindingVMActivity
import com.attrsense.android.databinding.ActivityDetailBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailActivity : BaseDataBindingActivity<ActivityDetailBinding>() {

    override fun setLayoutResId(): Int = R.layout.activity_detail

    override fun initView() {

    }

}
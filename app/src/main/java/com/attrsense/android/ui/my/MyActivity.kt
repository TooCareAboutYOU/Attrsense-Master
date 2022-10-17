package com.attrsense.android.ui.my

import android.os.Bundle
import com.attrsense.android.R
import com.attrsense.android.baselibrary.base.open.activity.BaseDataBindingVMActivity
import com.attrsense.android.databinding.ActivityMyBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyActivity : BaseDataBindingVMActivity<ActivityMyBinding, MyViewModel>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my)
    }

    override fun setLayoutResId(): Int = R.layout.activity_my

    override fun setViewModel(): Class<MyViewModel> = MyViewModel::class.java

    override fun initView() {
        super.initView()

    }
}
package com.attrsense.android.ui.detail

import android.os.Bundle
import com.attrsense.android.R
import com.attrsense.android.baselibrary.base.open.fragment.BaseDataBindingVMFragment
import com.attrsense.android.databinding.FragmentMainLocalBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class DetailFragment :
    BaseDataBindingVMFragment<FragmentMainLocalBinding, DetailFragmentViewModel>() {

    override fun setLayoutResId(): Int = R.layout.fragment_main_local

    override fun initView(savedInstanceState: Bundle?) {
    }


}
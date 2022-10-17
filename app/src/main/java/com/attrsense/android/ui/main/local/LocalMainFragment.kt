package com.attrsense.android.ui.main.local

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.attrsense.android.R
import com.attrsense.android.baselibrary.base.open.fragment.BaseDataBindingVMFragment
import com.attrsense.android.databinding.FragmentLocalMainBinding
import dagger.hilt.android.AndroidEntryPoint


//private const val ARG_PARAM1 = "param1"
//private const val ARG_PARAM2 = "param2"


@AndroidEntryPoint
class LocalMainFragment :
    BaseDataBindingVMFragment<FragmentLocalMainBinding, LocalMainViewModel>() {

//    private var param1: String? = null
//    private var param2: String? = null
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        arguments?.let {
//            param1 = it.getString(ARG_PARAM1)
//            param2 = it.getString(ARG_PARAM2)
//        }
//    }
//
//    companion object {
//
//        @JvmStatic
//        fun newInstance(param1: String, param2: String) =
//            LocalMainFragment().apply {
//                arguments = Bundle().apply {
//                    putString(ARG_PARAM1, param1)
//                    putString(ARG_PARAM2, param2)
//                }
//            }
//    }

    override fun setLayoutResId(): Int = R.layout.fragment_local_main

    override fun setViewModel(): Class<LocalMainViewModel> = LocalMainViewModel::class.java


    override fun onCreateFragment(savedInstanceState: Bundle?) {
        super.onCreateFragment(savedInstanceState)
    }


    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
    }


}
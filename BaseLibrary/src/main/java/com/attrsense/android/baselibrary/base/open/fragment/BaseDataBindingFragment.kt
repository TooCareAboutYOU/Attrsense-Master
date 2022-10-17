package com.attrsense.android.baselibrary.base.open.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

/**
 * author : zhangshuai@attrsense.com
 * date : 2022/10/8 10:07
 * mark : custom something
 */
abstract class BaseDataBindingFragment<DB : ViewDataBinding> : BaseFragment() {

    lateinit var mDataBinding: DB

    @LayoutRes
    protected abstract fun setLayoutResId(): Int

    open fun onCreateFragment(savedInstanceState: Bundle?) {}

    protected abstract fun initView(savedInstanceState: Bundle?)


    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onCreateFragment(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mDataBinding = DataBindingUtil.inflate(inflater, setLayoutResId(), container, false)
        return mDataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    override fun onDestroy() {
        super.onDestroy()
        mDataBinding.unbind()
    }

    override fun onDetach() {
        super.onDetach()
    }
}
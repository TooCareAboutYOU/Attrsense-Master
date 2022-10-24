package com.attrsense.android.ui.main.my

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.attrsense.android.R
import com.attrsense.android.app.AttrSenseApplication
import com.attrsense.android.baselibrary.base.open.fragment.BaseDataBindingVMFragment
import com.attrsense.android.baselibrary.base.open.model.ResponseData
import com.attrsense.android.databinding.FragmentMainMyBinding
import com.attrsense.android.ui.about.AboutUsActivity
import com.attrsense.android.ui.apply.ApplyActivity
import com.attrsense.android.ui.contact.ContactUsActivity
import com.attrsense.android.ui.feedback.FeedbackActivity
import com.attrsense.android.ui.login.LoginActivity
import com.blankj.utilcode.util.ToastUtils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainMyFragment : BaseDataBindingVMFragment<FragmentMainMyBinding, MainMyViewModel>() {

    override fun setLayoutResId(): Int = R.layout.fragment_main_my

    override fun setViewModel(): Class<MainMyViewModel> = MainMyViewModel::class.java

    override fun onCreateFragment(savedInstanceState: Bundle?) {
        super.onCreateFragment(savedInstanceState)
    }

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        jumpActivity()
    }

    private fun jumpActivity() {
        mDataBinding.acTvGoFeedBack.setOnClickListener {
//            if (AttrSenseApplication.userManger.isLogin()) {
//                toActivity(FeedbackActivity::class.java)
//            }else{
//            ToastUtils.showShort("未登录！")
            toActivity(LoginActivity::class.java)
//            }
        }

        mDataBinding.acTvApply.setOnClickListener {
            toActivity(ApplyActivity::class.java)
        }

        mDataBinding.acTvContact.setOnClickListener {
            toActivity(ContactUsActivity::class.java)
        }

        mDataBinding.acTvAboutUs.setOnClickListener {
            toActivity(AboutUsActivity::class.java)
        }

        mDataBinding.acTvLogout.setOnClickListener {
            mViewModel.logout()
        }

        mViewModel.logoutLivedata.observe(this) {
            when (it) {
                is ResponseData.onFailed -> {
                    ToastUtils.showShort("退出异常！")
                    Log.e("printInfo", "MainMyFragment::jumpActivity: ${it.throwable}")
                }
                is ResponseData.onSuccess -> {
                    toActivity(LoginActivity::class.java)
                    activity?.finish()
                }
            }
        }
    }

    private fun toActivity(clz: Class<*>) {
        startActivity(Intent(activity, clz))
    }
}
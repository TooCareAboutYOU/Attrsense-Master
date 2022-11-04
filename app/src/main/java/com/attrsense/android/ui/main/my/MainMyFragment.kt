package com.attrsense.android.ui.main.my

import android.content.Intent
import android.os.*
import android.util.Log
import com.attrsense.android.R
import com.attrsense.android.baselibrary.base.open.fragment.BaseDataBindingVMFragment
import com.attrsense.android.baselibrary.base.open.model.ResponseData
import com.attrsense.android.databinding.FragmentMainMyBinding
import com.attrsense.android.ui.about.AboutUsActivity
import com.attrsense.android.ui.apply.ApplyActivity
import com.attrsense.android.ui.contact.ContactUsActivity
import com.attrsense.android.ui.feedback.FeedbackActivity
import com.attrsense.android.ui.login.LoginActivity
import com.attrsense.android.manager.UserDataManager
import com.attrsense.android.ui.main.MainActivity
import com.blankj.utilcode.util.ToastUtils
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainMyFragment : BaseDataBindingVMFragment<FragmentMainMyBinding, MainMyViewModel>() {

    @Inject
    lateinit var userManger: UserDataManager

    override fun setLayoutResId(): Int = R.layout.fragment_main_my

    override fun initView(savedInstanceState: Bundle?) {

        mDataBinding.acTvUserMobile.text = _mmkv.getString(UserDataManager.KEY_USER_MOBILE)

        jumpActivity()

        viewModel.logoutLivedata.observe(this) {
            when (it) {
                is ResponseData.onFailed -> {
                    ToastUtils.showShort("退出异常！")
                    Log.e("print_logs", "MainMyFragment::jumpActivity: ${it.throwable}")
                }
                is ResponseData.onSuccess -> {
                    toActivity(LoginActivity::class.java)
                    (requireActivity() as MainActivity).finish()
                }
            }
        }
    }

    private fun jumpActivity() {
        mDataBinding.acTvGoFeedBack.setOnClickListener {
            if (userManger.isLogin()) {
                toActivity(FeedbackActivity::class.java)
            } else {
                ToastUtils.showShort("未登录！")
                LoginActivity.jump(requireActivity())
            }
        }

        mDataBinding.acTvApply.setOnClickListener {
            if (userManger.isLogin()) {
                toActivity(ApplyActivity::class.java)
            } else {
                ToastUtils.showShort("未登录！")
                LoginActivity.jump(requireActivity())
            }
        }

        mDataBinding.acTvContact.setOnClickListener {
            toActivity(ContactUsActivity::class.java)
        }

        mDataBinding.acTvAboutUs.setOnClickListener {
            toActivity(AboutUsActivity::class.java)
        }

        mDataBinding.acTvLogout.setOnClickListener {
            viewModel.logout()
        }
    }

    private fun toActivity(clz: Class<*>) {
        startActivity(Intent(activity, clz))
    }
}
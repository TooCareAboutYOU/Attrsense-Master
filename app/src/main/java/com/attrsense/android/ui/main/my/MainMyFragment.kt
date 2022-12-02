package com.attrsense.android.ui.main.my

import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.attrsense.android.R
import com.attrsense.android.baselibrary.base.open.fragment.BaseDataBindingVMFragment
import com.attrsense.android.baselibrary.base.open.model.ResponseData
import com.attrsense.android.databinding.FragmentMainMyBinding
import com.attrsense.android.manager.UserDataManager
import com.attrsense.android.ui.about.AboutUsActivity
import com.attrsense.android.ui.apply.ApplyActivity
import com.attrsense.android.ui.contact.ContactUsActivity
import com.attrsense.android.ui.feedback.FeedbackActivity
import com.attrsense.android.ui.login.LoginActivity
import com.attrsense.android.ui.main.MainActivity
import com.attrsense.android.ui.statistics.StatisticsActivity
import com.attrsense.ui.library.expand.singleClick
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

        mViewModel.logoutLivedata.observe(this) {
            when (it) {
                is ResponseData.OnFailed -> {
                    showToast("退出异常！")
                    Log.e("print_logs", "MainMyFragment::jumpActivity: ${it.throwable.message}")
                }
                is ResponseData.OnSuccess -> {
                    toActivity(LoginActivity::class.java)
                    (requireActivity() as MainActivity).finish()
                }
            }
        }
    }

    private fun jumpActivity() {
        mDataBinding.acTvGoFeedBack.singleClick {
            if (userManger.isLogin()) {
                toActivity(FeedbackActivity::class.java)
            } else {
                showToast("未登录！")
                LoginActivity.jump(requireActivity())
            }
        }

        mDataBinding.acTvApply.singleClick {
            if (userManger.isLogin()) {
                toActivity(ApplyActivity::class.java)
            } else {
                showToast("未登录！")
                LoginActivity.jump(requireActivity())
            }
        }

        mDataBinding.acTvStatistics.singleClick {
            if (userManger.isLogin()) {
                toActivity(StatisticsActivity::class.java)
            } else {
                showToast("未登录！")
                LoginActivity.jump(requireActivity())
            }
        }

        mDataBinding.acTvContact.singleClick {
            toActivity(ContactUsActivity::class.java)
        }

        mDataBinding.acTvAboutUs.singleClick {
            toActivity(AboutUsActivity::class.java)
        }

        mDataBinding.acTvLogout.singleClick {
            mViewModel.logout()
        }
    }

    private fun toActivity(clz: Class<*>) {
        startActivity(Intent(activity, clz))
    }
}
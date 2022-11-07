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
import com.attrsense.android.ui.statistics.StatisticsActivity
import com.jakewharton.rxbinding4.view.clicks
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
                is ResponseData.onFailed -> {
                    showToast("退出异常！")
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
        mDataBinding.acTvGoFeedBack.clicks().subscribe {
            if (userManger.isLogin()) {
                toActivity(FeedbackActivity::class.java)
            } else {
                showToast("未登录！")
                LoginActivity.jump(requireActivity())
            }
        }

        mDataBinding.acTvApply.clicks().subscribe {
            if (userManger.isLogin()) {
                toActivity(ApplyActivity::class.java)
            } else {
                showToast("未登录！")
                LoginActivity.jump(requireActivity())
            }
        }

        mDataBinding.acTvStatistics.clicks().subscribe {
            if (userManger.isLogin()) {
                toActivity(StatisticsActivity::class.java)
            } else {
                showToast("未登录！")
                LoginActivity.jump(requireActivity())
            }
        }

        mDataBinding.acTvContact.clicks().subscribe {
            toActivity(ContactUsActivity::class.java)
        }

        mDataBinding.acTvAboutUs.clicks().subscribe {
            toActivity(AboutUsActivity::class.java)
        }

        mDataBinding.acTvLogout.clicks().subscribe {
            mViewModel.logout()
        }
    }

    private fun toActivity(clz: Class<*>) {
        startActivity(Intent(activity, clz))
    }
}
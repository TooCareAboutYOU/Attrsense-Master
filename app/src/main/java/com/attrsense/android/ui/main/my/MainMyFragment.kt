package com.attrsense.android.ui.main.my

import android.content.Context
import android.content.Intent
import android.os.*
import android.os.Handler.Callback
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
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
import com.example.snpetest.JniInterface
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainMyFragment : BaseDataBindingVMFragment<FragmentMainMyBinding, MainMyViewModel>() {

    @Inject
    lateinit var userManger: UserDataManager

    private val handler = Handler(Looper.myLooper()!!) {
        if (it.what == 100) {
            val result = it.obj
            Log.i("print_logs", "MainMyFragment:输出 $result")
            mDataBinding.acTvTestText.text = (result as CharSequence?).toString()
        }
        false
    }

    override fun setLayoutResId(): Int = R.layout.fragment_main_my

    override fun setViewModel(): Class<MainMyViewModel> = MainMyViewModel::class.java

    override fun onCreateFragment(savedInstanceState: Bundle?) {
        super.onCreateFragment(savedInstanceState)
    }

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)

        jumpActivity()

        mViewModel.logoutLivedata.observe(this) {
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

        mDataBinding.acImgUserIcon.setOnLongClickListener {
            mDataBinding.acEtNum.text = null
            mDataBinding.acTvTestText.text = null

            mDataBinding.llTest.visibility =
                if (mDataBinding.llTest.visibility == View.VISIBLE) {
                    View.GONE
                } else {
                    View.VISIBLE
                }
            false
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

        mDataBinding.acTvTest.setOnClickListener {

            val num = mDataBinding.acEtNum.text.toString().toInt()
            if (num == 0) {
                ToastUtils.showShort("次数不能为0！")
                return@setOnClickListener
            }

            mDataBinding.acTvTestText.text = ""

            val batterManager =
                requireActivity().getSystemService(Context.BATTERY_SERVICE) as BatteryManager
            val stringBuilder = StringBuilder()
            Thread {
                val anfPath = userManger.getAnf()
                val beforeLevel =
                    batterManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)
                Log.i("print_logs", "MainMyFragment::jumpActivity-1: $anfPath, 当前电量：$beforeLevel")
                stringBuilder.append("运行前电量：$beforeLevel").append("\n")

                val time = JniInterface.decodeCommitTimeTest(anfPath, num)
                val afterLevel =
                    batterManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)

                Log.i("print_logs", "MainMyFragment::jumpActivity-2: $time, 当前电量：$beforeLevel")
                stringBuilder.append("运行时长：$time").append("\n")
                stringBuilder.append("运行后电量：$afterLevel")

                handler.sendMessage(handler.obtainMessage(100, stringBuilder.toString()))
            }.start()
        }

        mDataBinding.acTvAboutUs.setOnClickListener {
            toActivity(AboutUsActivity::class.java)
        }

        mDataBinding.acTvLogout.setOnClickListener {
            mViewModel.logout()
        }
    }

    private fun toActivity(clz: Class<*>) {
        startActivity(Intent(activity, clz))
    }
}
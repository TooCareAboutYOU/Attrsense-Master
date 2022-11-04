package com.attrsense.android.ui.register

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.transition.Explode
import android.view.Window
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.attrsense.android.R
import com.attrsense.android.baselibrary.base.open.activity.BaseDataBindingVMActivity
import com.attrsense.android.baselibrary.base.open.model.ResponseData
import com.attrsense.android.databinding.ActivityRegisterBinding
import com.attrsense.android.ui.login.LoginActivity
import com.blankj.utilcode.util.ToastUtils
import com.jakewharton.rxbinding4.widget.textChanges
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.core.Observable

@AndroidEntryPoint
class RegisterActivity :
    BaseDataBindingVMActivity<ActivityRegisterBinding, RegisterViewModel>() {


    companion object {
        fun jump(activity: FragmentActivity) {
            activity.startActivity(
                Intent(activity, RegisterActivity::class.java),
                ActivityOptions.makeSceneTransitionAnimation(activity).toBundle()
            )
        }
    }

    override fun setLayoutResId(): Int = R.layout.activity_register

    override fun initViewBefore(savedInstanceState: Bundle?) {
        super.initViewBefore(savedInstanceState)
        window.apply {
            requestFeature(Window.FEATURE_CONTENT_TRANSITIONS)
            enterTransition = Explode()
        }
    }

    override fun initView(savedInstanceState: Bundle?) {
        mDataBinding.toolbar.load(this).apply {
            setCenterTitle("注\t\t册")
            setLeftClick {
                finish()
            }
        }

        mDataBinding.acEtMobile.requestFocus()

        val mobile: Observable<CharSequence> = mDataBinding.acEtMobile.textChanges().skipInitialValue()
        val code: Observable<CharSequence> = mDataBinding.acEtCode.textChanges().skipInitialValue()
        Observable.combineLatest(mobile, code) { t1, t2 ->
            !TextUtils.isEmpty(t1) && t1.length == 11 && !TextUtils.isEmpty(t2) && t2.length == 6
        }.subscribe {
            mDataBinding.acBtnRegister.isEnabled = it
            mDataBinding.acBtnRegister.setBackgroundColor(
                if (it) {
                    ContextCompat.getColor(this, R.color.color_4A90E2)
                } else {
                    ContextCompat.getColor(this, R.color.color_999999)
                }
            )
        }

        mDataBinding.acBtnRequestCode.setOnClickListener {
//            ToastUtils.showShort("验证码发送成功！")
            mDataBinding.acEtCode.setText("111111")
        }

        mDataBinding.acBtnRegister.setOnClickListener {
            viewModel.register(
                mDataBinding.acEtMobile.text.toString(),
                mDataBinding.acEtCode.text.toString()
            )
        }

        viewModel.registerLivedata.observe(this) {
            when (it) {
                is ResponseData.onFailed -> {
                    ToastUtils.showShort("注册失败！")
                }
                is ResponseData.onSuccess -> {
                    ToastUtils.showShort("注册成功!")
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                }
            }
        }
    }
}
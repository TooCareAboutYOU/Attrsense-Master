package com.attrsense.android.ui.login

import android.content.Intent
import android.text.TextUtils
import androidx.core.content.ContextCompat
import com.attrsense.android.R
import com.attrsense.android.baselibrary.base.open.activity.BaseDataBindingVMActivity
import com.attrsense.android.baselibrary.base.open.model.ResponseData
import com.attrsense.android.databinding.ActivityLoginBinding
import com.attrsense.android.ui.main.MainActivity
import com.attrsense.android.ui.register.RegisterActivity
import com.blankj.utilcode.util.ToastUtils
import com.jakewharton.rxbinding4.widget.textChanges
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.core.Observable

@AndroidEntryPoint
class LoginActivity : BaseDataBindingVMActivity<ActivityLoginBinding, LoginViewModel>() {

    override fun setLayoutResId(): Int = R.layout.activity_login

    override fun setViewModel(): Class<LoginViewModel> = LoginViewModel::class.java

    override fun initView() {
        super.initView()

        val mobile: Observable<CharSequence> =
            mDataBinding.acEtMobile.textChanges().skipInitialValue()
        val code: Observable<CharSequence> = mDataBinding.acEtCode.textChanges().skipInitialValue()
        Observable.combineLatest(mobile, code) { t1, t2 ->
            !TextUtils.isEmpty(t1) && t1.length == 11 && !TextUtils.isEmpty(t2) && t2.length == 6
        }.subscribe {
            mDataBinding.acBtnLogin.isClickable = it
            mDataBinding.acBtnLogin.setBackgroundColor(
                if (it) {
                    ContextCompat.getColor(this, R.color.color_4A90E2)
                } else {
                    ContextCompat.getColor(this, R.color.color_999999)
                }
            )
        }

        mDataBinding.acEtMobile.setText("18874701235")

        mDataBinding.acBtnRequestCode.setOnClickListener {
            ToastUtils.showShort("验证码发送成功！")
            mDataBinding.acEtCode.setText("111111")
        }

        mDataBinding.acBtnLogin.setOnClickListener {
            mViewModel.login(
                mDataBinding.acEtMobile.text.toString(),
                mDataBinding.acEtCode.text.toString()
            )
        }

        mViewModel.loginLivedata.observe(this) {
            when (it) {
                is ResponseData.onFailed -> {
                    ToastUtils.showShort(it.throwable.toString())
                }
                is ResponseData.onSuccess -> {
                    ToastUtils.showShort("登录成功!")
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }
            }
        }

        mDataBinding.acTvGoRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }
}
package com.attrsense.android.ui.register

import android.content.Intent
import android.content.res.Resources.Theme
import android.graphics.Color
import android.text.TextUtils
import androidx.core.content.ContextCompat
import com.attrsense.android.R
import com.attrsense.android.baselibrary.base.open.activity.BaseDataBindingVMActivity
import com.attrsense.android.baselibrary.base.open.model.ResponseData
import com.attrsense.android.databinding.ActivityRegisterBinding
import com.attrsense.android.ui.login.LoginActivity
import com.blankj.utilcode.util.ToastUtils
import com.jakewharton.rxbinding4.widget.textChanges
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.functions.BiFunction

//18874701235
@AndroidEntryPoint
class RegisterActivity :
    BaseDataBindingVMActivity<ActivityRegisterBinding, RegisterViewModel>() {

    override fun setLayoutResId(): Int = R.layout.activity_register

    override fun setViewModel(): Class<RegisterViewModel> = RegisterViewModel::class.java

    override fun initView() {
        super.initView()

        val mobile: Observable<CharSequence> =
            mDataBinding.acEtMobile.textChanges().skipInitialValue()
        val code: Observable<CharSequence> = mDataBinding.acEtCode.textChanges().skipInitialValue()
        Observable.combineLatest(mobile, code) { t1, t2 ->
            !TextUtils.isEmpty(t1) && t1.length == 11 && !TextUtils.isEmpty(t2) && t2.length == 6
        }.subscribe {
            mDataBinding.acBtnRegister.isClickable = it
            mDataBinding.acBtnRegister.setBackgroundColor(
                if (it) {
                    ContextCompat.getColor(this, R.color.color_4A90E2)
                } else {
                    ContextCompat.getColor(this, R.color.color_999999)
                }
            )
        }

        mDataBinding.acBtnRequestCode.setOnClickListener {
            ToastUtils.showShort("验证码发送成功！")
            mDataBinding.acEtCode.setText("111111")
        }

        mDataBinding.acBtnRegister.setOnClickListener {
            mViewModel.register(
                mDataBinding.acEtMobile.text.toString(),
                mDataBinding.acEtCode.text.toString()
            )
        }

        mViewModel.registerLivedata.observe(this) {
            when (it) {
                is ResponseData.onFailed -> {
                    ToastUtils.showShort(it.throwable.toString())
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
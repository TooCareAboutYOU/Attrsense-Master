package com.attrsense.android.ui.login

import android.app.Activity
import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.transition.Explode
import android.transition.Fade
import android.transition.Slide
import android.util.Log
import android.view.View
import android.view.Window
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.transition.Transition
import com.attrsense.android.R
import com.attrsense.android.baselibrary.base.open.activity.BaseDataBindingVMActivity
import com.attrsense.android.baselibrary.base.open.model.ResponseData
import com.attrsense.android.databinding.ActivityLoginBinding
import com.attrsense.android.ui.main.MainActivity
import com.attrsense.android.ui.register.RegisterActivity
import com.blankj.utilcode.util.ToastUtils
import com.google.android.material.transition.platform.*
import com.jakewharton.rxbinding4.widget.textChanges
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.core.Observable

@AndroidEntryPoint
class LoginActivity : BaseDataBindingVMActivity<ActivityLoginBinding, LoginViewModel>() {


    companion object {
        fun jump(activity: FragmentActivity) {
            activity.startActivity(
                Intent(activity, LoginActivity::class.java),
                ActivityOptions.makeSceneTransitionAnimation(activity).toBundle()
            )
        }
    }

    override fun setLayoutResId(): Int = R.layout.activity_login

    override fun setViewModel(): Class<LoginViewModel> = LoginViewModel::class.java

    override fun initViewBefore(savedInstanceState: Bundle?) {
        super.initViewBefore(savedInstanceState)
        window.apply {
            requestFeature(Window.FEATURE_CONTENT_TRANSITIONS)
            enterTransition = Explode()
//            enterTransition = Slide()
//            enterTransition = Fade()
//            enterTransition = Hold()
//            enterTransition = MaterialElevationScale(true)
//            enterTransition = MaterialFade()
//            enterTransition = MaterialFadeThrough()
//            enterTransition = MaterialSharedAxis(MaterialSharedAxis.Y, true)
        }
    }

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)

        val mobile: Observable<CharSequence> =
            mDataBinding.acEtMobile.textChanges().skipInitialValue()
        val code: Observable<CharSequence> = mDataBinding.acEtCode.textChanges().skipInitialValue()
        Observable.combineLatest(mobile, code) { t1, t2 ->
            !TextUtils.isEmpty(t1) && t1.length == 11 && !TextUtils.isEmpty(t2) && t2.length == 6
        }.subscribe {
            mDataBinding.acBtnLogin.isEnabled = it
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
            mDataBinding.acEtCode.setText("111111")
        }

        mDataBinding.acBtnLogin.setOnClickListener {
            mViewModel.login(
                mDataBinding.acEtMobile.text.toString(), mDataBinding.acEtCode.text.toString()
            )
        }

        mViewModel.loginLivedata.observe(this) {
            when (it) {
                is ResponseData.onFailed -> {
                    ToastUtils.showShort("登录失败！${it.throwable}")
                }
                is ResponseData.onSuccess -> {
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }
            }
        }

        mDataBinding.acTvGoRegister.setOnClickListener {
            RegisterActivity.jump(this)
        }
    }
}
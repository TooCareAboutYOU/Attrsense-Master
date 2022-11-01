package com.attrsense.android.ui.apply

import android.os.Bundle
import android.util.Log
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import com.attrsense.android.R
import com.attrsense.android.baselibrary.base.open.activity.BaseDataBindingVMActivity
import com.attrsense.android.baselibrary.base.open.model.ResponseData
import com.attrsense.android.databinding.ActivityApplyBinding
import com.blankj.utilcode.util.ToastUtils
import com.jakewharton.rxbinding4.widget.textChanges
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.core.Observable

/**
 * 申请SDK内测
 */
@AndroidEntryPoint
class ApplyActivity : BaseDataBindingVMActivity<ActivityApplyBinding, ApplyViewModel>() {


    override fun setLayoutResId(): Int = R.layout.activity_apply

    override fun setViewModel(): Class<ApplyViewModel> = ApplyViewModel::class.java

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        mDataBinding.toolBarView.load(this).apply {
            this.setCenterTitle(R.string.tab_main_user_apply_title)
            this.setLeftClick {
                this@ApplyActivity.finish()
            }
        }


        mDataBinding.acTvContact.text = getContent(R.string.tab_main_user_apply_contact)
        mDataBinding.acTvPhone.text = getContent(R.string.tab_main_user_apply_phone)
        mDataBinding.acTvEmail.text = getContent(R.string.tab_main_user_apply_email)
        mDataBinding.acTvCompany.text = getContent(R.string.tab_main_user_apply_company)
        mDataBinding.acTvDescription.text = getContent(R.string.tab_main_user_apply_description)

        textListener()

        mDataBinding.acBtnCommit.setOnClickListener {
            mViewModel.apply(
                mDataBinding.acEtContact.text.toString(),
                mDataBinding.acEtPhone.text.toString(),
                mDataBinding.acEtCompany.text.toString(),
                mDataBinding.acEtEmail.text.toString(),
                mDataBinding.acEtDescription.text.toString()
            )
        }

        mViewModel.applyLiveData.observe(this) {
            when (it) {
                is ResponseData.onFailed -> {
                    ToastUtils.showShort("提交失败：${it.throwable}")
                }
                is ResponseData.onSuccess -> {
                    mDataBinding.acEtContact.text = null
                    mDataBinding.acEtPhone.text = null
                    mDataBinding.acEtCompany.text = null
                    mDataBinding.acEtEmail.text = null
                    mDataBinding.acEtDescription.text = null
                    ToastUtils.showShort("提交成功！")
                }
            }
        }
    }

    private fun textListener() {
        val contactObservable: Observable<CharSequence> =
            mDataBinding.acEtContact.textChanges().skipInitialValue()
        val phoneObservable: Observable<CharSequence> =
            mDataBinding.acEtPhone.textChanges().skipInitialValue()
        val emailObservable: Observable<CharSequence> =
            mDataBinding.acEtEmail.textChanges().skipInitialValue()
        val companyObservable: Observable<CharSequence> =
            mDataBinding.acEtCompany.textChanges().skipInitialValue()

        Observable.combineLatest(
            contactObservable,
            phoneObservable,
            emailObservable,
            companyObservable
        ) { t1, t2, t3, t4 ->
            t1.isNotEmpty() &&
                    t2.isNotEmpty() &&
                    t2.length == 11 &&
                    t3.isNotEmpty() &&
                    t4.isNotEmpty()
        }.subscribe {
            mDataBinding.acBtnCommit.isEnabled = it
        }

    }

    private fun getContent(@StringRes resId: Int): String {
        return HtmlCompat.fromHtml(getString(resId), HtmlCompat.FROM_HTML_MODE_LEGACY).toString()
    }
}
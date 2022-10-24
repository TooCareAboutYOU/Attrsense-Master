package com.attrsense.android.ui.feedback

import com.attrsense.android.R
import com.attrsense.android.baselibrary.base.open.activity.BaseDataBindingVMActivity
import com.attrsense.android.databinding.ActivityFeedbackBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 * 意见反馈
 */
@AndroidEntryPoint
class FeedbackActivity : BaseDataBindingVMActivity<ActivityFeedbackBinding, FeedbackViewModel>() {

    override fun setLayoutResId(): Int = R.layout.activity_feedback

    override fun setViewModel(): Class<FeedbackViewModel> = FeedbackViewModel::class.java

    override fun initView() {
        super.initView()
    }
}
package com.attrsense.android.ui.statistics

import android.os.Bundle
import android.util.Log
import com.attrsense.android.R
import com.attrsense.android.baselibrary.base.open.activity.BaseDataBindingVMActivity
import com.attrsense.android.baselibrary.base.open.model.ResponseData
import com.attrsense.android.databinding.ActivityStatisticsBinding
import com.blankj.utilcode.util.ConvertUtils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StatisticsActivity :
    BaseDataBindingVMActivity<ActivityStatisticsBinding, StatisticsViewModel>() {

    override fun setLayoutResId(): Int = R.layout.activity_statistics

    override fun initView(savedInstanceState: Bundle?) {
        mDataBinding.toolBarView.load(this).apply {
            this.setLeftClick { finish() }
            this.setCenterTitle("统计数据")
        }

        mViewModel.getLocalData()
        mViewModel.getRemoteData()

        mViewModel.localLiveData.observe(this) {
            when (it) {
                is ResponseData.onFailed -> {

                }
                is ResponseData.onSuccess -> {
                    it.value?.let { entity ->
                        mDataBinding.acTvLocalOriginalSizeNum.text =
                            ConvertUtils.byte2FitMemorySize(entity.originalAllSize.toLong())
                        mDataBinding.acTvLocalAnfSizeNum.text =
                            ConvertUtils.byte2FitMemorySize(entity.anfAllSize.toLong())
                        mDataBinding.acTvLocalCountNum.text = entity.count.toString()
                    }
                }
            }
        }

        mViewModel.remoteLiveData.observe(this) {
            when (it) {
                is ResponseData.onFailed -> {

                }
                is ResponseData.onSuccess -> {
                    it.value?.let { entity ->
                        mDataBinding.acTvRemoteOriginalSizeNum.text =
                            ConvertUtils.byte2FitMemorySize(entity.totalSrcImageSize.toLong())
                        mDataBinding.acTvRemoteAnfSizeNum.text =
                            ConvertUtils.byte2FitMemorySize(entity.totalImageSize.toLong())
                        mDataBinding.acTvRemoteCountNum.text = entity.imageCount.toString()
                    }
                }
            }
        }
    }
}
package com.attrsense.android.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.annotation.ColorInt
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import com.attrsense.android.R
import com.attrsense.android.databinding.LayoutToolBarBinding

/**
 * @author zhangshuai
 * @date 2022/10/26 18:08
 * @description
 */
class AttrToolBarView constructor(
    context: Context, attrs: AttributeSet?
) : Toolbar(context, attrs) {


    private var mBinding: LayoutToolBarBinding


    init {
        mBinding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.layout_tool_bar,
            this,
            true
        )
        this.setContentInsetsRelative(0, 0)
    }

    /**
     * 初始化View控件
     */
    fun load(activity: AppCompatActivity): AttrToolBarView {
        activity.setSupportActionBar(mBinding.toolbar)
        return this
    }


    /**
     * 设置ToolBar背景色
     */
    fun setBgColor(@ColorInt colorId: Int) {
        this.setBackgroundColor(colorId)
    }

    /**
     * 设置标题
     * @param title String
     */
    fun setCenterTitle(title: String) {
        mBinding.acTvTitle.text = title
    }

    /**
     * 设置标题
     * @param titleRes Int
     */
    fun setCenterTitle(@StringRes titleRes: Int) {
        this.setCenterTitle(context.resources.getString(titleRes))
    }

    /**
     * 设置监听
     */
    fun setLeftClick(block: () -> Unit) {
        mBinding.acIvBack.setOnClickListener { block() }
    }
}

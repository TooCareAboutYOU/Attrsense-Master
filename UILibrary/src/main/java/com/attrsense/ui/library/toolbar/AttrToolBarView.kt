package com.attrsense.ui.library.toolbar

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.ColorInt
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import com.attrsense.ui.library.BuildConfig
import com.attrsense.ui.library.R
import com.attrsense.ui.library.databinding.LayoutToolBarBinding
import com.attrsense.ui.library.expand.singleClick

/**
 * @author zhangshuai@attrsense.com
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
     * @param activity Activity
     * @return AttrToolBarView
     */
    fun load(activity: Activity): AttrToolBarView {
        when (activity) {
            is AppCompatActivity -> {
                activity.setSupportActionBar(mBinding.toolbar)
            }
            is FragmentActivity -> {
                (activity as AppCompatActivity).apply {
                    setSupportActionBar(mBinding.toolbar)
                }
            }
            else -> {}
        }
        return this
    }


    /**
     * ---------------------------------------------------------------------------------------------
     *                                          左边区域
     * ---------------------------------------------------------------------------------------------
     * @description：
     */

    /**
     * 隐藏左侧按钮
     * @param isVisibility Boolean
     * @return AppCompatImageView
     */
    fun hideLeftIcon(isVisibility: Boolean = false): AppCompatImageView {
        mBinding.acIvLeft.visibility = if (isVisibility) View.VISIBLE else View.GONE
        return mBinding.acIvLeft
    }

    /**
     * 设置左边按钮图标
     * @param res Any
     * @return AppCompatImageView
     */
    fun setLeftIcon(res: Any): AppCompatImageView {
        when (res) {
            is Drawable -> {
                mBinding.acIvLeft.setImageDrawable(res)
            }
            is Uri -> {
                mBinding.acIvLeft.setImageURI(res)
            }
            //@DrawableRes
            is Int -> {
                mBinding.acIvLeft.setImageResource(res)
            }
            is Bitmap -> {
                mBinding.acIvLeft.setImageBitmap(res)
            }
            else -> {
                if (BuildConfig.DEBUG) {
                    throw IllegalArgumentException("The leftIcon 'res' type mismatch")
                } else {
                    Log.e("print_logs", "The leftIcon 'res' type mismatch")
                }
            }
        }

        return mBinding.acIvLeft
    }

    /**
     * 设置监听
     * @param block Function0<Unit>
     * @return AppCompatImageView
     */
    fun setLeftClick(block: () -> Unit): AppCompatImageView {
        mBinding.acIvLeft.singleClick {
            block()
        }
        return this.mBinding.acIvLeft
    }

    /**
     * ---------------------------------------------------------------------------------------------
     *                                          父View
     * ---------------------------------------------------------------------------------------------
     * @description：
     */

    /**
     * 设置ToolBar背景色
     */
    fun setBgColor(@ColorInt colorId: Int) {
        this.setBackgroundColor(colorId)
    }

    /**
     * ---------------------------------------------------------------------------------------------
     *                                          中间文字区域
     * ---------------------------------------------------------------------------------------------
     * @description：
     */
    /**
     * 设置标题
     * @param res Any
     * @return AppCompatTextView
     */
    fun setCenterTitle(res: Any): AppCompatTextView {
        when (res) {
            is Int -> {
                mBinding.acTvTitle.text = context.resources.getString(res)
            }
            is String -> {
                mBinding.acTvTitle.text = res
            }
            else -> {
                if (BuildConfig.DEBUG) {
                    throw IllegalArgumentException("The title 'res' type mismatch")
                } else {
                    Log.e("print_logs", "The title 'res' type mismatch")
                }
            }
        }
        return this.mBinding.acTvTitle
    }

    /**
     * ---------------------------------------------------------------------------------------------
     *                                          右边区域
     * ---------------------------------------------------------------------------------------------
     * @description：
     */

    /**
     * 隐藏右侧按钮
     * @param isVisibility Boolean
     * @return AppCompatImageView
     */
    fun hideRightIcon(isVisibility: Boolean = false): AppCompatImageView {
        mBinding.acIvRight.visibility = if (isVisibility) View.VISIBLE else View.GONE
        return mBinding.acIvRight
    }

    /**
     * 设置左边按钮图标
     * @param res Any
     * @return AppCompatImageView
     */
    fun setRightIcon(res: Any): AppCompatImageView {
        when (res) {
            is Drawable -> {
                mBinding.acIvRight.setImageDrawable(res)
            }
            is Uri -> {
                mBinding.acIvRight.setImageURI(res)
            }
            //@DrawableRes
            is Int -> {
                mBinding.acIvRight.setImageResource(res)
            }
            is Bitmap -> {
                mBinding.acIvRight.setImageBitmap(res)
            }
            else -> {
                if (BuildConfig.DEBUG) {
                    throw IllegalArgumentException("The rightIcon 'res' type mismatch")
                } else {
                    Log.e("print_logs", "The rightIcon 'res' type mismatch")
                }
            }
        }

        return this.mBinding.acIvRight
    }

    /**
     * 设置监听
     * @param block Function0<Unit>
     * @return AppCompatImageView
     */
    fun setRightClick(block: () -> Unit): AppCompatImageView {
        mBinding.acIvRight.singleClick {
            block()
        }
        return this.mBinding.acIvRight
    }
}

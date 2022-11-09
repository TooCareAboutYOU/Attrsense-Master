package com.attrsense.ui.library.dialog

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.graphics.drawable.ColorDrawable
import android.text.TextUtils
import android.view.ViewGroup.LayoutParams
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.attrsense.ui.library.R

/**
 * @author zhangshuai
 * @date 2022/10/26 14:04
 * @description
 */
class LoadingDialog : AlertDialog {

    /**
     * 加载框类型
     */
    object LoadingDialogType {
        //默认
        const val DEFAULT = 0

        //只显示加载圈
        const val LOADING_CIRCLE = 1
    }

    private var clRootView: ConstraintLayout? = null

    //    var img: AppCompatImageView? = null
    //提示语文案
    var textStatus: AppCompatTextView? = null

    //背景
//    var mRrRoot: RelativeLayout? = null
//    private var mViewBg: View? = null

    //提示文案
    var textHint: String? = null

    //加载框类型
    private var loadingDialogType = 0
    private var mDialogWidth = LayoutParams.MATCH_PARENT
    private var mDialogHeight = LayoutParams.MATCH_PARENT
    private var mLoadingSize = -1
    private var mLoadingResId = -1
    private var mTextColorResId = -1
    private var mTextSize = -1

//    private val lifecycleOwner by lazy{
//        when (context) {
//            is RxFa -> {}
//            else -> {}
//        }
//    }

    constructor(context: Context) : super(context) {
        showDialog()
    }

    /**
     * 构造方法
     *
     * @param context  上下文
     * @param textHint 提示文案
     */
    constructor(context: Context, textHint: String?) : super(context) {
        this.textHint = textHint
        showDialog()

    }

    /**
     * 构造方法
     *
     * @param context           上下文
     * @param loadingDialogType 加载框类型
     */
    constructor(context: Context, loadingDialogType: Int) : super(context) {
        this.loadingDialogType = loadingDialogType
        showDialog()
    }

    /**
     * 构造方法
     *
     * @param context      上下文
     * @param textHint     提示文案
     * @param dialogSize   弹框大小（dp）
     * @param loadingSize  icon大小 （dp）
     * @param loadingResId icon资源id
     * @param textSize     文字size
     * @param textColor    文字颜色（id）
     */
    constructor(
        context: Context,
        textHint: String?,
        dialogSize: Int,
        loadingSize: Int,
        loadingResId: Int,
        textSize: Int,
        textColor: Int
    ) : super(context) {
        this.textHint = textHint
        mDialogWidth = dialogSize
        mDialogHeight = dialogSize
        mLoadingSize = loadingSize
        mLoadingResId = loadingResId
        mTextSize = textSize
        mTextColorResId = textColor
        showDialog()
    }

    private fun showDialog() {
        if (!isActivityRunning(context) && !isShowing) {
            return
        }
        show()
        setContentView(R.layout.loading_dialog_view)
        clRootView = findViewById(R.id.cl_root)
        textStatus = findViewById(R.id.loading_text_status)
//        img = findViewById(R.id.loading_img)
//        mRrRoot = findViewById(R.id.rr_root)
//        mViewBg = findViewById(R.id.view_bg)
//        if (mLoadingResId == -1) {
//            img!!.setImageResource(R.mipmap.leak_canary_icon)
//        } else {
//            img!!.setImageResource(mLoadingResId)
//            if (img!!.drawable != null) {
//                try {
//                    val animationDrawable = img!!.drawable as AnimationDrawable
//                    animationDrawable.isOneShot = false
//                    animationDrawable.start()
//                } catch (e: Exception) {
//                    e.printStackTrace()
//                }
//            }
//        }
        if (!TextUtils.isEmpty(textHint)) {
            textStatus!!.text = textHint
        }
//        if (mLoadingSize > 0) {
//            val imgParams = img!!.layoutParams
//            imgParams.width = SizeUtils.dp2px(mLoadingSize.toFloat())
//            imgParams.height = SizeUtils.dp2px(mLoadingSize.toFloat())
//            img!!.layoutParams = imgParams
//        }
//        if (mTextColorResId > 0) {
//            textStatus!!.setTextColor(
//                context.resources
//                    .getColor(mTextColorResId)
//            )
//        }
//        if (mTextSize > 0) {
//            textStatus!!.setTextSize(TypedValue.COMPLEX_UNIT_SP, mTextSize.toFloat())
//        }
//        when (loadingDialogType) {
//            LoadingDialogType.LOADING_CIRCLE -> {
//                mViewBg!!.visibility = View.GONE
//                textStatus!!.visibility = View.GONE
//            }
//            else -> {}
//        }
        window!!.setBackgroundDrawable(ColorDrawable(0))
        window!!.setDimAmount(0f)
        setCanceledOnTouchOutside(false)
        window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        )


//        val bgParams = mRrRoot!!.layoutParams
//        bgParams.width = SizeUtils.dp2px(mDialogWidth.toFloat())
//        bgParams.height = SizeUtils.dp2px(mDialogHeight.toFloat())
//        mRrRoot!!.layoutParams = bgParams
    }

//    override fun onWindowFocusChanged(hasFocus: Boolean) {
//        super.onWindowFocusChanged(hasFocus)
//        try {
//            val animationDrawable = img!!.drawable as AnimationDrawable
//            animationDrawable.start()
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//    }

    /**
     * dialog 的 context 是否在运行
     */
    private fun isActivityRunning(context: Context?): Boolean {
        if (context != null) {
            if (context is Activity) {
                if (!context.isFinishing) {
                    return true
                }
            } else {
                if (context is ContextWrapper) {
                    val context2 = context.baseContext
                    if (context2 != null && context2 is Activity) {
                        if (!context2.isFinishing) {
                            return true
                        }
                    }
                }
            }
        }
        return false
    }
}

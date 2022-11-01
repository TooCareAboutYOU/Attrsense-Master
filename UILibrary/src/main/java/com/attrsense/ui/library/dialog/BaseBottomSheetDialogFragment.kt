package com.attrsense.ui.library.dialog

import android.content.Context
import android.graphics.Color
import android.graphics.Point
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.FrameLayout
import androidx.fragment.app.FragmentManager
import com.attrsense.ui.library.R
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

/**
 * author : zhangshuai@attrsense.com
 * date : 2022/10/19 14:10
 * mark : custom something
 */
open class BaseBottomSheetDialogFragment : BottomSheetDialogFragment() {

    override fun onStart() {
        super.onStart()
        isCancelable = false

        dialog?.let {
            // 点击外面允许取消
            it.setCanceledOnTouchOutside(true)
            it.window?.apply {
                try {
                    setGravity(Gravity.TOP)
                    val bottomSheetDialog = dialog as BottomSheetDialog
                    val view =
                        bottomSheetDialog.window!!.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
                    val layoutParams = view.layoutParams
                    layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
                    view.layoutParams = layoutParams
                    view.setBackgroundResource(R.color.transparent) //R.color.translucent
                    BottomSheetBehavior.from(view).peekHeight = getScreenHeight()
                } catch (e: Exception) {
                    e.printStackTrace()
                    println("打印异常：$e")

                    val mContentView =
                        decorView.findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)
                    mContentView.setBackgroundResource(R.color.translucent)
                    val originLayoutParams = mContentView.layoutParams;
                    originLayoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
                    mContentView.layoutParams = originLayoutParams
                    BottomSheetBehavior.from(mContentView).apply {
                        state = BottomSheetBehavior.STATE_EXPANDED
                        addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                            override fun onStateChanged(bottomSheet: View, newState: Int) {
                                state = BottomSheetBehavior.STATE_EXPANDED
                            }

                            override fun onSlide(bottomSheet: View, slideOffset: Float) {}
                        })
                    }
                }
            }
        }
    }

    override fun show(manager: FragmentManager, tag: String?) {
        super.show(manager, tag)
        dialog?.setCanceledOnTouchOutside(true)
    }

    /**
     * 得到屏幕的高
     *
     * @param context
     * @return
     */
    fun getScreenHeight(): Int {
        context?.let {
            val wm: WindowManager = it.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            val point = Point()
            wm.defaultDisplay.getRealSize(point)
            return point.y
        }
        return -1
    }
}
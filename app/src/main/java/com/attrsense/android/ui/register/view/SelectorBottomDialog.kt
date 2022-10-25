package com.attrsense.android.ui.register.view

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.attrsense.android.R
import com.attrsense.android.databinding.DialogSelectorBottomBinding
import com.attrsense.android.widget.BaseBottomSheetDialogFragment
import com.blankj.utilcode.util.ToastUtils
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity
import com.yuyh.library.imgsel.ISNav
import com.yuyh.library.imgsel.config.ISCameraConfig
import com.yuyh.library.imgsel.config.ISListConfig

/**
 * author : zhangshuai@attrsense.com
 * date : 2022/10/19 14:15
 * mark : custom something
 *
用于自定义
override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
return BottomSheetDialog(_activity)
}
 */


class SelectorBottomDialog() : BaseBottomSheetDialogFragment() {

    private var mActivity: FragmentActivity? = null
    private var mFragment: Fragment? = null
    private var mCallback: onClickEventListener? = null
    private var maxCount: Int? = 9

    constructor(
        fragment: Fragment? = null,
        maxCount: Int? = 9,
        callback: onClickEventListener? = null
    ) : this() {
        this.mFragment = fragment
        this.maxCount = maxCount
        this.mCallback = callback
    }

    constructor(
        activity: FragmentActivity? = null,
        maxCount: Int? = 9,
        callback: onClickEventListener? = null
    ) : this() {
        this.mActivity = activity
        this.mCallback = callback
        this.maxCount = maxCount
    }

    /**
     * 图片返回到上层业务
     */
    interface onClickEventListener {
        fun onClickPhotos()
        fun onClickCamera()
        fun onClickFolder()
    }

    private lateinit var mBinding: DialogSelectorBottomBinding

    companion object {
        //拍照返回值
        const val CAMERA_REQUEST_CODE = 10
        const val SELECTOR_REQUEST_CODE = 100

        fun show(
            fragment: Fragment,
            maxCount: Int? = 9,
            callback: onClickEventListener? = null
        ) {
            SelectorBottomDialog(fragment, maxCount, callback).show(
                fragment.parentFragmentManager,//.supportFragmentManager,
                SelectorBottomDialog::class.java.simpleName
            )
        }

        fun show(
            activity: FragmentActivity,
            maxCount: Int? = 9,
            callback: onClickEventListener? = null
        ) {
            SelectorBottomDialog(activity, maxCount, callback).show(
                activity.supportFragmentManager,
                SelectorBottomDialog::class.java.simpleName
            )
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = DialogSelectorBottomBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mBinding.acTvToPhoto.setOnClickListener {
            val config = ISListConfig.Builder()
                // 是否多选, 默认true
                .multiSelect(true)
                // 是否记住上次选中记录, 仅当multiSelect为true的时候配置，默认为true
                .rememberSelected(false)
                // “确定”按钮背景色
                .btnBgColor(Color.GRAY)
                // “确定”按钮文字颜色
                .btnTextColor(Color.BLUE)
                // 使用沉浸式状态栏
                .statusBarColor(Color.parseColor("#3F51B5"))
                // 返回图标ResId
                .backResId(androidx.appcompat.R.drawable.abc_ic_ab_back_material) // 标题
                .title("图片")
                // 标题文字颜色
                .titleColor(Color.WHITE)
                // TitleBar背景色
                .titleBgColor(Color.parseColor("#3F51B5"))
                // 裁剪大小。needCrop为true的时候配置
//                .cropSize(1, 1, 200, 200)
//                .needCrop(false)
                // 第一个是否显示相机，默认true
                .needCamera(false)
                // 最大选择图片数量，默认9
                .maxNum(this.maxCount!!)
                .build()
            if (mActivity != null) {
                ISNav.getInstance().toListActivity(mActivity, config, SELECTOR_REQUEST_CODE)
            } else {
                ISNav.getInstance().toListActivity(mFragment, config, SELECTOR_REQUEST_CODE)
            }

            mCallback?.onClickPhotos()
            this.dismiss()
        }

        mBinding.acTvToCamera.setOnClickListener {
            val config = ISCameraConfig.Builder().build()
            if (mActivity != null) {
                ISNav.getInstance().toCameraActivity(mActivity, config, CAMERA_REQUEST_CODE)
            } else {
                ISNav.getInstance().toCameraActivity(mFragment, config, CAMERA_REQUEST_CODE)
            }

            mCallback?.onClickCamera()
            this.dismiss()
        }

        mBinding.acTvToFolder.setOnClickListener {
            mCallback?.onClickFolder()
            this.dismiss()
        }

        mBinding.acTvCancel.setOnClickListener {
            this.dismiss()
        }
    }
}
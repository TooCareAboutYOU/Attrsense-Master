package com.attrsense.android.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import com.attrsense.android.R
import com.attrsense.android.databinding.DialogSelectorBottomBinding
import com.attrsense.android.widget.BaseBottomSheetDialogFragment
import com.blankj.utilcode.util.ToastUtils
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity
import com.yuyh.library.imgsel.ISNav
import com.yuyh.library.imgsel.config.ISCameraConfig

/**
 * author : zhangshuai@attrsense.com
 * date : 2022/10/19 14:15
 * mark : custom something
 */
class SelectorBottomDialog constructor(private val callback: onClickEventListener? = null) :
    BaseBottomSheetDialogFragment() {

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
        fun show(
            activity: FragmentActivity,
            callback: onClickEventListener? = null
        ) {
            SelectorBottomDialog(callback).show(
                activity.supportFragmentManager,
                SelectorBottomDialog::class.java.simpleName
            )
        }
    }

    //用于自定义
//    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
//        return BottomSheetDialog(_activity)
//    }

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
            callback?.onClickPhotos()
            this.dismiss()
        }

        mBinding.acTvToCamera.setOnClickListener {
            callback?.onClickCamera()
            this.dismiss()
        }

        mBinding.acTvToFolder.setOnClickListener {
            callback?.onClickFolder()
            this.dismiss()
        }

        mBinding.acTvCancel.setOnClickListener {
            this.dismiss()
        }
    }
}
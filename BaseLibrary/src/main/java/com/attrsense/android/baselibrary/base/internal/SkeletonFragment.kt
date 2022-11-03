package com.attrsense.android.baselibrary.base.internal

import android.content.Context
import android.os.Bundle
import android.view.View
import com.attrsense.android.baselibrary.base.open.viewmodel.LoadViewImpl
import com.attrsense.android.baselibrary.util.MMKVUtils
import com.attrsense.ui.library.dialog.LoadingDialog
import com.tbruyelle.rxpermissions3.RxPermissions
import com.trello.rxlifecycle2.components.support.RxFragment
import io.reactivex.rxjava3.disposables.Disposable
import javax.inject.Inject

/**
 * author : zhangshuai@attrsense.com
 * date : 2022/10/8 10:07
 * mark : custom something
 */
open class SkeletonFragment : RxFragment(), LoadViewImpl {

    //也可自定义存储类型
//    @Inject
//    lateinit var mmkv:MMKV

    @Inject
    lateinit var _mmkv: MMKVUtils


    protected lateinit var rxPermissions: RxPermissions

    private var loadingDialog: LoadingDialog? = null


    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        rxPermissions = RxPermissions(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        hideLoadingDialog()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onDetach() {
        super.onDetach()
    }



    /**
     * 自定义函数
     */
    //手动添加指定Disposable
    fun addDisposable(disposable: Disposable) {
        (requireActivity() as SkeletonActivity).addDisposable(disposable)
    }

    //手动移除指定Disposable
    fun removeDisposable(disposable: Disposable) {
        (requireActivity() as SkeletonActivity).removeDisposable(disposable)
    }

    override fun showLoadingDialog(text: String) {
        try {
            if (!requireActivity().isFinishing && (loadingDialog == null || !loadingDialog?.isShowing!!)) {
                loadingDialog = LoadingDialog(requireActivity(), text)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun hideLoadingDialog() {
        try {
            if (!requireActivity().isFinishing && !requireActivity().isDestroyed && loadingDialog != null && loadingDialog?.isShowing!!) {
                loadingDialog?.dismiss()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}
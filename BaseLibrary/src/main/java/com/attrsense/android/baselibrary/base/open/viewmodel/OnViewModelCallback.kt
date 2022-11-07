package com.attrsense.android.baselibrary.base.open.viewmodel

import io.reactivex.rxjava3.disposables.Disposable

/**
 * @author zhangshuai
 * @date 2022/11/3 18:13
 * @description 访问网络时显示加载Dialog视图
 */
interface OnViewModelCallback {

    fun showLoadingDialog(text: String = "")

    fun dismissLoadingDialog()

    fun addDisposable(disposable: Disposable)

    fun removeDisposable(disposable: Disposable)

    fun showToast(text: String = "", isLong: Boolean = false)
}
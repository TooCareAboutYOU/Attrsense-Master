package com.attrsense.android.baselibrary.base.open.viewmodel

/**
 * @author zhangshuai
 * @date 2022/11/3 18:13
 * @description 访问网络时显示加载Dialog视图
 */
interface LoadViewImpl {

    fun showLoadingDialog(text: String = "")

    fun hideLoadingDialog()
}
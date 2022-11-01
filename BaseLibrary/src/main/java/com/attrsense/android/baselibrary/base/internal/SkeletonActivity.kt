package com.attrsense.android.baselibrary.base.internal

import android.os.Bundle
import com.attrsense.android.baselibrary.util.ActivityManager
import com.attrsense.android.baselibrary.util.MMKVUtils
import com.attrsense.android.baselibrary.view.LoadingDialog
import com.blankj.utilcode.util.ActivityUtils
import com.tbruyelle.rxpermissions3.RxPermissions
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import javax.inject.Inject

/**
 * author : zhangshuai@attrsense.com
 * date : 2022/10/8
 * mark : custom something
 */
open class SkeletonActivity : RxAppCompatActivity() {

    //也可自定义存储类型
//    @Inject
//    lateinit var mmkv:MMKV

    @Inject
    lateinit var _mmkv: MMKVUtils


    private var loadingDialog: LoadingDialog? = null

    private val mDisposables: CompositeDisposable = CompositeDisposable()

    protected lateinit var rxPermissions: RxPermissions


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityManager.getInstance().add(this)
        rxPermissions = RxPermissions(this)
        //临时监听网络状态
//        val mDisposable = ReactiveNetwork.observeNetworkConnectivity(this)
//            .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribe {
//                Logger.i("网络状态：${it.state()}")
//            }
//        mDisposables.add(mDisposable)
    }

    override fun onRestart() {
        super.onRestart()
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

    override fun onDestroy() {
        super.onDestroy()
        mDisposables.dispose()
        hideLoadingDialog()
        ActivityManager.getInstance().remove(this)
    }

    /**
     * 自定义函数
     */
    //手动添加指定Disposable
    fun addDisposable(disposable: Disposable) {
        mDisposables.add(disposable)
    }

    //手动移除指定Disposable
    fun removeDisposable(disposable: Disposable) {
        mDisposables.remove(disposable)
    }

    fun showLoadingDialog() {
        if (!isFinishing && (loadingDialog == null || !loadingDialog?.isShowing!!)) {
            loadingDialog = LoadingDialog(this)
        }
    }

    fun hideLoadingDialog() {
        if (!isFinishing && !isDestroyed && loadingDialog != null && loadingDialog?.isShowing!!) {
            loadingDialog?.dismiss()
        }
    }
}
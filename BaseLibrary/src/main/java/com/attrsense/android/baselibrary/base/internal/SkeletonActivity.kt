package com.attrsense.android.baselibrary.base.internal

import android.os.Bundle
import android.util.Log
import com.attrsense.android.baselibrary.base.open.viewmodel.LoadViewImpl
import com.attrsense.android.baselibrary.util.MMKVUtils
import com.attrsense.ui.library.dialog.LoadingDialog
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
open class SkeletonActivity : RxAppCompatActivity(), LoadViewImpl {

    @Inject
    lateinit var _mmkv: MMKVUtils


    private var loadingDialog: LoadingDialog? = null

    private val mDisposables: CompositeDisposable = CompositeDisposable()

    protected lateinit var rxPermissions: RxPermissions

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 经测试在代码里直接声明透明状态栏更有效
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            window.statusBarColor = Color.TRANSPARENT
//        }
        rxPermissions = RxPermissions(this)


        //临时监听网络状态
//        val mDisposable = ReactiveNetwork.observeNetworkConnectivity(this)
//            .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribe {
//                Logger.i("网络状态：${it.state()}")
//            }
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

    }

    //手动调用finish时，事件处理优先于生命周期
    override fun finish() {
        //需要提前从window移除Dialog
        hideLoadingDialog()
        super.finish()
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

    override fun showLoadingDialog(text: String) {
        if (!isFinishing && (loadingDialog == null || !loadingDialog?.isShowing!!)) {
            loadingDialog = LoadingDialog(this)
        }
    }

    override fun hideLoadingDialog() {
        if (!isFinishing && !isDestroyed && loadingDialog != null && loadingDialog?.isShowing!!) {
            loadingDialog?.dismiss()
        }
        loadingDialog = null
    }
}
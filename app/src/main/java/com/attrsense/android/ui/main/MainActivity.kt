package com.attrsense.android.ui.main

import android.Manifest
import android.content.ContentResolver
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.lifecycleScope
import com.attrsense.android.R
import com.attrsense.android.baselibrary.base.open.SkeletonDataBindingVMBaseActivity
import com.attrsense.android.databinding.ActivityMainBinding
import com.blankj.utilcode.util.ToastUtils
import com.blankj.utilcode.util.ZipUtils
import com.jakewharton.rxbinding4.view.clicks
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File


@AndroidEntryPoint
class MainActivity : SkeletonDataBindingVMBaseActivity<ActivityMainBinding, MainViewModel>() {

    external fun stringFromJNI(): String

    companion object {
        init {
            System.loadLibrary("android")
        }
    }

    override fun setViewModel(): Class<MainViewModel> = MainViewModel::class.java

    override fun setLayoutResId(): Int = R.layout.activity_main

    override fun initView() {
        //设置状态栏/导航栏样式
//        immersionBar {
//            transparentStatusBar()
//            transparentNavigationBar()
//        }

        mDataBinding.acBtnLogin.setOnClickListener {
            mViewModel.login("18874443157", "111111")
        }
        mViewModel.loginLiveData.observe(this) {
            ToastUtils.showShort("登录成功")
        }


        mDataBinding.acBtnRefreshToken.setOnClickListener {
            mViewModel.refreshToken()
        }
        mViewModel.refreshLiveData.observe(this) {
            ToastUtils.showShort("刷新成功！")
        }

        val pickIntent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        ).apply {
//            addCategory(Intent.ACTION_OPEN_DOCUMENT)
            setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")
        }
        val registerForActivityResult =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                if (it.resultCode == RESULT_OK) {
                    //获取返回结果
                    ToastUtils.showShort("获取成功：${it.data?.data}")
                    Log.i("printInfo", "MainActivity::initView: ${it.data?.data}")
//                lifecycleScope.launch{
//                    val result= withContext(Dispatchers.IO){
//
//                    }
//                }
                    it.data?.data?.let { path ->
                        val actualimagecursor = contentResolver.query(path, null, null, null, null)
                        actualimagecursor?.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                            ?.let { i ->
                                actualimagecursor.moveToFirst()
                                val imgPath = actualimagecursor.getString(i)
                                Log.i("printInfo", "MainActivity::initView: $imgPath")
                                mViewModel.uploadFile("1", "2", imgPath)
                            }
                    }
                }
            }

        mDataBinding.acBtnUploadFile.clicks().compose(
            rxPermissions.ensure(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        ).subscribe {
            //mViewModel.uploadFile()
            registerForActivityResult.launch(pickIntent)

        }
        mViewModel.uploadFileLiveData.observe(this) {
            ToastUtils.showShort("上传成功！")
        }


        mDataBinding.acBtnQueryUploadFile.setOnClickListener {
            mViewModel.queryUploadFile(1, 10)
        }
        mViewModel.queryUploadFileLiveData.observe(this) {
            ToastUtils.showShort("查询成功！")
        }


        mDataBinding.acBtnDeleteFile.setOnClickListener {
            mViewModel.deleteFile("group1/M00/00/00/zqgCImNGlGmAD62PAADOvOrd2dY008.anf")
        }
        mViewModel.deleteFileLiveData.observe(this) {

        }
    }
}
package com.attrsense.android.ui.main.local

import android.Manifest
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import com.attrsense.android.R
import com.attrsense.android.baselibrary.base.open.fragment.BaseDataBindingVMFragment
import com.attrsense.android.databinding.FragmentMainLocalBinding
import com.attrsense.android.view.SelectorBottomDialog
import com.jakewharton.rxbinding4.view.clicks
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity
import com.yuyh.library.imgsel.ISNav
import com.yuyh.library.imgsel.config.ISCameraConfig
import com.yuyh.library.imgsel.config.ISListConfig
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream


@AndroidEntryPoint
class MainLocalFragment :
    BaseDataBindingVMFragment<FragmentMainLocalBinding, MainLocalViewModel>() {

    private lateinit var loadingView: ProgressDialog

    override fun setLayoutResId(): Int = R.layout.fragment_main_local

    override fun setViewModel(): Class<MainLocalViewModel> = MainLocalViewModel::class.java

//    private val startActivityLauncher:ActivityResultLauncher<Intent> =registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
//        Log.i("printInfo", "MainLocalFragment::: ")
//        if (it.resultCode == RxAppCompatActivity.RESULT_OK) {
//            val path = it.data!!.getStringExtra("result") // 图片地址
//            Log.i("printInfo", "MainActivity::onActivityResult: $path")
//        }
//    }

    override fun onCreateFragment(savedInstanceState: Bundle?) {
        super.onCreateFragment(savedInstanceState)
//        startActivityLauncher.launch(Intent(requireContext(),MainActivity::class.java))

        loadingView = ProgressDialog(requireActivity())
            .apply {
                setMessage("压缩中...")
                setCanceledOnTouchOutside(false)
            }

        loadingView.setOnDismissListener {

        }
    }


    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        mDataBinding.acImgAdd.clicks().compose(
            rxPermissions.ensure(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
            )
        ).subscribe {
            try {
                SelectorBottomDialog.show(requireActivity(), clickEventListener)
            } catch (e: IllegalStateException) {
                e.printStackTrace()
            }
        }
    }

    private val clickEventListener = object : SelectorBottomDialog.onClickEventListener {
        override fun onClickPhotos() {
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
                .cropSize(1, 1, 200, 200)
                .needCrop(false)
                // 第一个是否显示相机，默认true
                .needCamera(false)
                // 最大选择图片数量，默认9
                .maxNum(9)
                .build()
            ISNav.getInstance().toListActivity(this@MainLocalFragment, config, 10)
        }

        override fun onClickCamera() {
            val config = ISCameraConfig.Builder().build()
            ISNav.getInstance().toCameraActivity(this@MainLocalFragment, config, 100)
        }

        override fun onClickFolder() {

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RxAppCompatActivity.RESULT_OK && data != null) {
            loadingView.show()
            if (requestCode == 10) {
                val path = data.getStringArrayListExtra("result") // 图片集合地址
                Log.i("printInfo", "MainLocalFragment::onActivityResult: $path")
            } else {
                val path = data.getStringExtra("result") // 图片地址
                Log.i("printInfo", "MainLocalFragment::onActivityResult: $path")
            }
        }
    }
}
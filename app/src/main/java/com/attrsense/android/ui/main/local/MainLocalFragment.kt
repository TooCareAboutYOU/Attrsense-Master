package com.attrsense.android.ui.main.local

import android.Manifest
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.attrsense.android.R
import com.attrsense.android.baselibrary.base.open.fragment.BaseDataBindingVMFragment
import com.attrsense.android.baselibrary.base.open.model.ResponseData
import com.attrsense.android.databinding.FragmentMainLocalBinding
import com.attrsense.android.view.SelectorBottomDialog
import com.attrsense.database.db.entity.AnfImageEntity
import com.blankj.utilcode.util.ToastUtils
import com.example.snpetest.JNI
import com.jakewharton.rxbinding4.view.clicks
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import java.util.ArrayList
import javax.inject.Inject


@AndroidEntryPoint
class MainLocalFragment :
    BaseDataBindingVMFragment<FragmentMainLocalBinding, MainLocalViewModel>() {

    @Inject
    lateinit var jni: JNI

    private lateinit var loadingView: ProgressDialog
    private val localList: ArrayList<AnfImageEntity> by lazy { arrayListOf() }
    private lateinit var mAdapter: LocalImageAdapter

//    private val startActivityLauncher:ActivityResultLauncher<Intent> =registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
//        Log.i("printInfo", "MainLocalFragment::: ")
//        if (it.resultCode == RxAppCompatActivity.RESULT_OK) {
//            val path = it.data!!.getStringExtra("result") // 图片地址
//            Log.i("printInfo", "MainActivity::onActivityResult: $path")
//        }
//    }

    override fun setLayoutResId(): Int = R.layout.fragment_main_local

    override fun setViewModel(): Class<MainLocalViewModel> = MainLocalViewModel::class.java

    override fun onCreateFragment(savedInstanceState: Bundle?) {
        super.onCreateFragment(savedInstanceState)
//        startActivityLauncher.launch(Intent(requireContext(),MainActivity::class.java))

        loadingView = ProgressDialog(requireActivity())
            .apply {
                setMessage("压缩中...")
                setCanceledOnTouchOutside(false)
            }

        loadingView.setOnDismissListener {
            Log.i(
                "printInfo",
                "MainLocalFragment::onCreateFragment: Dialog is setOnDismissListener "
            )
            mViewModel.getAll()
        }
    }

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        context?.let {
            mAdapter = LocalImageAdapter(it)
            mDataBinding.recyclerview.apply {
                layoutManager = GridLayoutManager(it, 3, RecyclerView.VERTICAL, false)
                adapter = mAdapter
            }
        }

        mDataBinding.acImgAdd.clicks().compose(
            rxPermissions.ensure(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
            )
        ).subscribe {
            try {
                SelectorBottomDialog.show(this)
            } catch (e: IllegalStateException) {
                e.printStackTrace()
            }
        }

        liveDataObserves()
    }

    /**
     * 拍照单张图片的编解码操作
     */
    private fun liveDataObserves() {

        mViewModel.getAll()

        mViewModel.getAllLiveData.observe(this) {
            when (it) {
                is ResponseData.onFailed -> {

                }
                is ResponseData.onSuccess -> {
                    if (it.value.isNotEmpty()) {
                        mAdapter.setData(it.value)
                    }
                }
            }
        }

    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        loadingView.show()
        super.onActivityResult(requestCode, resultCode, data)
        lifecycleScope.launchWhenResumed {
            Log.i("printInfo", "MainLocalFragment::onActivityResult: $resultCode")
            if (resultCode == RxAppCompatActivity.RESULT_OK && data != null) {
                if (requestCode == SelectorBottomDialog.CAMERA_REQUEST_CODE) {
                    val path = data.getStringExtra("result") // 图片地址
                    lifecycleScope.launchWhenResumed {
                        val anf_path = jni.encoderCommit(path)

                        delay(1000L)

                        if (anf_path.isNotEmpty()) {
                            val entity = AnfImageEntity(originalImage = path, anfImage = anf_path)
                            mViewModel.addEntity(entity)
                            loadingView.dismiss()
                        }
                    }

                } else {
                    val path = data.getStringArrayListExtra("result") as ArrayList // 图片集合地址
                    val anfPaths = jni.encoderCommitList(path.toTypedArray())
                    delay(1000L)
                    if (anfPaths.isNotEmpty()) {
                        localList.clear()
                        anfPaths.forEachIndexed { index, s ->
                            val entity =
                                AnfImageEntity(originalImage = path[index], anfImage = s)
                            localList.add(entity)
                        }
                        mViewModel.addEntities(localList)
                        loadingView.dismiss()
                    }
                }

            } else {
                loadingView.dismiss()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        localList.clear()
    }
}

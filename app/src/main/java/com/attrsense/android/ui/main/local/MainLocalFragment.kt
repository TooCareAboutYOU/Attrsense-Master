package com.attrsense.android.ui.main.local

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.attrsense.android.R
import com.attrsense.android.baselibrary.base.open.fragment.BaseDataBindingVMFragment
import com.attrsense.android.baselibrary.base.open.model.ResponseData
import com.attrsense.android.databinding.FragmentMainLocalBinding
import com.attrsense.android.manager.UserDataManager
import com.attrsense.android.ui.main.detail.ImageViewPagerFragment
import com.attrsense.android.util.FilesHelper
import com.attrsense.database.db.entity.AnfImageEntity
import com.attrsense.ui.library.dialog.SelectorBottomDialog
import com.attrsense.ui.library.recycler.GridLayoutDecoration
import com.blankj.utilcode.util.FileUtils
import com.example.snpetest.JniInterface
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class MainLocalFragment :
    BaseDataBindingVMFragment<FragmentMainLocalBinding, MainLocalViewModel>() {

    @Inject
    lateinit var userDataManager: UserDataManager

    private lateinit var intentActivityResultLauncher: ActivityResultLauncher<Intent>


    private val localList: MutableList<AnfImageEntity> = mutableListOf()
    private lateinit var mAdapter: LocalImageAdapter

    override fun setLayoutResId(): Int = R.layout.fragment_main_local

    override fun initView(savedInstanceState: Bundle?) {
        intentActivityResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                if (it.resultCode == Activity.RESULT_OK) {
                    //获取返回的结果
                }
            }

        mDataBinding.toolBarView.load(requireActivity()).apply {
            hideLeftIcon()
            this.setCenterTitle("双深")
            this.setRightIcon(com.attrsense.ui.library.R.drawable.icon_add)
            this.setRightClick {
                rxPermissions.request(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA
                ).subscribe {
                    try {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                            if (!Environment.isExternalStorageManager()) {
                                requestAllFilesPermission()
                            } else {
                                SelectorBottomDialog.show(this@MainLocalFragment)
                            }
                        } else {
                            SelectorBottomDialog.show(this@MainLocalFragment)
                        }

                    } catch (e: IllegalStateException) {
                        e.printStackTrace()
                    }
                }
            }
        }

        context?.let {
            mAdapter = LocalImageAdapter()
            mDataBinding.recyclerview.apply {
                layoutManager = GridLayoutManager(it, 2, RecyclerView.VERTICAL, false)
                addItemDecoration(GridLayoutDecoration(10))
                adapter = mAdapter
            }

            mAdapter.setEmptyView(com.attrsense.ui.library.R.layout.layout_load_empty_view)
        }

        initListener()

        liveDataObserves()

        mViewModel.getAll()
    }

    /**
     * 拍照单张图片的编解码操作
     */
    private fun liveDataObserves() {
        mViewModel.getLiveData.observe(this) {
            when (it) {
                is ResponseData.OnFailed -> {
                    showToast(it.throwable.message)
                }
                is ResponseData.OnSuccess -> {
                    it.value?.apply {
                        if (this.isNotEmpty()) {
                            mAdapter.addData(this)
                        }
                    }
                }
            }
            dismissLoadingDialog()
        }

        mViewModel.deleteLiveData.observe(this) {
            mAdapter.removeAt(it)
            showToast("删除成功！")
        }
    }

    private fun initListener() {
        mAdapter.setOnItemClickListener { _, _, position ->
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                if (!Environment.isExternalStorageManager()) {
                    requestAllFilesPermission()
                } else {
                    ImageViewPagerFragment.showDialog(requireActivity(), position, mAdapter.data)
                }
            } else {
                ImageViewPagerFragment.showDialog(requireActivity(), position, mAdapter.data)
            }
        }

        mAdapter.setOnItemLongClickListener { _, _, position ->
            val data = mAdapter.getItem(position)
            mViewModel.deleteByAnfPath(position, data.anfImage)
            false
        }
    }


    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        showLoadingDialog("压缩中...")
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RxAppCompatActivity.RESULT_OK) {
            if (data != null) {
                lifecycleScope.launch(Dispatchers.IO) {
                    if (requestCode == SelectorBottomDialog.CAMERA_REQUEST_CODE) {
                        val path = data.getStringExtra(SelectorBottomDialog.KEY_RESULT) // 图片地址
                        val anfPath = JniInterface.encoderCommit(path)
                        if (anfPath.isNotEmpty()) {
                            val entity = AnfImageEntity(
                                token = userDataManager.getToken(),
                                mobile = userDataManager.getMobile(),
                                originalImage = path,
                                thumbImage = FilesHelper.saveThumb(requireActivity(), path),
                                anfImage = anfPath,
                                srcSize = FileUtils.getFileLength(path).toInt(),
                                isLocal = true
                            )
                            mViewModel.addEntities(mutableListOf(entity))
                        }
                    } else {
                        val paths = data.getStringArrayListExtra(SelectorBottomDialog.KEY_RESULT) as ArrayList // 图片集合地址
                        val anfPaths = JniInterface.encoderCommitList(paths)
                        if (anfPaths.isNotEmpty()) {
                            localList.clear()
                            anfPaths.forEachIndexed { index, anfPath ->
                                val entity = AnfImageEntity(
                                    token = userDataManager.getToken(),
                                    mobile = userDataManager.getMobile(),
                                    originalImage = paths[index],
                                    thumbImage = FilesHelper.saveThumb(
                                        requireActivity(),
                                        paths[index]
                                    ),
                                    anfImage = anfPath,
                                    srcSize = FileUtils.getFileLength(paths[index]).toInt(),
                                    isLocal = true
                                )
                                localList.add(entity)
                            }
                            mViewModel.addEntities(localList)
                        }
                    }
                }
            } else {
                dismissLoadingDialog()
            }
        } else {
            dismissLoadingDialog()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        localList.clear()
    }

    private fun requestAllFilesPermission(){
        val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
        intent.data = Uri.parse("package:" + context?.packageName)
        intentActivityResultLauncher.launch(intent)
    }

}

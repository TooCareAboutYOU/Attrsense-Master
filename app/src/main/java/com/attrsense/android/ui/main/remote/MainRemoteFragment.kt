package com.attrsense.android.ui.main.remote

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.attrsense.android.R
import com.attrsense.android.baselibrary.base.open.fragment.BaseDataBindingVMFragment
import com.attrsense.android.baselibrary.base.open.model.BaseResponse
import com.attrsense.android.baselibrary.base.open.model.ResponseData
import com.attrsense.android.databinding.FragmentMainRemoteBinding
import com.attrsense.android.model.ImageInfoBean
import com.attrsense.android.model.ImagesBean
import com.attrsense.android.ui.main.detail.ImageViewPagerFragment
import com.attrsense.ui.library.dialog.SelectorBottomDialog
import com.attrsense.ui.library.recycler.GridLayoutDecoration
import com.attrsense.ui.library.recycler.RecyclerLoadMoreView
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainRemoteFragment :
    BaseDataBindingVMFragment<FragmentMainRemoteBinding, MainRemoteViewModel>() {


    private lateinit var mAdapter: RemoteImageAdapter
    private lateinit var intentActivityResultLauncher: ActivityResultLauncher<Intent>

    //页码
    private var pageIndex = 1

    //一页的数量
    private val pageSize = 20

    override fun setLayoutResId(): Int = R.layout.fragment_main_remote

    override fun initView(savedInstanceState: Bundle?) {
        intentActivityResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                if (it.resultCode == Activity.RESULT_OK) {

                }
            }


        mDataBinding.toolbar.load(requireActivity()).apply {
            this.hideLeftIcon()
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
                                SelectorBottomDialog.show(this@MainRemoteFragment)
                            }
                        } else {
                            SelectorBottomDialog.show(this@MainRemoteFragment)
                        }
                    } catch (e: IllegalStateException) {
                        e.printStackTrace()
                    }
                }
            }

        }

        initRecyclerView()

        liveDataObserves()

        initRefreshLayout()

        loadServer()

    }

    private fun initRecyclerView() {
        context?.let {
            mAdapter = RemoteImageAdapter()
            mAdapter.apply {
                loadMoreModule.apply {
                    //设置加载状态UI
                    loadMoreView = RecyclerLoadMoreView()
                    //打开或者关闭加载更多功能
                    isEnableLoadMore = true
                    // 是否自定义加载下一页（默认为true）
                    isAutoLoadMore = true
                    // 当数据不满一页时，是否继续自动加载（默认为true）
                    isEnableLoadMoreIfNotFullPage = false
                }
            }

            mDataBinding.recyclerview.apply {
                layoutManager = GridLayoutManager(it, 2, RecyclerView.VERTICAL, false)
                addItemDecoration(GridLayoutDecoration(10))
                adapter = mAdapter
            }

            mAdapter.setEmptyView(com.attrsense.ui.library.R.layout.layout_load_empty_view)
        }


        // 设置加载更多监听事件
        mAdapter.loadMoreModule.setOnLoadMoreListener {
            loadServer()
        }

        mAdapter.setOnItemClickListener { _, _, position ->
            _clickData = mAdapter.getItem(position)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                if (!Environment.isExternalStorageManager()) {
                    requestAllFilesPermission()
                } else {
                    mViewModel.getByThumb(position, _clickData!!.thumbnailUrl)
                }
            } else {
                mViewModel.getByThumb(position, _clickData!!.thumbnailUrl)
            }
        }

        mAdapter.setOnItemLongClickListener { _, _, position ->
            val data = mAdapter.getItem(position)
            mViewModel.deleteByThumb(position, data.thumbnailUrl, data.fileId)
            false
        }
    }


    private fun initRefreshLayout() {
        mDataBinding.swipeRefreshLayout.setColorSchemeColors(Color.rgb(47, 223, 189))
        mDataBinding.swipeRefreshLayout.setOnRefreshListener {
            mViewModel.deleteAll()
            pageIndex = 1
            mAdapter.setList(null)
            loadServer()
        }
    }

    private var _clickData: ImageInfoBean? = null
    private var clickPosition = -1
    private fun liveDataObserves() {
        mViewModel.getAllLiveData.observe(this) {
            loadData(it)
        }

        mViewModel.uploadLiveData.observe(this) {
            //新增单条或者多条数据
            loadData(it)
        }

        mViewModel.getByThumbLiveData.observe(this) {
            when (it) {
                is ResponseData.OnFailed -> {

                }
                is ResponseData.OnSuccess -> {
                    clickPosition = it.value!!
                }
            }
        }

        mViewModel.getAllDbLiveData.observe(this) {
            ImageViewPagerFragment.showDialog(requireActivity(), clickPosition, it)
        }

        mViewModel.deleteLiveData.observe(this) {
            when (it) {
                is ResponseData.OnFailed -> {
                    showToast("删除失败！")
                }
                is ResponseData.OnSuccess -> {
                    mAdapter.removeAt(it.value!!)
                    showToast("删除成功！")
                }
            }
        }
    }

    private fun loadServer() {
        mViewModel.getRemoteFiles(pageIndex, pageSize)
    }

    private fun loadData(
        response: ResponseData<BaseResponse<ImagesBean?>>
    ) {
        mDataBinding.swipeRefreshLayout.isRefreshing = false
        mAdapter.loadMoreModule.isEnableLoadMore = true
        when (response) {
            is ResponseData.OnFailed -> {
                showToast("解压失败！")
                Log.e("print_logs", "MainRemoteFragment::loadData: ${response.throwable}")
                mAdapter.loadMoreModule.loadMoreFail()
            }
            is ResponseData.OnSuccess -> {
                response.value?.data?.images?.apply {
                    if (this.isNotEmpty()) {
                        if (pageIndex == 1) {
                            mAdapter.setList(this)
                        } else {
                            mAdapter.addData(this)
                        }
                        if (this.size < pageSize) {
                            mAdapter.loadMoreModule.loadMoreEnd(true)
                        } else {
                            mAdapter.loadMoreModule.loadMoreComplete()
                        }
                        pageIndex++
                    } else {
                        if (pageIndex == 1) {
                            mAdapter.setEmptyView(com.attrsense.ui.library.R.layout.layout_load_empty_view)
                        } else {
                            mAdapter.loadMoreModule.loadMoreEnd(true)
                        }
                    }
                }
            }
        }
        dismissLoadingDialog()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RxAppCompatActivity.RESULT_OK && data != null) {
            if (requestCode == SelectorBottomDialog.CAMERA_REQUEST_CODE) {
                data.getStringExtra("result")?.apply {
                    upload(arrayListOf(this))
                }
            } else {
                (data.getStringArrayListExtra("result") as ArrayList).let {
                    if (it.isNotEmpty()) {
                        upload(it)
                    }
                }
            }
        } else {
            dismissLoadingDialog()
        }
    }

    private fun upload(list: List<String>) {
        showLoadingDialog("压缩中...")
        mViewModel.uploadFile("4", "4", list)
    }

    private fun requestAllFilesPermission(){
        val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
        intent.data = Uri.parse("package:" + context?.packageName)
        intentActivityResultLauncher.launch(intent)
    }
}
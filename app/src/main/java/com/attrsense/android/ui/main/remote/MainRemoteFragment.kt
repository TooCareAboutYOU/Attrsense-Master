package com.attrsense.android.ui.main.remote

import android.Manifest
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.attrsense.android.R
import com.attrsense.android.baselibrary.base.open.fragment.BaseDataBindingVMFragment
import com.attrsense.android.baselibrary.base.open.model.BaseResponse
import com.attrsense.android.baselibrary.base.open.model.ResponseData
import com.attrsense.android.baselibrary.util.CleanMessageUtils
import com.attrsense.android.databinding.FragmentMainRemoteBinding
import com.attrsense.android.databinding.LayoutDialogItemShowBinding
import com.attrsense.android.model.ImageInfoBean
import com.attrsense.android.model.ImagesBean
import com.attrsense.database.db.entity.AnfImageEntity
import com.attrsense.ui.library.dialog.ImageShowDialog
import com.attrsense.ui.library.dialog.SelectorBottomDialog
import com.attrsense.ui.library.recycler.RecyclerLoadMoreView
import com.blankj.utilcode.util.FileUtils
import com.blankj.utilcode.util.ToastUtils
import com.bumptech.glide.Glide
import com.example.snpetest.JniInterface
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File


@AndroidEntryPoint
class MainRemoteFragment :
    BaseDataBindingVMFragment<FragmentMainRemoteBinding, MainRemoteViewModel>() {

    private lateinit var mAdapter: RemoteImageAdapter

    //页码
    private var pageIndex = 1

    //一页的数量
    private val pageSize = 10

    override fun setLayoutResId(): Int = R.layout.fragment_main_remote

    override fun initView(savedInstanceState: Bundle?) {
        mDataBinding.toolbar.load(requireActivity()).apply {
            this.hideLeftIcon()
            this.setRightClick {
                rxPermissions.request(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA
                ).subscribe {
                    try {
                        SelectorBottomDialog.show(this@MainRemoteFragment)
                    } catch (e: IllegalStateException) {
                        e.printStackTrace()
                    }
                }
            }
            this.setCenterTitle("双深")
            this.setRightIcon(com.attrsense.ui.library.R.drawable.icon_add)
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
                layoutManager = GridLayoutManager(it, 3, RecyclerView.VERTICAL, false)
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
            viewModel.getByThumb(_clickData!!.thumbnailUrl)
        }

        mAdapter.setOnItemLongClickListener { _, _, position ->
            val data = mAdapter.getItem(position)
            viewModel.deleteByThumb(position, data.thumbnailUrl, data.fileId)
            false
        }
    }


    private fun initRefreshLayout() {
        mDataBinding.swipeRefreshLayout.setColorSchemeColors(Color.rgb(47, 223, 189))
        mDataBinding.swipeRefreshLayout.setOnRefreshListener {
            pageIndex = 1
            mAdapter.setList(null)
            loadServer()
        }
    }

    private var _clickData: ImageInfoBean? = null
    private fun liveDataObserves() {
        viewModel.getAllLiveData.observe(this) {
            loadData(it)
        }

        viewModel.uploadLiveData.observe(this) {
            //新增单条或者多条数据
            loadData(it)
        }

        viewModel.getByThumbLiveData.observe(this) {
            when (it) {
                is ResponseData.onFailed -> {

                }
                is ResponseData.onSuccess -> {
                    it.value?.let { entity ->
                        showDialog(entity)
                    }
                }
            }
        }

        viewModel.deleteLiveData.observe(this) {
            when (it) {
                is ResponseData.onFailed -> {
                    ToastUtils.showShort("删除失败！")
                }
                is ResponseData.onSuccess -> {
                    mAdapter.removeAt(it.value!!)
                    ToastUtils.showShort("删除成功！")
                }
            }
        }
    }

    private fun loadServer() {
        viewModel.getRemoteFiles(pageIndex, pageSize)
    }

    private fun loadData(
        response: ResponseData<BaseResponse<ImagesBean?>>
    ) {
        mDataBinding.swipeRefreshLayout.isRefreshing = false
        mAdapter.loadMoreModule.isEnableLoadMore = true
        when (response) {
            is ResponseData.onFailed -> {
                ToastUtils.showShort("解压失败！")
                Log.e("print_logs", "MainRemoteFragment::loadData: ${response.throwable}")
                mAdapter.loadMoreModule.loadMoreFail()
            }
            is ResponseData.onSuccess -> {
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
        viewModel.uploadFile("4", "4", list)
    }

    private fun showDialog(entity: AnfImageEntity) {
        showLoadingDialog("解压中...")
        val viewBinding: LayoutDialogItemShowBinding = DataBindingUtil.inflate(
            layoutInflater, R.layout.layout_dialog_item_show, null, true
        )

        Glide.with(requireActivity()).load(entity.thumbImage)
            .error(R.mipmap.ic_launcher)
            .into(viewBinding.acIvImg)

        val dialog = ImageShowDialog(requireActivity()).apply {
            show()
            setCancelable(true)
            setContentView(viewBinding.root)
        }


        lifecycleScope.launch {
            val bitmap = withContext(Dispatchers.IO) {
                JniInterface.decoderCommitPath2Buffer(entity.anfImage)
            }

            viewBinding.acTvInfo.text = StringBuilder().apply {
                if (_clickData != null) {
                    append("原JPG：${CleanMessageUtils.getFormatSize(_clickData?.srcSize!!.toDouble())}").append(
                        "\n"
                    )
                }
                append("ANF：${FileUtils.getSize(entity.anfImage)}").append("\n")
                append("宽：${bitmap.width}").append("\n").append("高：${bitmap.height}")
                toString()
            }

            Glide.with(requireActivity()).load(bitmap)
                .error(R.mipmap.ic_launcher)
                .into(viewBinding.acIvImg)
        }

        dismissLoadingDialog()


        viewBinding.acIvImg.setOnClickListener {
            dialog.dismiss()
        }

        viewBinding.root.setOnClickListener {
            dialog.dismiss()
        }


        viewBinding.acTvSave.setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO) {
                if (!TextUtils.isEmpty(entity.anfImage) && !File(entity.cacheImage).exists()) {
                    val path = JniInterface.decoderCommit(entity.anfImage)
                    entity.cacheImage = path
                    viewModel.updateList(arrayListOf(entity))
                }
            }
            ToastUtils.showShort("保存成功!")
        }
        dialog.setOnDismissListener {
            _clickData = null
        }
    }
}
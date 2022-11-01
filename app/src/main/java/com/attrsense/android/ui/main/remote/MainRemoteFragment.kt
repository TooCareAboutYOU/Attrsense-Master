package com.attrsense.android.ui.main.remote

import android.Manifest
import android.content.Intent
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
import com.attrsense.android.view.ImageShowDialog
import com.attrsense.android.view.SelectorBottomDialog
import com.attrsense.database.db.entity.AnfImageEntity
import com.blankj.utilcode.util.FileUtils
import com.blankj.utilcode.util.ToastUtils
import com.bumptech.glide.Glide
import com.example.snpetest.JniInterface
import com.jakewharton.rxbinding4.view.clicks
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

    private val mList: MutableList<ImageInfoBean> = mutableListOf()

    override fun setLayoutResId(): Int = R.layout.fragment_main_remote

    override fun setViewModel(): Class<MainRemoteViewModel> = MainRemoteViewModel::class.java

    override fun onCreateFragment(savedInstanceState: Bundle?) {
        super.onCreateFragment(savedInstanceState)
    }


    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)

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
            this.setCenterTitle("双深科技")
            this.setRightIcon(R.drawable.icon_add)
        }

        context?.let {
            mAdapter = RemoteImageAdapter(mList)
            mDataBinding.recyclerview.apply {
                layoutManager = GridLayoutManager(it, 3, RecyclerView.VERTICAL, false)
                adapter = mAdapter
            }
        }

        mAdapter.setOnItemClickListener { _, _, position ->
            _clickData = mList[position]
            mViewModel.getByThumb(mList[position].thumbnailUrl)
        }

        mAdapter.setOnItemLongClickListener { _, _, position ->
            mViewModel.deleteByThumb(position, mList[position].thumbnailUrl, mList[position].fileId)
            false
        }

        liveDataObserves()
    }

    private var _clickData: ImageInfoBean? = null
    private fun liveDataObserves() {
        mViewModel.getRemoteFiles(1, 10)
        mViewModel.getAllLiveData.observe(this) {
            reloadAdapter(it)
        }

        mViewModel.uploadLiveData.observe(this) {
            //新增单条或者多条数据
            reloadAdapter(it, true)
        }

        mViewModel.getByThumbLiveData.observe(this) {
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

        mViewModel.deleteLiveData.observe(this) {
            mAdapter.removeAt(it)
            ToastUtils.showShort("删除成功！")
        }
    }

    private fun reloadAdapter(
        response: ResponseData<BaseResponse<ImagesBean?>>, isNewAdd: Boolean? = false
    ) {
        when (response) {
            is ResponseData.onFailed -> {
                ToastUtils.showShort("解压失败！")
                Log.e("print_logs", "MainRemoteFragment::reloadAdapter: ${response.throwable}")
            }
            is ResponseData.onSuccess -> {
                response.value?.data?.images?.apply {
                    if (this.isNotEmpty()) {
                        isNewAdd?.let {
                            if (it) {
                                mList.addAll(this)
                                mAdapter.notifyItemInserted(mList.size - 1)
                            } else {
                                mAdapter.setList(this)
                            }
                        }
                    }
                }
            }
        }
        hideLoadingDialog()
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
            hideLoadingDialog()
        }
    }

    private fun upload(list: List<String>) {
        showLoadingDialog("压缩中...")
        mViewModel.uploadFile("4", "4", list)
    }

    private fun showDialog(entity: AnfImageEntity) {
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
            showLoadingDialog("解压中...")
            val bitmap = withContext(Dispatchers.IO) {
                JniInterface.decoderCommitPath2Buffer(entity.anfImage)
            }
            hideLoadingDialog()
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
                    mViewModel.addEntities(arrayListOf(entity))
                }
            }
            ToastUtils.showShort("保存成功!")
        }
        dialog.setOnDismissListener {
            _clickData = null
        }
    }
}
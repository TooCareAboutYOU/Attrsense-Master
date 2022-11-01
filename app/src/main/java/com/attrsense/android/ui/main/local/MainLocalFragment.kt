package com.attrsense.android.ui.main.local

import android.Manifest
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.media.ThumbnailUtils
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.attrsense.android.R
import com.attrsense.android.baselibrary.base.open.fragment.BaseDataBindingVMFragment
import com.attrsense.android.baselibrary.base.open.model.ResponseData
import com.attrsense.android.databinding.FragmentMainLocalBinding
import com.attrsense.android.databinding.LayoutDialogItemShowBinding
import com.attrsense.android.ui.main.OnItemClickListener
import com.attrsense.android.manager.UserDataManager
import com.attrsense.android.util.FilesHelper
import com.attrsense.android.view.ImageShowDialog
import com.attrsense.android.view.SelectorBottomDialog
import com.attrsense.database.db.entity.AnfImageEntity
import com.blankj.utilcode.util.FileUtils
import com.blankj.utilcode.util.ToastUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.snpetest.JniInterface
import com.jakewharton.rxbinding4.view.clicks
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.util.ArrayList
import javax.inject.Inject


@AndroidEntryPoint
class MainLocalFragment : BaseDataBindingVMFragment<FragmentMainLocalBinding, MainLocalViewModel>(),
    OnItemClickListener {

    @Inject
    lateinit var userDataManager: UserDataManager

    private val localList: ArrayList<AnfImageEntity> by lazy { arrayListOf() }
    private lateinit var mAdapter: LocalImageAdapter


    override fun setLayoutResId(): Int = R.layout.fragment_main_local

    override fun setViewModel(): Class<MainLocalViewModel> = MainLocalViewModel::class.java

    override fun onCreateFragment(savedInstanceState: Bundle?) {
        super.onCreateFragment(savedInstanceState)
    }

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        mDataBinding.toolBarView.load(requireActivity()).apply {
            hideLeftIcon()
            this.setRightClick {
                rxPermissions.request(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA
                ).subscribe {
                    try {
                        SelectorBottomDialog.show(this@MainLocalFragment)
                    } catch (e: IllegalStateException) {
                        e.printStackTrace()
                    }
                }
            }
            this.setCenterTitle("双深科技")
            this.setRightIcon(R.drawable.icon_add)
        }

        context?.let {
            mAdapter = LocalImageAdapter(it, this)
            mDataBinding.recyclerview.apply {
                layoutManager = GridLayoutManager(it, 3, RecyclerView.VERTICAL, false)
                adapter = mAdapter
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
//                    ToastUtils.showShort(it.throwable.toString())
                    Log.i("print_logs", "MainLocalFragment::liveDataObserves: ${it.throwable}")
                }
                is ResponseData.onSuccess -> {
                    mAdapter.setData(it.value)
                }
            }
        }

        mViewModel.addEntityLiveData.observe(this) {
            when (it) {
                is ResponseData.onFailed -> {

                }
                is ResponseData.onSuccess -> {
                    mViewModel.getAll()
                    hideLoadingDialog()
                }
            }
        }

        mViewModel.deleteLiveData.observe(this) {
            mViewModel.getAll()
            ToastUtils.showShort("删除成功！")
        }

    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        showLoadingDialog("压缩中...")
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RxAppCompatActivity.RESULT_OK && data != null) {
            lifecycleScope.launch(Dispatchers.IO) {
                if (requestCode == SelectorBottomDialog.CAMERA_REQUEST_CODE) {
                    val path = data.getStringExtra("result") // 图片地址
                    val anf_path = JniInterface.encoderCommit(path)
                    if (anf_path.isNotEmpty()) {
                        val entity = AnfImageEntity(
                            token = userDataManager.getToken(),
                            mobile = userDataManager.getMobile(),
                            originalImage = path,
                            thumbImage = FilesHelper.saveThumb(requireActivity(), path),
                            anfImage = anf_path,
                            isLocal = true
                        )
                        mViewModel.addEntities(arrayListOf(entity))
                    }
                } else {
                    val paths = data.getStringArrayListExtra("result") as ArrayList // 图片集合地址
                    val anfPaths = JniInterface.encoderCommitList(paths)
                    if (anfPaths.isNotEmpty()) {
                        localList.clear()
                        anfPaths.forEachIndexed { index, anfPath ->
                            val entity = AnfImageEntity(
                                token = userDataManager.getToken(),
                                mobile = userDataManager.getMobile(),
                                originalImage = paths[index],
                                thumbImage = FilesHelper.saveThumb(requireActivity(), paths[index]),
                                anfImage = anfPath,
                                isLocal = true
                            )
                            localList.add(entity)
                        }
                        mViewModel.addEntities(localList)
                    }
                }
            }
        } else {
            hideLoadingDialog()
        }
    }


    override fun onLongClickEvent(position: Int, anfPath: String?) {
        mViewModel.deleteByAnfPath(position, anfPath)
    }

    override fun onLocalClickEvent(entity: AnfImageEntity?) {
        entity?.also {
            showDialog(it)
        }
    }


    private fun showDialog(entity: AnfImageEntity) {
        val viewBinding: LayoutDialogItemShowBinding = DataBindingUtil.inflate(
            layoutInflater, R.layout.layout_dialog_item_show, null, true
        )

        Glide.with(requireActivity()).load(entity.thumbImage).error(R.mipmap.ic_launcher)
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
            viewBinding.acTvInfo.text =
                StringBuilder().append("原JPG：${FileUtils.getSize(entity.originalImage)}")
                    .append("\n").append("ANF：${FileUtils.getSize(entity.anfImage)}").append("\n")
                    .append("宽：${bitmap.width}").append("\n").append("高：${bitmap.height}")
                    .toString()

            Glide.with(requireActivity()).load(bitmap).error(R.mipmap.ic_launcher)
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
    }

    override fun onDestroyView() {
        super.onDestroyView()
        localList.clear()
    }

}

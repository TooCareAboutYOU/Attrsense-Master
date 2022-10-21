package com.attrsense.android.ui.main.local

import android.Manifest
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.WindowManager
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.DataBindingUtil.setContentView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.attrsense.android.R
import com.attrsense.android.baselibrary.base.open.fragment.BaseDataBindingVMFragment
import com.attrsense.android.baselibrary.base.open.model.ResponseData
import com.attrsense.android.databinding.FragmentMainLocalBinding
import com.attrsense.android.databinding.LayoutDialogItemShowBinding
import com.attrsense.android.ui.main.OnItemClickListener
import com.attrsense.android.ui.register.view.ImageShowDialog
import com.attrsense.android.ui.register.view.SelectorBottomDialog
import com.attrsense.database.db.entity.AnfImageEntity
import com.blankj.utilcode.util.FileUtils
import com.blankj.utilcode.util.ToastUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.snpetest.JNI
import com.jakewharton.rxbinding4.view.clicks
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.util.ArrayList
import javax.inject.Inject


@AndroidEntryPoint
class MainLocalFragment :
    BaseDataBindingVMFragment<FragmentMainLocalBinding, MainLocalViewModel>(), OnItemClickListener {

    @Inject
    lateinit var jni: JNI

    private lateinit var loadingView: ProgressDialog
    private lateinit var loading2View: ProgressDialog
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

        loading2View = ProgressDialog(requireActivity())
            .apply {
                setMessage("解压中...")
                setCanceledOnTouchOutside(false)
            }
    }

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        context?.let {
            mAdapter = LocalImageAdapter(it, this)
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
            val base_path =
                requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES)?.absolutePath

            val decodeFile = File(base_path, "anf")

            if (!decodeFile.exists()) {
                decodeFile.mkdirs()
            }

            val encodeFile = File(base_path, "image")
            if (!encodeFile.exists()) {
                encodeFile.mkdirs()
            }

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
//                    ToastUtils.showShort(it.throwable.toString())
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
                    loadingView.dismiss()
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
        loadingView.show()
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RxAppCompatActivity.RESULT_OK && data != null) {
            lifecycleScope.launch(Dispatchers.IO) {
                if (requestCode == SelectorBottomDialog.CAMERA_REQUEST_CODE) {
                    val path = data.getStringExtra("result") // 图片地址
                    val anf_path = jni.encoderCommit(path)
                    if (anf_path.isNotEmpty()) {
                        val entity = AnfImageEntity(originalImage = path, anfImage = anf_path)
                        mViewModel.addEntities(arrayListOf(entity))
                    }
                } else {
                    val paths = data.getStringArrayListExtra("result") as ArrayList // 图片集合地址
                    val anfPaths = jni.encoderCommitList(paths.toTypedArray())
                    if (anfPaths.isNotEmpty()) {
                        localList.clear()
                        anfPaths.forEachIndexed { index, path ->
                            val entity =
                                AnfImageEntity(originalImage = paths[index], anfImage = path)
                            localList.add(entity)
                        }
                        mViewModel.addEntities(localList)
                    }
                }
            }
        } else {
            loadingView.dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        localList.clear()
    }

    override fun onLongClickEvent(position: Int, anfPath: String?) {
        mViewModel.deleteByAnfPath(position, anfPath)
    }

    override fun onLocalClickEvent(entity: AnfImageEntity?) {
        entity?.also {
            loading2View.show()
            lifecycleScope.launch {
                withContext(Dispatchers.IO) {
                    val path =
                        jni.decoderCommit(it.anfImage) //("/storage/emulated/0/Android/data/com.attrsense.android/files/Pictures/anf/hahaha-1.anf")
                    it.cacheImage = path
                    mViewModel.addEntities(arrayListOf(it))
                    path
                }
                showDialog(entity)
            }
        }
    }

    private fun showDialog(entity: AnfImageEntity) {
        val viewBinding: LayoutDialogItemShowBinding =
            DataBindingUtil.inflate(
                layoutInflater,
                R.layout.layout_dialog_item_show,
                null,
                false
            )
        val bitmap = BitmapFactory.decodeFile(entity.cacheImage)
        viewBinding.acTvInfo.text = "ANF：${FileUtils.getSize(entity.anfImage)}\n" +
                "JPG：${FileUtils.getSize(entity.cacheImage)}\n" +
                "宽：${bitmap.width}\n" +
                "高：${bitmap.height}"

        Glide.with(requireActivity()).load(entity.cacheImage)
            .error(R.mipmap.ic_launcher)
            .addListener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    loading2View.dismiss()
                    return false
                }
            })
            .into(viewBinding.acIvImg)

        val dialog = ImageShowDialog(requireActivity()).apply {
            show()
            setCancelable(true)
            setContentView(viewBinding.root)
        }

        viewBinding.acIvImg.setOnClickListener {
            dialog.dismiss()
        }

        viewBinding.root.setOnClickListener {
            dialog.dismiss()
        }
    }
}

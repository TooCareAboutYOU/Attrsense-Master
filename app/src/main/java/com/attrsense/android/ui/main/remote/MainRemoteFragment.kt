package com.attrsense.android.ui.main.remote

import android.Manifest
import android.annotation.SuppressLint
import android.app.DownloadManager
import android.app.ProgressDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.database.Cursor
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.attrsense.android.R
import com.attrsense.android.baselibrary.base.open.fragment.BaseDataBindingVMFragment
import com.attrsense.android.baselibrary.base.open.model.BaseResponse
import com.attrsense.android.baselibrary.base.open.model.ResponseData
import com.attrsense.android.databinding.FragmentMainRemoteBinding
import com.attrsense.android.databinding.LayoutDialogItemShowBinding
import com.attrsense.android.model.ImageInfoBean
import com.attrsense.android.model.ImagesBean
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
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject


@AndroidEntryPoint
class MainRemoteFragment :
    BaseDataBindingVMFragment<FragmentMainRemoteBinding, MainRemoteViewModel>(),
    OnItemClickListener {

    @Inject
    lateinit var jni: JNI

    private lateinit var loadingView: ProgressDialog
    private lateinit var loading2View: ProgressDialog
    private lateinit var mAdapter: RemoteImageAdapter

    override fun setLayoutResId(): Int = R.layout.fragment_main_remote

    override fun setViewModel(): Class<MainRemoteViewModel> = MainRemoteViewModel::class.java

    override fun onCreateFragment(savedInstanceState: Bundle?) {
        super.onCreateFragment(savedInstanceState)

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
            mAdapter = RemoteImageAdapter(it, this)
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
        mViewModel.getRemoteFiles(1, 30)
        mViewModel.getAllLiveData.observe(this) {
            reloadAdapter(it)
        }

        mViewModel.uploadLiveData.observe(this) {
            reloadAdapter(it)
        }
    }

    private fun reloadAdapter(it: ResponseData<BaseResponse<ImagesBean?>>) {
        when (it) {
            is ResponseData.onFailed -> {
//                ToastUtils.showShort(it.throwable.toString())
            }
            is ResponseData.onSuccess -> {
                it.value?.data?.images?.apply {
                    if (this.isNotEmpty()) {
                        mAdapter.setData(this)
                    }
                }
            }
        }
        loadingView.dismiss()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        loadingView.show()
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RxAppCompatActivity.RESULT_OK && data != null) {
            lifecycleScope.launch(Dispatchers.IO) {
                if (requestCode == SelectorBottomDialog.CAMERA_REQUEST_CODE) {
                    // 图片地址
                    data.getStringExtra("result")?.apply {
                        Log.i("printInfo", "MainRemoteFragment::onActivityResult: $this")
                        upload(arrayListOf(this))
                    }
                } else {
                    (data.getStringArrayListExtra("result") as ArrayList).let {
                        Log.i("printInfo", "MainRemoteFragment::onActivityResult: ${it.size}")
                        if (it.isNotEmpty()) {
                            upload(it)
                        }
                    }
                }
            }
        } else {
            loadingView.dismiss()
        }
    }

    private fun upload(list: List<String>) {
        mViewModel.uploadFile("1", "2", list)
    }

    override fun onLongClickEvent(position: Int, anfPath: String?) {
//        mViewModel.deleteByAnfPath(position,anfPath)
    }

    override fun onRemoteClickEvent(entity: ImageInfoBean?) {
        entity?.url?.let {
            download(it)
        }
    }

    lateinit var downManager: DownloadManager
    var downloadId: Long = 0

    private fun download(anfPath: String) {
        downManager =
            requireActivity().getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager

        val request = DownloadManager.Request(Uri.parse(anfPath))
        //设置在什么网络情况下进行下载
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
        request.setAllowedOverMetered(true)
        //设置通知栏标题
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
        request.setTitle("下载ANF文件中")
        request.setDescription("正在下载...")
        //设置是否允许漫游
        request.setAllowedOverRoaming(true)
        //设置文件存放目录
        request.setDestinationInExternalFilesDir(
            requireActivity(),
            "Pictures/anf",
            "hahaha.anf"
        )

        downloadId = downManager.enqueue(request)

        requireActivity().registerReceiver(
            receiver,
            IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
        )
    }

    //广播监听下载的各个状态
    private val receiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            checkStatus()
        }
    }

    //检查下载状态
    @SuppressLint("Range")
    private fun checkStatus() {
        val query = DownloadManager.Query()
        //通过下载的id查找
        query.setFilterById(downloadId)
        val cursor: Cursor = downManager.query(query)
        if (cursor.moveToFirst()) {
            val status: Int = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))

            when (status) {
                DownloadManager.STATUS_PAUSED -> {}
                DownloadManager.STATUS_PENDING -> {}
                DownloadManager.STATUS_RUNNING -> {}
                DownloadManager.STATUS_FAILED -> {
                    cursor.close()
                }
                DownloadManager.STATUS_SUCCESSFUL -> {
                    //下载完成
                    Log.i("printInfo", "MainRemoteFragment::checkStatus: 下载完成！")
                    val remoteAnfUri =
                        cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_URI))
                    Log.i("printInfo", "MainRemoteFragment::checkStatus:下载地址： $remoteAnfUri")
                    val localAnfUri =
                        cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI))
                    Log.i("printInfo", "MainRemoteFragment::checkStatus:存储地址： $localAnfUri")
                    cursor.close()

//                    loading2View.show()
//
//                    lifecycleScope.launch {
//                        val imagePath=withContext(Dispatchers.IO) {
//                    val imagePath=jni.decoderCommit(localAnfUri)
//                        }
//                        Log.i("printInfo", "MainRemoteFragment::checkStatus:存储地址: $imagePath")
//                        showDialog(localAnfUri,imagePath)
//                    }
                    try {
                        val imagePath = jni.decoderCommit(localAnfUri)
                        Log.i("printInfo", "MainRemoteFragment::checkStatus:存储地址: $imagePath")
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        }
    }

    private fun showDialog(anfPath: String, jpgPath: String) {
        val viewBinding: LayoutDialogItemShowBinding =
            DataBindingUtil.inflate(
                layoutInflater,
                R.layout.layout_dialog_item_show,
                null,
                false
            )
        val bitmap = BitmapFactory.decodeFile(jpgPath)
        viewBinding.acTvInfo.text = "ANF：${FileUtils.getSize(anfPath)}\n" +
                "JPG：${FileUtils.getSize(jpgPath)}\n" +
                "宽：${bitmap.width}\n" +
                "高：${bitmap.height}"

        Glide.with(requireActivity()).load(jpgPath)
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
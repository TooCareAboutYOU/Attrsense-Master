package com.attrsense.android.ui.main.remote

import android.Manifest
import android.annotation.SuppressLint
import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.database.Cursor
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
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
import com.attrsense.android.view.ImageShowDialog
import com.attrsense.android.view.SelectorBottomDialog
import com.blankj.utilcode.util.FileUtils
import com.blankj.utilcode.util.ToastUtils
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.example.snpetest.JniInterface
import com.jakewharton.rxbinding4.view.clicks
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


@AndroidEntryPoint
class MainRemoteFragment : BaseDataBindingVMFragment<FragmentMainRemoteBinding, MainRemoteViewModel>(){

    private lateinit var mAdapter: RemoteImageAdapter

    private val mList: MutableList<ImageInfoBean> = mutableListOf()

    override fun setLayoutResId(): Int = R.layout.fragment_main_remote

    override fun setViewModel(): Class<MainRemoteViewModel> = MainRemoteViewModel::class.java

    override fun onCreateFragment(savedInstanceState: Bundle?) {
        super.onCreateFragment(savedInstanceState)
    }


    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        context?.let {
            mAdapter = RemoteImageAdapter(mList)
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

        mAdapter.setOnItemClickListener { _, _, position ->
            mList[position].url?.let {
                download(it)
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
            reloadAdapter(it,true)
        }
    }

    private fun reloadAdapter(response: ResponseData<BaseResponse<ImagesBean?>>,isNewAdd:Boolean?=false) {
        when (response) {
            is ResponseData.onFailed -> {
                ToastUtils.showShort("解压失败！")
                Log.e("printInfo", "MainRemoteFragment::reloadAdapter: ${response.throwable}")
            }
            is ResponseData.onSuccess -> {
                response.value?.data?.images?.apply {
                    if (this.isNotEmpty()) {
                        isNewAdd?.let {
                            if (it) {
                                mList.addAll(this)
                                mAdapter.notifyItemInserted(mList.size-1)
                            }else{
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
        mViewModel.uploadFile("1", "2", list)
    }

//    override fun onLongClickEvent(position: Int, anfPath: String?) {
//        mViewModel.deleteByAnfPath(position,anfPath)
//    }

    private var downloadBroadcastReceiver: DownloadBroadcastReceiver? = null
    lateinit var downManager: DownloadManager
    var downloadId: Long = 0

    private fun download(anfPath: String) {

        downManager = requireActivity().getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager

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
            "${anfPath.substringAfterLast("/")}"
        )

        downloadId = downManager.enqueue(request)

        downloadBroadcastReceiver = DownloadBroadcastReceiver()
        requireActivity().registerReceiver(
            downloadBroadcastReceiver,
            IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
        )
    }

    private inner class DownloadBroadcastReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            checkStatus()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (downloadBroadcastReceiver != null) {
            requireActivity().unregisterReceiver(downloadBroadcastReceiver)
            downloadBroadcastReceiver = null
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
            when (cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))) {
                DownloadManager.STATUS_PAUSED -> {}
                DownloadManager.STATUS_PENDING -> {}
                DownloadManager.STATUS_RUNNING -> {}
                DownloadManager.STATUS_FAILED -> {
                    ToastUtils.showShort("下载失败！请检查当前网络！")
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


                    lifecycleScope.launch {
//                        showLoadingDialog("解压中...")
                        val imagePath = withContext(Dispatchers.IO) {
                            JniInterface.decoderCommit(localAnfUri)
                        }
                        Log.i("printInfo", "MainRemoteFragment::checkStatus: $imagePath")
                        showDialog(localAnfUri, imagePath)
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

        viewBinding.acTvSave.setOnClickListener {

        }
    }


}
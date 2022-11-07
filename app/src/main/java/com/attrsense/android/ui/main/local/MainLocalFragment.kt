package com.attrsense.android.ui.main.local

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.attrsense.android.R
import com.attrsense.android.baselibrary.base.open.fragment.BaseDataBindingVMFragment
import com.attrsense.android.baselibrary.base.open.model.ResponseData
import com.attrsense.android.baselibrary.view.GridLayoutDecoration
import com.attrsense.android.databinding.FragmentMainLocalBinding
import com.attrsense.android.databinding.LayoutImageViewPagerBinding
import com.attrsense.android.manager.UserDataManager
import com.attrsense.android.ui.main.ImageViewPagerFragment
import com.attrsense.android.util.FilesHelper
import com.attrsense.ui.library.dialog.SelectorBottomDialog
import com.attrsense.database.db.entity.AnfImageEntity
import com.attrsense.ui.library.dialog.ImageShowDialog
import com.blankj.utilcode.util.FileUtils
import com.example.snpetest.JniInterface
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.ArrayList
import javax.inject.Inject


@AndroidEntryPoint
class MainLocalFragment :
    BaseDataBindingVMFragment<FragmentMainLocalBinding, MainLocalViewModel>() {

    @Inject
    lateinit var userDataManager: UserDataManager

    private val localList: MutableList<AnfImageEntity> = mutableListOf()
    private lateinit var mAdapter: LocalImageAdapter

    override fun setLayoutResId(): Int = R.layout.fragment_main_local

    override fun initView(savedInstanceState: Bundle?) {
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
                        SelectorBottomDialog.show(this@MainLocalFragment)
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
                is ResponseData.onFailed -> {
                    showToast(it.throwable.toString())
                    Log.e("print_logs", "MainLocalFragment::liveDataObserves: ${it.throwable}")
                }
                is ResponseData.onSuccess -> {
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
            showDialog(position)
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
        if (resultCode == RxAppCompatActivity.RESULT_OK && data != null) {
            lifecycleScope.launch(Dispatchers.IO) {
                if (requestCode == SelectorBottomDialog.CAMERA_REQUEST_CODE) {
                    val path = data.getStringExtra("result") // 图片地址
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
    }


    private fun showDialog(enterPosition: Int) {
        val viewBinding: LayoutImageViewPagerBinding = DataBindingUtil.inflate(
            layoutInflater, R.layout.layout_image_view_pager, null, true
        )

        val dialog = ImageShowDialog(requireActivity()).apply {
            show()
            setCancelable(true)
            setContentView(viewBinding.root)
        }
        viewBinding.viewPager2.adapter =
            ViewPager2Adapter(requireActivity() as AppCompatActivity, dialog, mAdapter.data)

        viewBinding.viewPager2.setCurrentItem(enterPosition, false)

        dialog.setOnDismissListener {
            viewBinding.viewPager2.removeAllViews()
        }
    }

    private class ViewPager2Adapter(
        activity: FragmentActivity,
        private val dialog: ImageShowDialog,
        private val dataList: MutableList<AnfImageEntity>
    ) : FragmentStateAdapter(activity) {
        override fun getItemCount(): Int = dataList.size

        override fun createFragment(position: Int): Fragment {
            return ImageViewPagerFragment.newInstance(dataList[position],
                object : ImageViewPagerFragment.OnViewPagerFragmentListener {
                    override fun onDismiss() {
                        dialog.dismiss()
                    }
                })
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        localList.clear()
    }

}

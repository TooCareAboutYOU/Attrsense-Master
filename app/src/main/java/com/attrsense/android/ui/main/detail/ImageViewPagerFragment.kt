package com.attrsense.android.ui.main.detail

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.media.MediaScannerConnection
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import com.attrsense.android.R
import com.attrsense.android.baselibrary.base.open.fragment.BaseDataBindingVMFragment
import com.attrsense.android.baselibrary.base.open.model.ResponseData
import com.attrsense.android.databinding.FragmentImageViewPagerBinding
import com.attrsense.android.databinding.LayoutImageViewPagerBinding
import com.attrsense.database.db.entity.AnfImageEntity
import com.attrsense.ui.library.dialog.ImageShowDialog
import com.attrsense.ui.library.expand.singleClick
import com.blankj.utilcode.util.ConvertUtils
import com.blankj.utilcode.util.FileUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.snpetest.JniInterface
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File


@AndroidEntryPoint
class ImageViewPagerFragment constructor(private val listener: OnViewPagerFragmentListener?) :
    BaseDataBindingVMFragment<FragmentImageViewPagerBinding, ImageViewPagerViewModel>() {

    private lateinit var entity: AnfImageEntity
    private var mBitmap: Bitmap? = null

    interface OnViewPagerFragmentListener {
        fun onDismiss()
    }

    companion object {
        private const val ARG_PARAM1 = "param1"

        @JvmStatic
        fun showDialog(
            activity: FragmentActivity,
            enterPosition: Int,
            dataList: MutableList<AnfImageEntity>
        ) {
            val viewBinding: LayoutImageViewPagerBinding = DataBindingUtil.inflate(
                activity.layoutInflater, R.layout.layout_image_view_pager, null, true
            )

            val dialog = ImageShowDialog(activity).apply {
                show()
                setCancelable(true)
                setContentView(viewBinding.root)
            }
            viewBinding.viewPager2.adapter =
                ViewPager2Adapter(
                    activity as AppCompatActivity,
                    dialog,
                    dataList
                )

            viewBinding.viewPager2.setCurrentItem(enterPosition, false)
            if (dataList.size == 2) {
                viewBinding.viewPager2.offscreenPageLimit = 1
            } else if (dataList.size > 2) {
                viewBinding.viewPager2.offscreenPageLimit = 2
            }

            dialog.setOnDismissListener {
                viewBinding.viewPager2.removeAllViews()
            }
        }

        @JvmStatic
        fun newInstance(
            entity: AnfImageEntity,
            listener: OnViewPagerFragmentListener? = null
        ) = ImageViewPagerFragment(listener).apply {
            arguments = Bundle().apply {
                putSerializable(ARG_PARAM1, entity)
            }
        }

    }

    override fun setLayoutResId(): Int = R.layout.fragment_image_view_pager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            entity = it.getSerializable(ARG_PARAM1) as AnfImageEntity
        }
    }

    override fun initView(savedInstanceState: Bundle?) {
        mDataBinding.viewModel = mViewModel

        Glide.with(this@ImageViewPagerFragment)
            .load(entity.thumbImage)
            .skipMemoryCache(true)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .into(mDataBinding.acIvPhotoThumbView)

        lifecycleScope.launch {
            mBitmap = withContext(Dispatchers.IO) {
                JniInterface.decoderCommitPath2Buffer(entity.anfImage)
            }

            mDataBinding.acTvInfo.text =
                StringBuilder().append("原JPG：${ConvertUtils.byte2FitMemorySize(entity.srcSize.toLong())}")
                    .append("\n").append("ANF：${FileUtils.getSize(entity.anfImage)}").append("\n")
                    .append("宽：${mBitmap?.width}").append("\n").append("高：${mBitmap?.height}")
                    .toString()

            Glide.with(this@ImageViewPagerFragment)
                .load(mBitmap)
                .error(R.drawable.icon_bad_image)
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        mDataBinding.loadingProgress.visibility = View.GONE
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        mDataBinding.acIvPhotoThumbView.visibility = View.GONE
                        mDataBinding.loadingProgress.visibility = View.GONE
                        return false
                    }

                })
                .into(mDataBinding.acIvPhotoView)


            mDataBinding.acIvPhotoView.singleClick {
                recycleBitmap()
                listener?.onDismiss()
            }

            mDataBinding.root.singleClick {
                recycleBitmap()
                listener?.onDismiss()
            }

            mDataBinding.acTvSave.singleClick {
                showLoadingDialog("保存中...")
                lifecycleScope.launch {
                    if (!TextUtils.isEmpty(entity.anfImage)) {
                        if (!File(entity.cacheImage).exists()) {
                            withContext(Dispatchers.IO) {
                                val path = JniInterface.decoderCommit(entity.anfImage)
                                entity.cacheImage = path
                                mViewModel.update(entity)
                                MediaScannerConnection.scanFile(
                                    context,
                                    arrayOf(path),
                                    null,
                                    null
                                )
                            }
                        } else {
                            showToast("文件已保存!")
                        }
                    }
                }
            }
        }

        mViewModel.getLiveData.observe(this) {
            dismissLoadingDialog()
            when (it) {
                is ResponseData.OnFailed -> {
                    showToast("保存失败：${it.throwable.message}")
                }
                is ResponseData.OnSuccess -> {
                    showToast("保存成功!")
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        recycleBitmap()
    }

    private fun recycleBitmap() {
        if (mBitmap != null) {
            mBitmap?.recycle()
            mBitmap = null
        }
    }
}
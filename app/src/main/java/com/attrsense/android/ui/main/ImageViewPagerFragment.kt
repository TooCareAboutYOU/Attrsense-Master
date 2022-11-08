package com.attrsense.android.ui.main

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.attrsense.android.R
import com.attrsense.android.baselibrary.base.open.fragment.BaseDataBindingVMFragment
import com.attrsense.android.baselibrary.base.open.model.ResponseData
import com.attrsense.android.baselibrary.util.singleClick
import com.attrsense.android.databinding.FragmentImageViewPagerBinding
import com.attrsense.database.db.entity.AnfImageEntity
import com.blankj.utilcode.util.ConvertUtils
import com.blankj.utilcode.util.FileUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.snpetest.JniInterface
import com.jakewharton.rxbinding4.view.clicks
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File


@AndroidEntryPoint
class ImageViewPagerFragment private constructor(private val listener: OnViewPagerFragmentListener?) :
    BaseDataBindingVMFragment<FragmentImageViewPagerBinding, ImageViewPagerViewModel>() {

    private lateinit var entity: AnfImageEntity
    private var bitmap: Bitmap? = null

    interface OnViewPagerFragmentListener {
        fun onDismiss()
    }

    companion object {
        private const val ARG_PARAM1 = "param1"

        @JvmStatic
        fun newInstance(entity: AnfImageEntity, listener: OnViewPagerFragmentListener? = null) =
            ImageViewPagerFragment(listener).apply {
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

    override fun onResume() {
        super.onResume()
        mDataBinding.acIvPhotoThumbView.visibility = View.VISIBLE
        mDataBinding.loadingProgress.visibility = View.VISIBLE

        lifecycleScope.launch {
            bitmap = withContext(Dispatchers.IO) {
                JniInterface.decoderCommitPath2Buffer(entity.anfImage)
            }

            mDataBinding.acTvInfo.text =
                StringBuilder().append("原JPG：${ConvertUtils.byte2FitMemorySize(entity.srcSize.toLong())}")
                    .append("\n").append("ANF：${FileUtils.getSize(entity.anfImage)}").append("\n")
                    .append("宽：${bitmap?.width}").append("\n").append("高：${bitmap?.height}")
                    .toString()

            Glide.with(this@ImageViewPagerFragment)
                .load(bitmap)
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
                bitmap?.recycle()
                listener?.onDismiss()
            }

            mDataBinding.root.singleClick {
                bitmap?.recycle()
                listener?.onDismiss()
            }

            mDataBinding.acTvSave.singleClick {
                lifecycleScope.launchWhenResumed {
                    withContext(Dispatchers.IO) {
                        if (!TextUtils.isEmpty(entity.anfImage) && !File(entity.cacheImage).exists()) {
                            val path = JniInterface.decoderCommit(entity.anfImage)
                            entity.cacheImage = path
                            mViewModel.updateList(arrayListOf(entity))
                        }
                    }
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        if (bitmap != null) {
            bitmap?.recycle()
            bitmap = null
        }
    }


    override fun initView(savedInstanceState: Bundle?) {
        Glide.with(this@ImageViewPagerFragment)
            .load(entity.thumbImage)
            .skipMemoryCache(true)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .into(mDataBinding.acIvPhotoThumbView)

        mViewModel.getLiveData.observe(this) {
            when (it) {
                is ResponseData.onFailed -> {
                    showToast("保存失败：${it.throwable}")
                }
                is ResponseData.onSuccess -> {
                    showToast("保存成功!")
                }
            }
        }
    }
}
package com.attrsense.android.ui.main.remote

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.attrsense.android.R
import com.attrsense.android.baselibrary.view.BaseBindingViewHolder
import com.attrsense.android.databinding.LayoutLocalItemBinding
import com.attrsense.android.databinding.LayoutRemoteItemBinding
import com.attrsense.android.model.ImageInfoBean
import com.attrsense.android.ui.main.OnItemClickListener
import com.attrsense.database.db.entity.AnfImageEntity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.jakewharton.rxbinding4.view.clicks
import com.jakewharton.rxbinding4.view.longClicks
import com.orhanobut.logger.Logger
import okhttp3.internal.notify

/**
 * author : zhangshuai@attrsense.com
 * date : 2022/10/20 15:28
 * mark : custom something
 */
class RemoteImageAdapter constructor(
    private val context: Context,
    private val listener: OnItemClickListener? = null
) : RecyclerView.Adapter<BaseBindingViewHolder>() {

    private val mList: MutableList<ImageInfoBean?> = mutableListOf()

    @SuppressLint("NotifyDataSetChanged")
    fun setData(list: List<ImageInfoBean?>?, isClear: Boolean? = false) {
        Log.i("printInfo", "LocalImageAdapter::setData: 刷新列表before_ ${mList.size}")
        list?.let {
            isClear?.let { state ->
                if (state) {
                    mList.clear()
                }
            }
            mList.addAll(it)
            notifyDataSetChanged()
            Log.i("printInfo", "LocalImageAdapter::setData: 刷新列表after_ ${mList.size}")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseBindingViewHolder =
        BaseBindingViewHolder(
            LayoutRemoteItemBinding.inflate(
                LayoutInflater.from(context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: BaseBindingViewHolder, position: Int) {
        val entity = mList[position]
        (holder.binding as LayoutRemoteItemBinding).apply {
            acIvImg.clicks().subscribe {
                listener?.onRemoteClickEvent(entity)
            }

            acIvImg.longClicks().subscribe {
                listener?.onLongClickEvent(position, entity?.url)
            }

            Glide.with(context).load(entity?.thumbnailUrl)
                .error(R.mipmap.ic_launcher)
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        Logger.e("local image is load failed!!! $e")
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        return false
                    }

                }).into(this.acIvImg)
        }
    }

    override fun getItemCount(): Int = mList.size
}
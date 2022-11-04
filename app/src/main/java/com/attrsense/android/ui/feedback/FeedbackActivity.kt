package com.attrsense.android.ui.feedback

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.attrsense.android.R
import com.attrsense.android.baselibrary.base.open.activity.BaseDataBindingVMActivity
import com.attrsense.android.baselibrary.view.GridLayoutDecoration
import com.attrsense.android.databinding.ActivityFeedbackBinding
import com.attrsense.android.ui.feedback.entity.ItemMultipleEntity
import com.attrsense.ui.library.dialog.SelectorBottomDialog
import com.blankj.utilcode.util.ToastUtils
import com.jakewharton.rxbinding4.widget.textChanges
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.ArrayList

/**
 * 意见反馈
 */
@AndroidEntryPoint
class FeedbackActivity : BaseDataBindingVMActivity<ActivityFeedbackBinding, FeedbackViewModel>() {


    private var mList = ArrayList<ItemMultipleEntity>().apply {
        add(ItemMultipleEntity(ItemMultipleEntity.PLACE_HOLDER))
    }


    private lateinit var mAdapter: FeedbackPictureSelectorAdapter

    //记录需要添加图片的item位置
    private var clickPosition: Int = 0

    //空占位
    private var isAddToList = true
    private val maxCount = 6
    private var selectorCount = maxCount

    override fun setLayoutResId(): Int = R.layout.activity_feedback


    override fun initView(savedInstanceState: Bundle?) {

        mDataBinding.toolBarView.load(this).apply {
            this.setCenterTitle(R.string.tab_main_user_feedback_title)
            this.setLeftClick {
                this@FeedbackActivity.finish()
            }
        }

        mAdapter = FeedbackPictureSelectorAdapter(mList)

        mDataBinding.recyclerview.apply {
            layoutManager =
                GridLayoutManager(this@FeedbackActivity, 3, RecyclerView.VERTICAL, false)
            addItemDecoration(GridLayoutDecoration(10))
            adapter = mAdapter
        }


        setListener()
    }

    private fun setListener() {
        mDataBinding.acEtDescription.requestFocus()
        addDisposable(mDataBinding.acEtDescription.textChanges().subscribe {
            mDataBinding.acBtnCommit.isEnabled = it.isNotEmpty()
            mDataBinding.acTvCount.text =
                getString(R.string.tab_main_user_feedback_count).format(it.length)
        })

        //提交
        mDataBinding.acBtnCommit.setOnClickListener {
            val pictures = arrayListOf<String?>().apply {
                if (mList.size > 0) {
                    mList.forEach { this.add(it.imageUrl) }
                }
            }

            viewModel.feedback(mDataBinding.acEtDescription.text.toString(), pictures)
        }


        //监听提交
        viewModel.feedbackLivedata.observe(this) {
            if (it) {
                ToastUtils.showShort("提交成功！")
                mDataBinding.acEtDescription.text = null
                mList.clear()
                mList.add(ItemMultipleEntity(ItemMultipleEntity.PLACE_HOLDER))
                mAdapter.notifyDataSetChanged()
            } else {
                ToastUtils.showShort("提交失败,请重试！")
            }
            dismissLoadingDialog()
        }


        mAdapter.setOnItemClickListener { adapter, _, position ->
            rxPermissions.request(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
            ).subscribe {
                clickPosition = position
                val item = adapter.getItem(position) as ItemMultipleEntity
                when (item.itemType) {
                    ItemMultipleEntity.IMAGE -> {

                    }
                    ItemMultipleEntity.PLACE_HOLDER -> {
                        if (isAddToList) {
                            selectorCount = maxCount - mList.size + 1
                        }

                        try {
                            SelectorBottomDialog.show(this, maxCount = selectorCount)
                        } catch (e: IllegalStateException) {
                            e.printStackTrace()
                        }
                    }
                    else -> {}
                }
            }
        }
        mAdapter.addChildClickViewIds(R.id.acIv_delete)

        mAdapter.setOnItemChildClickListener { _, _, position ->
            mAdapter.removeAt(position)
            if (!isAddToList) {
                mList.add(mList.size, ItemMultipleEntity(ItemMultipleEntity.PLACE_HOLDER))
                isAddToList = true
            }
        }
    }


    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RxAppCompatActivity.RESULT_OK && data != null) {
            lifecycleScope.launch(Dispatchers.IO) {
                if (requestCode == SelectorBottomDialog.CAMERA_REQUEST_CODE) {
                    val path = data.getStringExtra("result") // 图片地址
                    if (!TextUtils.isEmpty(path)) {
                        mList.add(
                            clickPosition,
                            ItemMultipleEntity(ItemMultipleEntity.IMAGE).apply {
                                imageUrl = path
                            })
                        if (mList.size > maxCount) {
                            mList.removeAt(maxCount)
                            isAddToList = false
                        }
                    }
                } else {
                    val paths = data.getStringArrayListExtra("result") as ArrayList // 图片集合地址
                    if (paths.isNotEmpty()) {
                        paths.forEach {
                            if (isAddToList) {
                                mAdapter.removeAt(mList.size - 1)
                                isAddToList = false
                            }

                            mList.add(
                                mList.size,
                                ItemMultipleEntity(ItemMultipleEntity.IMAGE).apply {
                                    imageUrl = it
                                })
                        }

                        if (mList.size < maxCount && !isAddToList) {
                            mList.add(ItemMultipleEntity(ItemMultipleEntity.PLACE_HOLDER))
                            isAddToList = true
                        }
                    }
                }

                mAdapter.notifyDataSetChanged()
            }
        }
    }
}
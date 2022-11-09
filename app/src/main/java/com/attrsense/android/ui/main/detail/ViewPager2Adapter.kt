package com.attrsense.android.ui.main.detail

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.attrsense.database.db.entity.AnfImageEntity
import com.attrsense.ui.library.dialog.ImageShowDialog

/**
 * @author zhangshuai
 * @date 2022/11/9 11:48
 * @description
 */
class ViewPager2Adapter(
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

package com.attrsense.android.ui.detail

import com.attrsense.android.baselibrary.base.open.viewmodel.SkeletonViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * author : zhangshuai@attrsense.com
 * date : 2022/10/14 15:17
 * mark : custom something
 */
@HiltViewModel
class DetailFragmentViewModel @Inject constructor(
    private val detailFragmentRepository: DetailFragmentRepository
) : SkeletonViewModel() {


}
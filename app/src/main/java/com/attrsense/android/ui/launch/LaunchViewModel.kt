package com.attrsense.android.ui.launch

import com.attrsense.android.baselibrary.base.open.viewmodel.SkeletonViewModel
import com.attrsense.android.repository.AppRepository
import com.attrsense.database.repository.DatabaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * author : zhangshuai@attrsense.com
 * date : 2022/10/9 16:51
 * mark : custom something
 */
@HiltViewModel
class LaunchViewModel @Inject constructor(
    private val databaseRepository: DatabaseRepository,
    private val appRepository: AppRepository
) : SkeletonViewModel() {


}
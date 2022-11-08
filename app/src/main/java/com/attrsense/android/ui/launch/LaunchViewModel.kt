package com.attrsense.android.ui.launch

import android.util.Log
import android.view.View
import com.attrsense.android.baselibrary.base.open.viewmodel.BaseViewModel
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
) : BaseViewModel() {


}
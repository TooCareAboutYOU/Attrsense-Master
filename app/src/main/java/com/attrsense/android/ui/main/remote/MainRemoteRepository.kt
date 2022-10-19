package com.attrsense.android.ui.main.remote

import com.attrsense.android.base.BaseRepository
import com.attrsense.android.http.ApiService
import javax.inject.Inject

/**
 * author : zhangshuai@attrsense.com
 * date : 2022/10/18 16:34
 * mark : custom something
 */
class MainRemoteRepository @Inject constructor(private val apiService: ApiService) :
    BaseRepository() {
}
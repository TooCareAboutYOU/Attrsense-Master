package com.attrsense.android.util

import com.attrsense.android.baselibrary.util.MMKVUtils
import javax.inject.Inject

/**
 * author : zhangshuai@attrsense.com
 * date : 2022/10/17 15:57
 * mark : 用户信息操作相关
 */

class UserDataManager @Inject constructor(private val mmkvUtils: MMKVUtils) {

    companion object{
        //账户token
        private const val KEY_ACCOUNT_TOKEN = "key_account_token"
        private const val KEY_ACCOUNT_REFRESH_TOKEN = "key_account_refresh_token"
        private const val _token = "" //eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJleHAiOjE2NzMzNTgxODUsInVzZXJfaWQiOjV9.4ugNg57lwAamYi9RmdRjYcJJ08hYY1kc7o6qfUCHAQE
    }

    init {
        mmkvUtils.setValue(KEY_ACCOUNT_TOKEN, _token)
    }

    fun isLogin(): Boolean {
        return mmkvUtils.getString(KEY_ACCOUNT_TOKEN) != ""
    }

    fun getToken(): String? = mmkvUtils.getString(KEY_ACCOUNT_TOKEN)

    fun setToken(token: String? = "", refresh_token: String? = "") {
        mmkvUtils.setValue(KEY_ACCOUNT_TOKEN, token)
        mmkvUtils.setValue(KEY_ACCOUNT_REFRESH_TOKEN, refresh_token)
    }
}
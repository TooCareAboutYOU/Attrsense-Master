package com.attrsense.android.manager

import com.attrsense.android.baselibrary.util.MMKVUtils
import javax.inject.Inject

/**
 * author : zhangshuai@attrsense.com
 * date : 2022/10/17 15:57
 * mark : 用户信息操作相关
 */

class UserDataManager @Inject constructor(private val mmkvUtils: MMKVUtils) {

    companion object {
        //用户手机号
        private const val KEY_ACCOUNT_MOBILE = "key_account_mobile"

        //账户token
        private const val KEY_ACCOUNT_TOKEN = "key_account_token"

        //刷新token
        private const val KEY_ACCOUNT_REFRESH_TOKEN = "key_account_refresh_token"
    }

    init {
        mmkvUtils.setValue(KEY_ACCOUNT_TOKEN, "")
    }

    fun isLogin(): Boolean {
        return mmkvUtils.getString(KEY_ACCOUNT_TOKEN) != ""
    }

    fun getMobile(): String? = mmkvUtils.getString(KEY_ACCOUNT_MOBILE)

    fun getToken(): String? = mmkvUtils.getString(KEY_ACCOUNT_TOKEN)


    fun save(mobile: String? = "", token: String? = "", refresh_token: String? = "") {
        mmkvUtils.setValue(KEY_ACCOUNT_MOBILE, mobile)
        mmkvUtils.setValue(KEY_ACCOUNT_TOKEN, token)
        mmkvUtils.setValue(KEY_ACCOUNT_REFRESH_TOKEN, refresh_token)
    }
}
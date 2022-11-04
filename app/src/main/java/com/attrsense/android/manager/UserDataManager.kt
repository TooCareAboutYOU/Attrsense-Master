package com.attrsense.android.manager

import android.text.TextUtils
import android.util.Log
import com.attrsense.android.baselibrary.util.MMKVUtils
import javax.inject.Inject

/**
 * author : zhangshuai@attrsense.com
 * date : 2022/10/17 15:57
 * mark : 用户信息操作相关
 */

class UserDataManager @Inject constructor(
    private val mmkvUtils: MMKVUtils
) {

    companion object {
        //用户手机号
        const val KEY_USER_MOBILE = "key_user_mobile"

        //账户token
        private const val KEY_USER_TOKEN = "key_user_token"

        //刷新token
        private const val KEY_USER_REFRESH_TOKEN = "key_user_refresh_token"
    }

    fun isLogin(): Boolean {
        val state = !TextUtils.isEmpty(mmkvUtils.getString(KEY_USER_TOKEN))
        return state
    }

    fun getMobile(): String? {
        val mobile = mmkvUtils.getString(KEY_USER_MOBILE)
        return mobile
    }

    fun getToken(): String? {
        val token = mmkvUtils.getString(KEY_USER_TOKEN)
        return token
    }


    fun save(mobile: String? = "", token: String? = "", refresh_token: String? = "") {
        mmkvUtils.setValue(KEY_USER_MOBILE, mobile)
        mmkvUtils.setValue(KEY_USER_TOKEN, token)
        mmkvUtils.setValue(KEY_USER_REFRESH_TOKEN, refresh_token)
    }

    fun unSave() {
        mmkvUtils.removeKey(KEY_USER_MOBILE)
        mmkvUtils.removeKey(KEY_USER_TOKEN)
        mmkvUtils.removeKey(KEY_USER_REFRESH_TOKEN)
    }
}
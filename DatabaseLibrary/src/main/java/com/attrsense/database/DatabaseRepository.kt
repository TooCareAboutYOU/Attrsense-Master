package com.attrsense.database

import android.text.TextUtils
import android.util.Log
import com.attrsense.android.baselibrary.base.open.repository.SkeletonRepository
import com.attrsense.database.db.AttrSenseRoomDatabase
import javax.inject.Inject

/**
 * 数据库操作管理类
 */
class DatabaseRepository @Inject constructor(
    private val db: AttrSenseRoomDatabase
) :
    SkeletonRepository() {

    fun getUserDao() = db.getUserDao()

    /**
     * 提供给业务层的使用
     */
    suspend fun isLogin(mobile: String?): Boolean = if (TextUtils.isEmpty(mobile)) {
        Log.e("printInfo", "DatabaseRepository::isLogin: 手机号为空！")
        false
    } else {
        val users = getUserDao().queryByMobile(mobile)
        if (users != null && users.isNotEmpty()) {
            Log.e("printInfo", "DatabaseRepository::isLogin: 已经登录！")
            !TextUtils.isEmpty(users[0].token)
        }
        Log.e("printInfo", "DatabaseRepository::isLogin: 未登录!")
        false
    }

    /**
     * 提供给业务层的使用
     */
    suspend fun isLoginByToken(token: String?): Boolean = if (TextUtils.isEmpty(token)) {
        Log.e("printInfo", "DatabaseRepository::isLogin: 手机号为空！")
        false
    } else {
        val users = getUserDao().queryByToken(token)
        if (users != null && users.isNotEmpty()) {
            Log.e("printInfo", "DatabaseRepository::isLogin: 已经登录！")
            !TextUtils.isEmpty(users[0].token)
        }
        Log.e("printInfo", "DatabaseRepository::isLogin: 未登录!")
        false
    }
}
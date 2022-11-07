package com.attrsense.database.base

import com.google.gson.Gson

/**
 * author : zhangshuai@attrsense.com
 * date : 2022/10/14 10:48
 * mark : 数据库Entity表结构基类
 */
open class BaseEntity :java.io.Serializable{
    override fun toString(): String {
        return Gson().toJson(this)
    }
}
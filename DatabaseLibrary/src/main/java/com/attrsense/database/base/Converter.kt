package com.attrsense.database.base

import androidx.room.TypeConverter
import java.util.*

/**
 * author : zhangshuai@attrsense.com
 * date : 2022/10/14 10:48
 * mark : 自定义转换器
 */
open class Converter {

    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }
}
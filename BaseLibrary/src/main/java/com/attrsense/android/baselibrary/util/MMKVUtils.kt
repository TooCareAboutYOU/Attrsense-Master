package com.attrsense.android.baselibrary.util

import android.os.Parcelable
import com.orhanobut.logger.Logger
import com.tencent.mmkv.MMKV
import java.util.Collections
import javax.inject.Inject

/**
 * 轻量级数据缓存
 */
interface MMKVUtilsEvent {
    fun setValue(key: String, value: Any?)

    fun setStringSet(key: String, value: MutableSet<String>?)

    fun getStringSet(key: String): MutableSet<String>?

    fun getBoolean(key: String): Boolean

    fun getInt(key: String): Int

    fun getLong(key: String): Long

    fun getFloat(key: String): Float

    fun getDouble(key: String): Double

    fun getString(key: String): String?

    fun getByteArray(key: String): ByteArray?

    fun <T : Parcelable> getParcelable(key: String, tClass: Class<T>): T?

    fun removeKey(key: String)

    fun clearAll()
}

class MMKVUtils @Inject constructor(private val _mmkv: MMKV) : MMKVUtilsEvent {

    init {
//        Logger.i("MMKVUtils::init success")
    }

    override fun setValue(key: String, value: Any?) {
        when (value) {
            is String -> {
                _mmkv.encode(key, value)
            }
            is Int -> {
                _mmkv.encode(key, value)
            }
            is Float -> {
                _mmkv.encode(key, value)
            }
            is Long -> {
                _mmkv.encode(key, value)
            }
            is Double -> {
                _mmkv.encode(key, value)
            }
            is Boolean -> {
                _mmkv.encode(key, value)
            }
            is ByteArray -> {
                _mmkv.encode(key, value)
            }
            is Parcelable -> {
                _mmkv.encode(key, value)
            }
            else -> {
                Logger.i("Save Failed！！！")
                return
            }
        }
    }

    override fun setStringSet(key: String, value: MutableSet<String>?) {
        _mmkv.encode(key, value)
    }

    override fun getStringSet(key: String): MutableSet<String>? =
        _mmkv.decodeStringSet(key, Collections.emptySet())

    override fun getBoolean(key: String): Boolean = _mmkv.decodeBool(key, false)

    override fun getInt(key: String): Int = _mmkv.decodeInt(key, 0)

    override fun getLong(key: String): Long = _mmkv.decodeLong(key, 0L)

    override fun getFloat(key: String): Float = _mmkv.decodeFloat(key, 0F)

    override fun getDouble(key: String): Double = _mmkv.decodeDouble(key, 0.00)

    override fun getString(key: String): String? = _mmkv.decodeString(key, "")

    override fun getByteArray(key: String): ByteArray? = _mmkv.decodeBytes(key, null)

    override fun <T : Parcelable> getParcelable(key: String, tClass: Class<T>): T? =
        _mmkv.decodeParcelable(key, tClass, null)

    override fun removeKey(key: String) {
        _mmkv.removeValueForKey(key)
    }

    override fun clearAll() {
        _mmkv.clearAll()
    }
}
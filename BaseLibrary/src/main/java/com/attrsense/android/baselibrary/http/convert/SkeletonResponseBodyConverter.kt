package com.attrsense.android.baselibrary.http.convert

import com.attrsense.android.baselibrary.http.AttrException
import com.google.gson.Gson
import com.google.gson.TypeAdapter
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Converter

/**
 * @author zhangshuai@attrsense.com
 * @date 2022/11/2 15:19
 * @description
 */
class SkeletonResponseBodyConverter<T> constructor(
    private val gson: Gson,
    private val adapter: TypeAdapter<T>
) : Converter<ResponseBody, T> {

    override fun convert(value: ResponseBody): T {
        val jsonString = value.string()
        try {
            val obj = JSONObject(jsonString)
            val code = obj.getString("errorCode")
            if (code != "200") {
                val message = obj.getString("message")
                throw AttrException(code, errorMsg = message, Throwable(message))
            }
            return adapter.fromJson(obj.getString("data"))
        } catch (e: Exception) {
            e.printStackTrace()
            throw AttrException(cause = e)
        } finally {
            value.close()
        }
    }
}
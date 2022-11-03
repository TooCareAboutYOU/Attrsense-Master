package com.attrsense.android.baselibrary.http.convert

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type

/**
 * @author zhangshuai
 * @date 2022/11/2 15:58
 * @description 自定义转换器，状态码统一处理
 */
class SkeletonConverterFactory(private val gson: Gson) : Converter.Factory() {

    companion object {

        @JvmStatic
        fun create(): SkeletonConverterFactory {
            return create(Gson())
        }

        @JvmStatic
        fun create(gson: Gson?): SkeletonConverterFactory {
            if (gson == null) throw NullPointerException("gson == null")
            return SkeletonConverterFactory(gson)
        }
    }

    override fun responseBodyConverter(
        type: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit
    ): Converter<ResponseBody, *> {
        val adapter = gson.getAdapter(TypeToken.get(type))
        return SkeletonResponseBodyConverter(gson, adapter)
    }
}
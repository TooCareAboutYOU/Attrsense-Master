package com.attrsense.android.baselibrary.http.interceptor

import com.attrsense.android.baselibrary.http.anno.SpecificTimeout
import okhttp3.Interceptor
import okhttp3.Response
import retrofit2.Invocation

/**
 * @author zhangshuai@attrsense.com
 * @date 2023/1/9 11:16
 * @description 自定义okhttp超时时间
 */
internal class TimeOutInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        request.tag(Invocation::class.java)
            ?.method()
            ?.getAnnotation(SpecificTimeout::class.java)
            ?.let {
                return chain.withConnectTimeout(it.duration, it.unit)
                    .withReadTimeout(it.duration, it.unit)
                    .withConnectTimeout(it.duration, it.unit)
                    .proceed(request)
            } ?: kotlin.run {
            return chain.proceed(request)
        }
    }
}
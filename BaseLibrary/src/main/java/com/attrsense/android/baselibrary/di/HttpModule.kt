package com.attrsense.android.baselibrary.di

import android.annotation.SuppressLint
import com.attrsense.android.baselibrary.BuildConfig
import com.attrsense.android.baselibrary.config.AppConfig
import com.attrsense.android.baselibrary.http.HttpDns
import com.attrsense.android.baselibrary.http.HttpEventListener
import com.attrsense.android.baselibrary.util.JsonUtil
import com.attrsense.android.baselibrary.util.JsonUtil.decodeUnicode
import com.blankj.utilcode.util.JsonUtils
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.orhanobut.logger.Logger
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.security.SecureRandom
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.inject.Singleton
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.X509TrustManager


/**
 * author : zhangshuai@attrsense.com
 * date : 2022/10/8 14:02
 * mark : custom something
 */
@Module
@InstallIn(SingletonComponent::class)
object HttpModule {

    private const val TIME_OUT_30S = 30L
    private const val TIME_OUT_20S = 20L
    private val TIME_OUT = if (BuildConfig.DEBUG) TIME_OUT_30S else TIME_OUT_20S

    @Singleton
    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        val interceptorLogger = HttpLoggingInterceptor(HttpLogger()).apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        return OkHttpClient.Builder()
            .writeTimeout(TIME_OUT, TimeUnit.SECONDS)
            .connectTimeout(TIME_OUT, TimeUnit.SECONDS)
            .readTimeout(TIME_OUT, TimeUnit.SECONDS)
            .addNetworkInterceptor(interceptorLogger)
            .eventListenerFactory(HttpEventListener.FACTORY)
            .dns(HttpDns())
            .sslSocketFactory(createSSLSocketFactory()!!, trustManager).build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(AppConfig.BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createAsync())
            .addCallAdapterFactory(CoroutineCallAdapterFactory.invoke())
            .build()
    }

    private class HttpLogger : HttpLoggingInterceptor.Logger {
        private val mMessage = java.lang.StringBuilder()
        override fun log(result: String) {
            // 请求或者响应开始
            var message: String? = result
            if (message!!.startsWith("--> POST") || message.startsWith("--> GET")) {
                mMessage.setLength(0)
            }
            // 以{}或者[]形式的说明是响应结果的json数据，需要进行格式化
            if (message.startsWith("{") && message.endsWith("}")
                || message.startsWith("[") && message.endsWith("]")
            ) {
                message = JsonUtils.formatJson(message)
//                JsonUtil.formatJson(decodeUnicode(message))
            }
            mMessage.append(
                """
                $message
                
                """.trimIndent()
            )
            // 响应结束，打印整条日志
            if (message!!.startsWith("<-- END HTTP")) {
                Logger.i(mMessage.toString())
            }
        }
    }

    private fun createSSLSocketFactory(): SSLSocketFactory? {
        var ssfFactory: SSLSocketFactory? = null
        try {
            val sc = SSLContext.getInstance("TLS")
            sc.init(null, arrayOf(trustManager), SecureRandom())
            ssfFactory = sc.socketFactory
        } catch (e: Exception) {
            Logger.e("HttpModule::createSSLSocketFactory: $e")
        }
        return ssfFactory
    }

    private val trustManager: X509TrustManager = @SuppressLint("CustomX509TrustManager")
    object : X509TrustManager {
        @SuppressLint("TrustAllX509TrustManager")
        override fun checkClientTrusted(p0: Array<out X509Certificate>?, p1: String?) {
        }

        @SuppressLint("TrustAllX509TrustManager")
        override fun checkServerTrusted(p0: Array<out X509Certificate>?, p1: String?) {
        }

        override fun getAcceptedIssuers(): Array<X509Certificate?> = arrayOfNulls(0)

    }
}
package com.attrsense.android.baselibrary.di

import android.annotation.SuppressLint
import com.attrsense.android.baselibrary.BuildConfig
import com.attrsense.android.baselibrary.config.BaseConfig
import com.attrsense.android.baselibrary.http.HttpDns
import com.attrsense.android.baselibrary.http.HttpEventListener
import com.blankj.utilcode.util.JsonUtils
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.localebro.okhttpprofiler.OkHttpProfilerInterceptor
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
import javax.net.ssl.*


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
        val builder = OkHttpClient.Builder()
            .writeTimeout(TIME_OUT, TimeUnit.SECONDS)
            .connectTimeout(TIME_OUT, TimeUnit.SECONDS)
            .readTimeout(TIME_OUT, TimeUnit.SECONDS)
            .addNetworkInterceptor(interceptorLogger)
            .eventListenerFactory(HttpEventListener.FACTORY)
            .dns(HttpDns())
            .sslSocketFactory(createSSLSocketFactory()!!, trustManager)
            .hostnameVerifier(hostnameVerifier)
        if (BuildConfig.DEBUG) {
            builder.addInterceptor(OkHttpProfilerInterceptor())
        }
        return builder.build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BaseConfig.BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createAsync())
            .addCallAdapterFactory(CoroutineCallAdapterFactory.invoke())
            .build()
    }


    private class HttpLogger : HttpLoggingInterceptor.Logger {
        private val mMessage = java.lang.StringBuilder()
        override fun log(message: String) {
            // ????????????????????????
            var localMsg: String? = message
            if (localMsg!!.startsWith("--> POST") || localMsg.startsWith("--> GET") || localMsg.startsWith(
                    "--> PUT"
                )
            ) {
                mMessage.setLength(0)
            }
            // ???{}??????[]?????????????????????????????????json??????????????????????????????
            if (localMsg.startsWith("{") && localMsg.endsWith("}")
                || localMsg.startsWith("[") && localMsg.endsWith("]")
            ) {
                localMsg = JsonUtils.formatJson(localMsg)
            }
            mMessage.append("$localMsg\n")
            // ?????????????????????????????????
            if (localMsg!!.startsWith("<-- END HTTP")) {
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

    private val hostnameVerifier = HostnameVerifier { _, _ -> true }
}
package com.attrsense.android.baselibrary.di

import android.annotation.SuppressLint
import com.attrsense.android.baselibrary.BuildConfig
import com.attrsense.android.baselibrary.http.HttpDns
import com.attrsense.android.baselibrary.http.HttpEventListener
import com.attrsense.android.baselibrary.http.convert.SkeletonConverterFactory
import com.attrsense.android.baselibrary.util.MMKVUtils
import com.attrsense.android.baselibrary.util.StringUtils
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.localebro.okhttpprofiler.OkHttpProfilerInterceptor
import com.orhanobut.logger.Logger
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
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

    private val TIME_OUT = if (BuildConfig.DEBUG) 20L else 25L

    @Singleton
    @Provides
    fun provideOkHttpClient(mmkvUtils: MMKVUtils): OkHttpClient.Builder {
        val interceptorLogger = HttpLoggingInterceptor(HttpLogger()).apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        return OkHttpClient.Builder()
            .writeTimeout(TIME_OUT, TimeUnit.SECONDS)
            .connectTimeout(TIME_OUT, TimeUnit.SECONDS)
            .readTimeout(TIME_OUT, TimeUnit.SECONDS)
            .eventListenerFactory(HttpEventListener.FACTORY)
            .sslSocketFactory(createSSLSocketFactory()!!, trustManager)
            .hostnameVerifier(hostnameVerifier)
            .dns(HttpDns()).apply {
//                addInterceptor(HeadInterceptor(mmkvUtils))
                if (BuildConfig.DEBUG) {
                    addInterceptor(interceptorLogger)
                    addInterceptor(OkHttpProfilerInterceptor())
                }
            }
    }

    private class HeadInterceptor(private val mmkvUtils: MMKVUtils) : Interceptor {
        private val Authorization = "Authorization2"

        override fun intercept(chain: Interceptor.Chain): Response {
            val request = chain.request()
            val builder = request.newBuilder().apply {
                val token = mmkvUtils.getString("key_user_token")
                token?.let {
                    val value = StringUtils.encodeHeadInfo(it)
                    addHeader(Authorization, value)
                }
            }
            return chain.proceed(builder.build())
        }
    }

    @Singleton
    @Provides
    fun provideRetrofit(client: OkHttpClient.Builder): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(client.build())
            .addConverterFactory(SkeletonConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory.invoke())
            .build()
    }

    private class HttpLogger : HttpLoggingInterceptor.Logger {

        private val gson: Gson by lazy { GsonBuilder().setPrettyPrinting().create() }
        private val jsonParser: JsonParser by lazy { JsonParser() }
        private val mMessage = java.lang.StringBuilder()

        override fun log(message: String) {
            try {
                // 请求或者响应开始
                var localMsg: String? = message
                if (localMsg!!.startsWith("--> POST")
                    || localMsg.startsWith("--> GET")
                    || localMsg.startsWith("--> PUT")
                    || localMsg.startsWith("--> DELETE")
                ) {
                    mMessage.setLength(0)
                }

                // 以{}或者[]形式的说明是响应结果的json数据，需要进行格式化
                if ((localMsg.startsWith("{") && localMsg.startsWith("}", localMsg.length - 2))
                    || (localMsg.startsWith("[") && localMsg.startsWith("]", localMsg.length - 2))
                ) {
                    localMsg = gson.toJson(jsonParser.parse(localMsg).asJsonObject)
                }
                mMessage.append("$localMsg\n")
                // 响应结束，打印整条日志
                if (localMsg!!.startsWith("<-- END HTTP")) {
                    Logger.i(mMessage.toString())
                }
            } catch (e: Exception) {
                e.printStackTrace()
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
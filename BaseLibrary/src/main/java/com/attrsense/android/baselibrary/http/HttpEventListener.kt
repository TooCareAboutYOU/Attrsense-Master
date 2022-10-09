package com.attrsense.android.baselibrary.http

import okhttp3.*
import okhttp3.EventListener
import okhttp3.EventListener.Factory
import org.json.JSONObject
import java.io.IOException
import java.net.InetAddress
import java.net.InetSocketAddress
import java.net.Proxy
import java.util.*
import java.util.concurrent.atomic.AtomicLong

/**
 * author: zhangshuai
 * email: zhangshuai@attrsense.com
 * mark:
 *      callId每次请求的标识
 *      callStartNanos每次请求到开始时间
 */
internal class HttpEventListener(
    private val callId: Long,
    private val url: HttpUrl,
    private val callStartNanos: Long
) : EventListener() {

    companion object {
        private val nextCallId = AtomicLong(1L)

        @JvmStatic
        val FACTORY = Factory {
            return@Factory HttpEventListener(
                nextCallId.getAndIncrement(),
                it.request().url,
                System.nanoTime()
            )
        }
    }

    private fun recordEventLog(name: String) {
        val elapseNanos = System.nanoTime() - callStartNanos
        val sbLog = StringBuilder(url.toString()).append(" ").append(callId)
        sbLog.append(
            java.lang.String.format(
                Locale.CHINA,
                "%.3f-%s",
                elapseNanos / 1000000000.0,
                name
            )
        )
    }

    override fun callStart(call: Call) {
        super.callStart(call)
        recordEventLog("callStart")
    }

    override fun dnsStart(call: Call, domainName: String) {
        super.dnsStart(call, domainName)
        recordEventLog("dnsStart: $domainName")
    }

    override fun dnsEnd(
        call: Call,
        domainName: String,
        inetAddressList: List<@JvmSuppressWildcards InetAddress>
    ) {
        super.dnsEnd(call, domainName, inetAddressList)
        inetAddressList.forEach {
            recordEventLog("dnsEnd：${it.hostName}, ${it.hostAddress}")
        }
    }

    override fun connectStart(call: Call, inetSocketAddress: InetSocketAddress, proxy: Proxy) {
        super.connectStart(call, inetSocketAddress, proxy)
        recordEventLog("connectStart")
    }

    override fun secureConnectStart(call: Call) {
        super.secureConnectStart(call)
        recordEventLog("secureConnectStart")
    }

    override fun secureConnectEnd(call: Call, handshake: Handshake?) {
        super.secureConnectEnd(call, handshake)
        recordEventLog("secureConnectEnd")
    }

    override fun connectEnd(
        call: Call,
        inetSocketAddress: InetSocketAddress,
        proxy: Proxy,
        protocol: Protocol?
    ) {
        super.connectEnd(call, inetSocketAddress, proxy, protocol)
        recordEventLog("connectEnd：${inetSocketAddress.address.hostAddress}，${proxy.address()}")
    }

    override fun connectFailed(
        call: Call,
        inetSocketAddress: InetSocketAddress,
        proxy: Proxy,
        protocol: Protocol?,
        ioe: IOException
    ) {
        super.connectFailed(call, inetSocketAddress, proxy, protocol, ioe)
        recordEventLog("connectFailed：")
    }

    override fun connectionAcquired(call: Call, connection: Connection) {
        super.connectionAcquired(call, connection)
        val mJsonObject = JSONObject().apply {
            //获取http协议版本
            put("protocol", connection.protocol().toString())
            //获取IP地址
            put(
                "socketIP",
                "${connection.socket().inetAddress.hostName}, ${connection.socket().inetAddress.hostAddress}，${connection.socket().localAddress.hostAddress}"
            )
            //获取tls版本
            put("tlsVersion", connection.handshake()?.tlsVersion?.javaName)
        }
        recordEventLog("connectionAcquired：$mJsonObject")
    }

    override fun connectionReleased(call: Call, connection: Connection) {
        super.connectionReleased(call, connection)
        recordEventLog("connectionReleased")
    }

    override fun requestHeadersStart(call: Call) {
        super.requestHeadersStart(call)
        recordEventLog("requestHeadersStart")
    }

    override fun requestHeadersEnd(call: Call, request: Request) {
        super.requestHeadersEnd(call, request)
        recordEventLog("requestHeadersEnd：")
    }

    override fun requestBodyStart(call: Call) {
        super.requestBodyStart(call)
        recordEventLog("requestBodyStart")
    }

    override fun requestBodyEnd(call: Call, byteCount: Long) {
        super.requestBodyEnd(call, byteCount)
        recordEventLog("requestBodyEnd")
    }

    override fun responseHeadersStart(call: Call) {
        super.responseHeadersStart(call)
        recordEventLog("responseHeadersStart")
    }

    override fun responseHeadersEnd(call: Call, response: Response) {
        super.responseHeadersEnd(call, response)
        recordEventLog("responseHeadersEnd")
    }

    override fun responseBodyStart(call: Call) {
        super.responseBodyStart(call)
        recordEventLog("responseBodyStart")
    }

    override fun responseBodyEnd(call: Call, byteCount: Long) {
        super.responseBodyEnd(call, byteCount)
        recordEventLog("responseBodyEnd")
    }

    override fun callEnd(call: Call) {
        super.callEnd(call)
        recordEventLog("callEnd：请求完成：")
    }

    override fun callFailed(call: Call, ioe: IOException) {
        super.callFailed(call, ioe)
        recordEventLog("callFailed：请求异常：$ioe")
    }
}
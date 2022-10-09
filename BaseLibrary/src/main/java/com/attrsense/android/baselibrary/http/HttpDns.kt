package com.attrsense.android.baselibrary.http

import com.orhanobut.logger.Logger
import okhttp3.Dns
import java.net.Inet4Address
import java.net.InetAddress
import java.net.UnknownHostException
import java.security.SecureRandom
import java.security.cert.X509Certificate
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.X509TrustManager

/**
 * author : zhangshuai@attrsense.com
 * date : 2022/10/9 9:49
 * mark : 自定义 OKHTTP.DNS
 */
internal class HttpDns : Dns {

    override fun lookup(hostname: String): List<InetAddress> {
        return try {
            val mInetAddressesList: MutableList<InetAddress> = ArrayList()
            val mInetAddresses = InetAddress.getAllByName(
                hostname
            )
            if (mInetAddresses.size > 1) {
                for (mInetAddress in mInetAddresses) {
                    if (mInetAddress is Inet4Address) {
                        mInetAddressesList.add(mInetAddress)
                    } else {
                        mInetAddressesList.add(0, mInetAddress)
                    }
                }
            } else {
                for (address in mInetAddresses) {
                    if (address is Inet4Address) {
                        mInetAddressesList.add(0, address)
                    } else {
                        mInetAddressesList.add(address)
                    }
                }
            }
            mInetAddressesList
        } catch (var4: NullPointerException) {
            val unknownHostException = UnknownHostException(
                "Broken system behaviour"
            )
            unknownHostException.initCause(var4)
            throw unknownHostException
        }
    }

}
package com.attrsense.android.baselibrary.util

/**
 * @author zhangshuai@attrsense.com
 * @date 2022/11/16 10:12
 * @description
 */
object StringUtils {

    /**
     * 处理okhttp请求头编码
     * @param headInfo String
     * @return String
     */
    fun encodeHeadInfo(headInfo: String): String {
        val stringBuffer = StringBuffer()
        var i = 0
        val length = headInfo.length
        while (i < length) {
            val c = headInfo[i]
            if (c <= '\u001f' || c >= '\u007f') {
                stringBuffer.append(String.format("\\u%04x", c.code))
            } else {
                stringBuffer.append(c)
            }
            i++
        }
        return stringBuffer.toString()
    }
}
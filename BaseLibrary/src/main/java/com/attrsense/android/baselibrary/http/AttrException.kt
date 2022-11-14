package com.attrsense.android.baselibrary.http



/**
 * @author zhangshuai@attrsense.com
 * @date 2022/11/2 15:48
 * @description
 */
class AttrException constructor(
    private var errorCode: String? = null,
    private var errorMsg: String? = null,
    cause: Throwable? = null
) : RuntimeException(errorMsg, cause) {

    fun getErrorMsg(): String? {
        return errorMsg
    }
}
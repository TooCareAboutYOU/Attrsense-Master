package com.attrsense.android.baselibrary.base.open.livedata

import androidx.lifecycle.MutableLiveData
import com.attrsense.android.baselibrary.base.open.model.BaseResponse
import com.attrsense.android.baselibrary.base.open.model.ResponseData

/**
 * @author zhangshuai@attrsense.com
 * @date 2022/11/2 18:37
 * @description
 */
class ResponseBaseMutableLiveData<T> : MutableLiveData<ResponseData<BaseResponse<T>>> {

    constructor() : super()

    constructor(value: ResponseData<BaseResponse<T>>) : super(value)

}
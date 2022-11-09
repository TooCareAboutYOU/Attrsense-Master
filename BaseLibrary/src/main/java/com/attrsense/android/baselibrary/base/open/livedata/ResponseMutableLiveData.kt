package com.attrsense.android.baselibrary.base.open.livedata

import androidx.lifecycle.MutableLiveData
import com.attrsense.android.baselibrary.base.open.model.ResponseData

/**
 * @author zhangshuai
 * @date 2022/11/2 18:37
 * @description
 */
class ResponseMutableLiveData<T> : MutableLiveData<ResponseData<T>> {

    constructor() : super()

    constructor(value: ResponseData<T>) : super(value)

}
package com.attrsense.android.baselibrary.base.open.livedata

import androidx.lifecycle.LiveData
import com.attrsense.android.baselibrary.base.open.model.ResponseData

/**
 * @author zhangshuai@attrsense.com
 * @date 2022/11/2 18:34
 * @description
 */
abstract class ResponseLiveData<T> : LiveData<ResponseData<T>> {

    constructor() : super()

    constructor(value: ResponseData<T>) : super(value)

}
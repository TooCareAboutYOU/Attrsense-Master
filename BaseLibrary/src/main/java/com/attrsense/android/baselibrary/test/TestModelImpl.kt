package com.attrsense.android.baselibrary.test

import android.util.Log
import javax.inject.Inject

/**
 * author : zhangshuai@attrsense.com
 * date : 2022/10/11 12:46
 * mark : custom something
 */

interface Event {
    fun load()
}

class TestModelImpl @Inject constructor() : Event {

    override fun load() {
        Log.i("printInfo", "TestModelImpl::load: ")
    }
}
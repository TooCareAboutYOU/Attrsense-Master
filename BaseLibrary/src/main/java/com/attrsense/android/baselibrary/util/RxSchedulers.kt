package com.attrsense.android.baselibrary.util

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableTransformer
import io.reactivex.rxjava3.schedulers.Schedulers

/**
 * @author zhangshuai
 * @date 2022/11/2 09:36
 * @description
 */
object RxSchedulers {
    /**
     * 统一线程处理
     * 简化线程
     */
    fun <T> rxObservableScheduler(): ObservableTransformer<T, T> {
        return ObservableTransformer { observable: Observable<T> ->
            observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
        }
    }
}
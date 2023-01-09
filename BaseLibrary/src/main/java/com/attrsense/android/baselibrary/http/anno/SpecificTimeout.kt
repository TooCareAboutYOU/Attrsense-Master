package com.attrsense.android.baselibrary.http.anno

import java.util.concurrent.TimeUnit

/**
 * @author zhangshuai@attrsense.com
 * @date 2023/1/9 11:14
 * @description
 */
@Retention
@Target(allowedTargets = [AnnotationTarget.FUNCTION])
annotation class SpecificTimeout(val duration:Int,val unit:TimeUnit)

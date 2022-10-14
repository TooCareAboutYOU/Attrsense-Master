package com.attrsense.android

import org.gradle.api.JavaVersion


/**
 * 应用版本控制
 */
// TODO:  修改文件后必须 Rebuild project
object Versions {
    //应用配置
    object AndroidConfig {
        const val applicationId = "com.attrsense.android"
        const val compileSdkVersion = 32
        const val targetSdkVersion = 31
        const val minSdkVersion = 21
        const val versionCode = 1
        const val versionName = "1.0.0"
        val sourceCompatibilityVersion = JavaVersion.VERSION_1_8
        val targetCompatibilityVersion = JavaVersion.VERSION_1_8
        const val jvmTargetVersion = "1.8"
        const val cmakeVersion = "3.18.1"
        const val multiDexEnabled = true
    }

    //测试库
    object Test {
        const val junitVersion = "4.13.2"
        const val junitExtVersion = "1.1.3"
        const val espressoVersion = "3.4.0"
        const val mockkVersion = "1.12.3"
        const val debugDBVersion = "1.0.6"
        const val leakcanaryAndroidVersion = "2.8.1"
        const val okHttpProfiler = "1.0.8"
        const val legacySupportVersion = "1.0.0"
    }

    //基础库
    object BaseLibrary {
        const val multidexVersion = "2.0.1"
        const val coreKtxVersion = "1.8.0"
        const val appcompatVersion = "1.5.0"
        const val materialVersion = "1.6.1"
        const val constraintLayoutVersion = "2.1.4"
        const val swipeRefreshLayoutVersion = "1.1.0"
        const val recyclerviewVersion = "1.2.1"
        const val BaseRecyclerViewAdapterHelperVersion = "3.0.8"
        const val cardViewVersion = "1.0.0"
        const val viewpager2Version = "1.0.0"
        const val splashscreenVersion = "1.0.0-alpha02"
        const val coroutinesKtxVersion = "1.5.1"
        const val coroutinesKtxRx2Version = "1.6.1"
        const val serializationJsonKtxVersion = "1.3.2"
        const val reflectKtx = "1.7.10"
        const val roomVersion = "2.4.3"
        const val navigationVersion = "2.4.2"
        const val pagingVersion = "3.1.1"
    }

    //第三方库
    object ExtLibrary {
        const val hiltDaggerVersion = "2.44"
        const val hiltVersion = "1.0.0"
        const val aRouterVersion = "1.5.2"
        const val rxjavaVersion = "3.1.5"
        const val rxAndroidVersion = "3.0.0"
        const val rxBindingVersion = "4.0.0"
        const val rxLifecycleVersion = "2.2.2"
        const val lifecycleVersion = "2.5.1"
        const val activityKtxVersion = "1.4.0"
        const val fragmentKtxVersion = "1.4.1"
        const val retrofit2Version = "2.9.0"
        const val okhttpLoggingInterceptorVersion = "4.9.3"
        const val retrofit2CoroutinesAdapterKtxVersion = "0.9.2"
        const val photoViewVersion = "2.3.0"
        const val circleImageViewVersion = "3.0.1"
        const val immersionBarVersion = "3.2.2"
        const val systemBarTintVersion = "1.0.3"
        const val aspectJrtVersion = "1.9.4"
        const val rxPermissionsVersion = "0.12"
        const val reactiveNetworkRx2Version = "3.0.8"
        const val loggerVersion = "2.2.0"
        const val utilCodexVersion = "1.31.0"
        const val mmkvVersion = "1.2.14"
    }
}

object Deps {
    val applicationId = Versions.AndroidConfig.applicationId
    val compileSdk = Versions.AndroidConfig.compileSdkVersion
    val targetSdk = Versions.AndroidConfig.targetSdkVersion
    val minSdk = Versions.AndroidConfig.minSdkVersion
    val versionCode = Versions.AndroidConfig.versionCode
    val versionName = Versions.AndroidConfig.versionName
    val sourceCompatibility = Versions.AndroidConfig.sourceCompatibilityVersion
    val targetCompatibility = Versions.AndroidConfig.targetCompatibilityVersion
    val jvmTarget = Versions.AndroidConfig.jvmTargetVersion
    val cmakeVersion = Versions.AndroidConfig.cmakeVersion
    val multiDexEnabled = Versions.AndroidConfig.multiDexEnabled


    val junit = "junit:junit:${Versions.Test.junitVersion}"
    val jUnitExt = "androidx.test.ext:junit:${Versions.Test.junitExtVersion}"
    val espressoCore = "androidx.test.ext:junit:${Versions.Test.espressoVersion}"

    //https://github.com/mockk/mockk
    val mockk = "io.mockk:mockk:${Versions.Test.mockkVersion}"

    //Debug数据库 https://github.com/amitshekhariitbhu/Android-Debug-Database
    val debugDB = "com.amitshekhar.android:debug-db:${Versions.Test.debugDBVersion}"

    //内存检测
    val leakcanaryAndroid =
        "com.squareup.leakcanary:leakcanary-android:${Versions.Test.leakcanaryAndroidVersion}"
    val okHttpProfiler = "com.localebro:okhttpprofiler:${Versions.Test.okHttpProfiler}"

    val legacySupportV4 = "androidx.legacy:legacy-support-v4:${Versions.Test.legacySupportVersion}"

    //https://developer.android.google.cn/studio/build/multidex?hl=zh_cn
    val multidex = "androidx.multidex:multidex:${Versions.BaseLibrary.multidexVersion}"
    val coreKtx = "androidx.core:core-ktx:${Versions.BaseLibrary.coreKtxVersion}"
    val appcompat = "androidx.appcompat:appcompat:${Versions.BaseLibrary.appcompatVersion}"
    val material = "com.google.android.material:material:${Versions.BaseLibrary.materialVersion}"
    val constraintLayout =
        "androidx.constraintlayout:constraintlayout:${Versions.BaseLibrary.constraintLayoutVersion}"

    //https://github.com/CymChad/BaseRecyclerViewAdapterHelper
    val swipeRefreshLayout =
        "androidx.swiperefreshlayout:swiperefreshlayout:${Versions.BaseLibrary.swipeRefreshLayoutVersion}"
    val recyclerview =
        "androidx.recyclerview:recyclerview:${Versions.BaseLibrary.recyclerviewVersion}"
    val baseRecyclerViewAdapterHelper =
        "com.github.CymChad:BaseRecyclerViewAdapterHelper:${Versions.BaseLibrary.BaseRecyclerViewAdapterHelperVersion}"
    val cardView = "androidx.cardview:cardview:${Versions.BaseLibrary.cardViewVersion}"
    val viewpager2 = "androidx.viewpager2:viewpager2:${Versions.BaseLibrary.viewpager2Version}"

    //splashscreen 启动页
    val splashscreen = "androidx.core:core-splashscreen:${Versions.BaseLibrary.splashscreenVersion}"

    //协程 coroutines
    val coroutinesCoreKtx =
        "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.BaseLibrary.coroutinesKtxVersion}"
    val coroutinesAndroidKtx =
        "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.BaseLibrary.coroutinesKtxVersion}"
    val coroutinesRx2Ktx =
        "org.jetbrains.kotlinx:kotlinx-coroutines-rx2:${Versions.BaseLibrary.coroutinesKtxRx2Version}"

    //kotlin序列化
    val serializationJsonKtx =
        "org.jetbrains.kotlinx:kotlinx-serialization-json:${Versions.BaseLibrary.serializationJsonKtxVersion}"

    val reflectKtx = "org.jetbrains.kotlin:kotlin-reflect:${Versions.BaseLibrary.reflectKtx}"

    //room - https://developer.android.google.cn/jetpack/androidx/releases/room
    val roomKtx = "androidx.room:room-ktx:${Versions.BaseLibrary.roomVersion}"
    val roomRuntime = "androidx.room:room-runtime:${Versions.BaseLibrary.roomVersion}"
    val roomCompiler = "androidx.room:room-compiler:${Versions.BaseLibrary.roomVersion}"

    val roomPaging = "androidx.room:room-paging:${Versions.BaseLibrary.roomVersion}"

    //navigation - https://developer.android.google.cn/jetpack/androidx/releases/navigation
    val navigationFragmentKtx =
        "androidx.navigation:navigation-fragment-ktx:${Versions.BaseLibrary.navigationVersion}"
    val navigationUiKtx =
        "androidx.navigation:navigation-ui-ktx:${Versions.BaseLibrary.navigationVersion}"

    //Feature module Support
    val navigationDynamicFeaturesFragment =
        "androidx.navigation:navigation-dynamic-features-fragment:${Versions.BaseLibrary.navigationVersion}"

    //paging - https://developer.android.google.cn/jetpack/androidx/releases/paging
    val pagingRuntime = "androidx.paging:paging-runtime:${Versions.BaseLibrary.pagingVersion}"

    // alternatively - without Android dependencies for tests
    val pagingCommon = "androidx.paging:paging-common:${Versions.BaseLibrary.pagingVersion}"

    // optional - RxJava2 support
    val pagingRxjava2 = "androidx.paging:paging-rxjava2:${Versions.BaseLibrary.pagingVersion}"

    //https://developer.android.google.cn/codelabs/android-hilt?hl=zh_cn#0
    val hiltDaggerAndroid =
        "com.google.dagger:hilt-android:${Versions.ExtLibrary.hiltDaggerVersion}"
    val hiltDaggerCompiler =
        "com.google.dagger:hilt-compiler:${Versions.ExtLibrary.hiltDaggerVersion}"

    //Hilt与WorkManager集成
    val hiltWork = "androidx.hilt:hilt-work:${Versions.ExtLibrary.hiltVersion}"
    val hiltCompiler = "androidx.hilt:hilt-compiler:${Versions.ExtLibrary.hiltVersion}"

    //Hilt与Navigation 库集成
    val hiltNavigationFragment =
        "androidx.hilt:hilt-navigation-fragment:${Versions.ExtLibrary.hiltVersion}"

    //https://github.com/alibaba/ARouter/blob/master/README_CN.md
    val arouterApi = "com.alibaba:arouter-api:${Versions.ExtLibrary.aRouterVersion}"
    val arouterCompiler = "com.alibaba:arouter-compiler:${Versions.ExtLibrary.aRouterVersion}"

    val rxjava = "io.reactivex.rxjava3:rxjava:${Versions.ExtLibrary.rxjavaVersion}"
    val rxAndroid = "io.reactivex.rxjava3:rxandroid:${Versions.ExtLibrary.rxAndroidVersion}"
    val rxBinding = "com.jakewharton.rxbinding4:rxbinding:${Versions.ExtLibrary.rxBindingVersion}"

    /**
    implementation 'com.jakewharton.rxbinding4:rxbinding-core:4.0.0'
    implementation 'com.jakewharton.rxbinding4:rxbinding-appcompat:4.0.0'
    implementation 'com.jakewharton.rxbinding4:rxbinding-drawerlayout:4.0.0'
    implementation 'com.jakewharton.rxbinding4:rxbinding-leanback:4.0.0'
    implementation 'com.jakewharton.rxbinding4:rxbinding-recyclerview:4.0.0'
    implementation 'com.jakewharton.rxbinding4:rxbinding-slidingpanelayout:4.0.0'
    implementation 'com.jakewharton.rxbinding4:rxbinding-swiperefreshlayout:4.0.0'
    implementation 'com.jakewharton.rxbinding4:rxbinding-viewpager:4.0.0'
    implementation 'com.jakewharton.rxbinding4:rxbinding-viewpager2:4.0.0
     */

    val rxLifecycle =
        "com.trello.rxlifecycle2:rxlifecycle:${Versions.ExtLibrary.rxLifecycleVersion}"
    val rxLifecycleAndroid =
        "com.trello.rxlifecycle2:rxlifecycle-android:${Versions.ExtLibrary.rxLifecycleVersion}"
    val rxLifecycleComponents =
        "com.trello.rxlifecycle2:rxlifecycle-components:${Versions.ExtLibrary.rxLifecycleVersion}"

    //https://developer.android.google.cn/jetpack/androidx/releases/lifecycle
    //lifecycleScope, Lifecycles
    val lifecycleRuntimeKtx =
        "androidx.lifecycle:lifecycle-runtime-ktx:${Versions.ExtLibrary.lifecycleVersion}"

    ////viewModelScope, ViewModel
    val lifecycleViewModelKtx =
        "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.ExtLibrary.lifecycleVersion}"

    // Saved state module for ViewModel
    val lifecycleViewModelSavedState =
        "androidx.lifecycle:lifecycle-viewmodel-savedstate:${Versions.ExtLibrary.lifecycleVersion}"

    //livedata
    val lifecycleLivedataKtx =
        "androidx.lifecycle:lifecycle-livedata-ktx:${Versions.ExtLibrary.lifecycleVersion}"
    val lifecycleLivedataCoreKtx =
        "androidx.lifecycle:lifecycle-livedata-core-ktx:${Versions.ExtLibrary.lifecycleVersion}"

    //Annotation processor
    val lifecycleCompiler =
        "androidx.lifecycle:lifecycle-compiler:${Versions.ExtLibrary.lifecycleVersion}"

    //optional - helpers for implementing LifecycleOwner in a Service
    val lifecycleService =
        "androidx.lifecycle:lifecycle-service:${Versions.ExtLibrary.lifecycleVersion}"

    //optional - ProcessLifecycleOwner provides a lifecycle for the whole application process
    val lifecycleProcess =
        "androidx.lifecycle:lifecycle-process:${Versions.ExtLibrary.lifecycleVersion}"

    //optional - ReactiveStreams support for LiveData
    val lifecycleReactivestreamsKtx =
        "androidx.lifecycle:lifecycle-reactivestreams-ktx:${Versions.ExtLibrary.lifecycleVersion}"

    //使用：by viewModels<__ViewModel>()
    val activityKtx = "androidx.activity:activity-ktx:${Versions.ExtLibrary.activityKtxVersion}"
    val fragmentKtx = "androidx.fragment:fragment-ktx:${Versions.ExtLibrary.fragmentKtxVersion}"

    //https://square.github.io/retrofit/
    val retrofit2 = "com.squareup.retrofit2:retrofit:${Versions.ExtLibrary.retrofit2Version}"
    val retrofit2ConverterGson =
        "com.squareup.retrofit2:converter-gson:${Versions.ExtLibrary.retrofit2Version}"
    val retrofit2AdapterRxjava2 =
        "com.squareup.retrofit2:adapter-rxjava2:${Versions.ExtLibrary.retrofit2Version}"
    val okhttp3LoggingInterceptor =
        "com.squareup.okhttp3:logging-interceptor:${Versions.ExtLibrary.okhttpLoggingInterceptorVersion}"

    //https://github.com/JakeWharton/retrofit2-kotlin-coroutines-adapter
    val retrofit2CoroutinesAdapterKtx =
        "com.jakewharton.retrofit:retrofit2-kotlin-coroutines-adapter:${Versions.ExtLibrary.retrofit2CoroutinesAdapterKtxVersion}"

    //图片展示 https://github.com/Baseflow/PhotoView
    val photoView = "com.github.chrisbanes:PhotoView:${Versions.ExtLibrary.photoViewVersion}"

    //裁剪圆形图片  https://github.com/hdodenhof/CircleImageView
    val circleImageview =
        "de.hdodenhof:circleimageview:${Versions.ExtLibrary.circleImageViewVersion}"

    //导航栏  https://github.com/gyf-dev/ImmersionBar
    val immersionBar =
        "com.geyifeng.immersionbar:immersionbar:${Versions.ExtLibrary.immersionBarVersion}"

    // 导航栏kotlin扩展
    val immersionBarKtx =
        "com.geyifeng.immersionbar:immersionbar-ktx:${Versions.ExtLibrary.immersionBarVersion}"

    //状态栏 https://github.com/jgilfelt/SystemBarTint
    val systemBarTint =
        "com.readystatesoftware.systembartint:systembartint:${Versions.ExtLibrary.systemBarTintVersion}"

    //面向切面
    val aspectJrt = "org.aspectj:aspectjrt:${Versions.ExtLibrary.aspectJrtVersion}"

    //动态申请权限 https://github.com/tbruyelle/RxPermissions
    val rxPermissions =
        "com.github.tbruyelle:rxpermissions:${Versions.ExtLibrary.rxPermissionsVersion}"

    //监听网络 https://github.com/pwittchen/ReactiveNetwork
    val reactiveNetwork =
        "com.github.pwittchen:reactivenetwork-rx2:${Versions.ExtLibrary.reactiveNetworkRx2Version}"

    //打印日志 https://github.com/orhanobut/logger
    val logger = "com.orhanobut:logger:${Versions.ExtLibrary.loggerVersion}"

    //工具类集合库 https://github.com/Blankj/AndroidUtilCode
    val utilCodex = "com.blankj:utilcodex:${Versions.ExtLibrary.utilCodexVersion}"

    //临时数据存储 https://github.com/Tencent/MMKV/blob/master/README_CN.md
    val mmkv = "com.tencent:mmkv:${Versions.ExtLibrary.mmkvVersion}"

}
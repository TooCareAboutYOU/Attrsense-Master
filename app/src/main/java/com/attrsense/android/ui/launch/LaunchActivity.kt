package com.attrsense.android.ui.launch

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.core.splashscreen.SplashScreen
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.attrsense.android.R
import com.attrsense.android.baselibrary.base.open.activity.BaseDataBindingVMActivity
import com.attrsense.android.databinding.ActivityLaunchBinding
import com.attrsense.android.ui.login.LoginActivity
import com.attrsense.android.ui.main.MainActivity
import com.attrsense.android.util.UserDataManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import java.util.concurrent.atomic.AtomicBoolean
import javax.inject.Inject

@AndroidEntryPoint
class LaunchActivity : BaseDataBindingVMActivity<ActivityLaunchBinding, LaunchViewModel>() {


    @Inject
    lateinit var userDataManager: UserDataManager

    private var keepScreen = AtomicBoolean(true)
    private lateinit var splashScreen: SplashScreen
    private var mJob: Job? = null

    override fun setLayoutResId(): Int = R.layout.activity_launch

    override fun setViewModel(): Class<LaunchViewModel> = LaunchViewModel::class.java

    override fun initViewBefore(savedInstanceState: Bundle?) {
        super.initViewBefore(savedInstanceState)
        splashScreen = installSplashScreen()
    }

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        launch()
        //展示完毕的监听
        splashScreen.setOnExitAnimationListener { provider ->
            Log.i("printInfo", "SplashActivity::setOnExitAnimationListener: 启动进程才会回调！")
            //移除监听
            provider.remove()
            //跳转到下个页面
        }

        mDataBinding.acTvNumber.setOnClickListener {
            jumpActivity()
        }
    }

    private fun launch() {
        lifecycleScope.launch {
            delay(1000L)
            keepScreen.compareAndSet(true, false)
//            loadTimer()
            jumpActivity()
        }
        //绑定数据
        splashScreen.setKeepVisibleCondition { keepScreen.get() }
    }

    override fun onPause() {
        super.onPause()
        mJob?.apply {
            if (isActive) {
                cancel()
            }
        }
        mJob = null
    }

    private val MAX_COUNT = 3

    /**
     * 三秒倒计时
     */
    private fun loadTimer() {
        mJob = (MAX_COUNT downTo 0).asFlow()
            .flowOn(Dispatchers.Default)
            .onStart { Log.i("printInfo", "flow onStart") }
            .onEach {
                it.setTimerText()
                delay(1000L)
            }
            .onCompletion {
                Log.i("printInfo", "flow onCompletion")
                jumpActivity()
            } //在发送数据收集完之后添加数据
            .launchIn(lifecycleScope) //在单独的协程中启动流的收集
    }

    private fun Int.setTimerText() {
        mDataBinding.acTvNumber.text = "${this}s"
    }

    private fun jumpActivity() {
        if (userDataManager.isLogin()) {
            toActivity(MainActivity::class.java)
        } else {
            toActivity(LoginActivity::class.java)
        }
    }

    private fun toActivity(clz: Class<*>) {
        startActivity(Intent(this@LaunchActivity, clz))
        finish()
        overridePendingTransition(0, 0)
    }
}
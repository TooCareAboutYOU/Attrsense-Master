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
import com.attrsense.android.manager.UserDataManager
import com.attrsense.ui.library.expand.singleClick
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


    override fun initViewBefore(savedInstanceState: Bundle?) {
        super.initViewBefore(savedInstanceState)
        splashScreen = installSplashScreen()
    }

    override fun initView(savedInstanceState: Bundle?) {
        mDataBinding.viewModel = mViewModel

        launch()

        mDataBinding.acTvNumber.singleClick {
            jumpActivity()
        }
    }

    private fun launch() {
//        lifecycleScope.launch {
//            delay(1000L)
//            keepScreen.compareAndSet(true, false)
//            loadTimer()
//        }

//        loadTimer()
        jumpActivity()

        //绑定数据 返回false即可
        splashScreen.setKeepVisibleCondition {
            val state = keepScreen.get()
            Log.i("print_logs", "LaunchActivity::launch: setKeepVisibleCondition $state")
            state
        }

        //展示完毕的监听
        splashScreen.setOnExitAnimationListener { provider ->
            Log.d("print_logs", "LaunchActivity::initView: setOnExitAnimationListener")
            //移除监听
            provider.remove()
            //跳转到下个页面
        }
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
        mJob = (MAX_COUNT downTo 0)
            .asFlow()
            .flowOn(Dispatchers.Default)
            .onStart {
                Log.i("print_logs", "LaunchActivity::loadTimer: onStart")
                keepScreen.compareAndSet(true, false)
            }
            .onEach {
                Log.i("print_logs", "LaunchActivity::loadTimer: onEach")
                it.setTimerText()
                delay(1000L)
            }.onCompletion { //在发送数据收集完之后添加数据
                Log.i("print_logs", "LaunchActivity::loadTimer: onCompletion")
//                jumpActivity()
            }
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
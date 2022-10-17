package com.attrsense.android.ui.launch

import android.content.Intent
import android.util.Log
import androidx.core.splashscreen.SplashScreen
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.attrsense.android.R
import com.attrsense.android.baselibrary.base.open.activity.BaseDataBindingVMActivity
import com.attrsense.android.databinding.ActivityLaunchBinding
import com.attrsense.android.ui.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import java.util.concurrent.atomic.AtomicBoolean

@AndroidEntryPoint
class LaunchActivity : BaseDataBindingVMActivity<ActivityLaunchBinding, LaunchViewModel>() {

    private var keepScreen = AtomicBoolean(true)
    private lateinit var splashScreen: SplashScreen
    private var mJob: Job? = null

    override fun setLayoutResId(): Int = R.layout.activity_launch

    override fun setViewModel(): Class<LaunchViewModel> = LaunchViewModel::class.java

    override fun initViewBefore() {
        splashScreen = installSplashScreen()
    }

    override fun initView() {
        super.initView()


        launch()


        mDataBinding.acTvNumber.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }

    private fun launch() {
        lifecycleScope.launch {
            delay(100L)
            keepScreen.compareAndSet(true, false)
        }
        //绑定数据
        splashScreen.setKeepVisibleCondition { keepScreen.get() }

        //展示完毕的监听
        splashScreen.setOnExitAnimationListener { provider ->
            Log.i("printInfo", "SplashActivity::setOnExitAnimationListener: 启动进程才会回调！")
            //移除监听
            provider.remove()
            //跳转到下个页面
//            startActivity(Intent(this, MainActivity::class.java))
//            finish()
//            overridePendingTransition(0,0)
        }
    }

    private var max = 3
    override fun onResume() {
        super.onResume()
        // TODO: 如果是登录用户直接跳转到首页
        max.setTimerText()
        loadTimer()
    }

    override fun onStop() {
        super.onStop()
        // TODO: 如果是登录用户直接跳转到首页
        max = 3
    }

    override fun onDestroy() {
        super.onDestroy()
        mJob?.apply {
            if (isActive) {
                cancel()
            }
        }
    }

    /**
     * 三秒倒计时
     */
    private fun loadTimer() {
        mJob = (max downTo 0).asFlow()
            .onEach { delay(1000L) }
            .flowOn(Dispatchers.Default)
            .onStart { Log.i("printInfo", "flow onStart") }
            .onEach {
                it.setTimerText()
                if (it == 0) {
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }
            }
            .onCompletion {
                Log.i("printInfo", "flow onCompletion")
            } //在发送数据收集完之后添加数据
            .launchIn(lifecycleScope) //在单独的协程中启动流的收集
    }

    private fun Int.setTimerText() {
        val result = "${this}s"
        mDataBinding.acTvNumber.text = result
    }

//    override fun onBackPressed() {
//        super.onBackPressed()
//        val intent = Intent(Intent.ACTION_MAIN).apply {
//            addCategory(Intent.CATEGORY_HOME)
//        }
//        Log.i("printInfo", "SplashActivity::onBackPressed: ")
//        startActivity(intent)
//    }
}
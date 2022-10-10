package com.attrsense.android.ui.splash

import android.app.Application
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.attrsense.android.R
import com.attrsense.android.baselibrary.base.open.SkeletonDataBindingBaseActivity
import com.attrsense.android.databinding.ActivitySplashBinding
import com.attrsense.android.http.ApiService
import com.attrsense.android.ui.main.MainActivity
import com.blankj.utilcode.util.ToastUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import java.util.concurrent.atomic.AtomicBoolean
import javax.inject.Inject

@AndroidEntryPoint
class SplashActivity : SkeletonDataBindingBaseActivity<ActivitySplashBinding>() {

    private var keepScreen = AtomicBoolean(true)
    private lateinit var splashScreen: SplashScreen

    private val splash2ViewModel: Splash2ViewModel by viewModels { ViewModelFactory(this) }

    inner class ViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
            return when (modelClass) {
                Splash2ViewModel::class.java -> {
                    //通过extras传递自定义参数
                    Splash2ViewModel(context)
                }
                else -> throw IllegalArgumentException("Unknown class $modelClass")
            } as T
        }
    }

    override fun setLayoutResId(): Int = R.layout.activity_splash

    override fun initViewBefore() {
        splashScreen = installSplashScreen()
    }

    override fun initView() {
        mDataBinding.acTv.setOnClickListener {
            splash2ViewModel.load("js")
        }
        splash2ViewModel.github().observe(this) {
            startActivity(Intent(this, MainActivity::class.java))
        }

        countDowntime()

        //展示完毕的监听
        splashScreen.setOnExitAnimationListener { provider ->
            //移除监听
            provider.remove()
            //跳转到下个页面
//            startActivity(Intent(this, MainActivity::class.java))
//            finish()
//            overridePendingTransition(0,0)
        }
    }

    //设置欢迎页展示时间
    fun countDowntime() {
        lifecycleScope.launchWhenResumed {
            delay(1000)
            keepScreen.compareAndSet(true, false)
        }
        //绑定数据
        splashScreen.setKeepVisibleCondition { keepScreen.get() }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(Intent.ACTION_MAIN).apply {
            addCategory(Intent.CATEGORY_HOME)
        }
        Log.i("printInfo", "SplashActivity::onBackPressed: ")
        startActivity(intent)
    }
}
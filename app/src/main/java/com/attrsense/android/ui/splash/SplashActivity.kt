package com.attrsense.android.ui.splash

import android.content.Intent
import androidx.core.splashscreen.SplashScreen
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.attrsense.android.R
import com.attrsense.android.baselibrary.base.open.SkeletonDataBindingVMBaseActivity
import com.attrsense.android.databinding.ActivitySplashBinding
import com.attrsense.android.ui.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import java.util.concurrent.atomic.AtomicBoolean

@AndroidEntryPoint
class SplashActivity : SkeletonDataBindingVMBaseActivity<ActivitySplashBinding, SplashViewModel>() {

    private var keepScreen = AtomicBoolean(true)
    private lateinit var splashScreen: SplashScreen

    override fun setLayoutResId(): Int = R.layout.activity_splash

    override fun setViewModel(): Class<SplashViewModel> = SplashViewModel::class.java

    override fun initViewBefore() {
        splashScreen = installSplashScreen()
    }

    override fun initView() {
        mDataBinding.acTv.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
//            mViewModel.load("js")
        }
        mViewModel.github().observe(this) {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }

    fun launch() {
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

//    override fun onBackPressed() {
//        super.onBackPressed()
//        val intent = Intent(Intent.ACTION_MAIN).apply {
//            addCategory(Intent.CATEGORY_HOME)
//        }
//        Log.i("printInfo", "SplashActivity::onBackPressed: ")
//        startActivity(intent)
//    }
}
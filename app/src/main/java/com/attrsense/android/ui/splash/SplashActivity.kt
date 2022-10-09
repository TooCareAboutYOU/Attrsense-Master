package com.attrsense.android.ui.splash

import android.util.Log
import androidx.core.splashscreen.SplashScreen
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.MutableCreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.attrsense.android.R
import com.attrsense.android.baselibrary.base.open.SkeletonDataBindingBaseActivity
import com.attrsense.android.databinding.ActivitySplashBinding
import com.attrsense.android.http.ApiService
import com.attrsense.android.ui.main.MainViewModel
import com.blankj.utilcode.util.ToastUtils
import com.orhanobut.logger.Logger
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import java.util.concurrent.atomic.AtomicBoolean
import javax.inject.Inject

@AndroidEntryPoint
class SplashActivity : SkeletonDataBindingBaseActivity<ActivitySplashBinding>() {

    private var keepScreen = AtomicBoolean(true)
    private lateinit var splashScreen: SplashScreen

    @Inject
    lateinit var apiService: ApiService
    private val mainViewModel: MainViewModel by lazy {
        viewModelFactory { initializer { MainViewModel(apiService) } }.create(
            MainViewModel::class.java,
            MutableCreationExtras()
        )
    }

    override fun setLayoutResId(): Int = R.layout.activity_splash

    override fun initViewBefore() {
        splashScreen = installSplashScreen()
    }

    override fun initView() {
        mDataBinding.acTv.setOnClickListener {
            ToastUtils.showShort("弹出成功!")
            mainViewModel.login("18874703157", "111111")
        }
        mainViewModel.loginLiveData.observe(this) {
            it?.apply {
//                Logger.i("$this")
            }
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
}
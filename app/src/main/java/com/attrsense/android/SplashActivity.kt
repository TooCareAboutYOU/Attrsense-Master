package com.attrsense.android

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {
//    private var keepScreen = AtomicBoolean(true)
//    private lateinit var splashScreen: SplashScreen

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        splashScreen=installSplashScreen()
        setContentView(R.layout.activity_splash)

//        countDowntime()
//
//        //展示完毕的监听
//        splashScreen.setOnExitAnimationListener {provider->
//            //移除监听
//            provider.remove()
//            //跳转到下个页面
//            startActivity(Intent(this, MainActivity::class.java))
//            finish()
//            overridePendingTransition(0,0)
//        }

    }

    //设置欢迎页展示时间
//    fun countDowntime(){
//        lifecycleScope.launchWhenResumed {
//            delay(1000)
//            keepScreen.compareAndSet(true,false)
//        }
//        //绑定数据
//        splashScreen.setKeepVisibleCondition{keepScreen.get()}
//    }
}
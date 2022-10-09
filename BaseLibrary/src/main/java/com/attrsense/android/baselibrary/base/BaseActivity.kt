package com.attrsense.android.baselibrary.base

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity

/**
 * author : zhangshuai@attrsense.com
 * date : 2022/10/8
 * mark : custom something
 */
open class BaseActivity : RxAppCompatActivity() {

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onRestart() {
        super.onRestart()
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

}
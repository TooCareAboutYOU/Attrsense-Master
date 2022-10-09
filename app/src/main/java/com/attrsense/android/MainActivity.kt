package com.attrsense.android

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.attrsense.android.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    external fun stringFromJNI(): String

    companion object {
        // Used to load the 'android' library on application startup.
        init {
            System.loadLibrary("android")
        }
    }
}
package com.practice.android.pocketmate

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.practice.android.pocketmate.Auth.IntroActivity
import com.practice.android.pocketmate.databinding.ActivitySplashBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay

class SplashActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivitySplashBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        GlobalScope.launch {
            delay(3000)
            startActivity(Intent(this@SplashActivity, IntroActivity::class.java))
            finish()
        }
    }
}
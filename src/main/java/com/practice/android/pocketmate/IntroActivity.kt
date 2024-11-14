package com.practice.android.pocketmate

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.practice.android.pocketmate.databinding.ActivityIntroBinding

class IntroActivity : AppCompatActivity() {

    lateinit var binding : ActivityIntroBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIntroBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}
package com.practice.android.pocketmate.Tip

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.practice.android.pocketmate.databinding.ActivityWriteTipBinding

class WriteTipActivity : AppCompatActivity() {

    lateinit var binding : ActivityWriteTipBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWriteTipBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}
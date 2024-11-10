package com.practice.android.pocketmate.Tip

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.practice.android.pocketmate.R
import com.practice.android.pocketmate.databinding.ActivityTipBinding

class TipActivity : AppCompatActivity() {

    lateinit var binding : ActivityTipBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTipBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }
}
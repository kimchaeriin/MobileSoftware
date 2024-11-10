package com.practice.android.pocketmate.Pocket

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.practice.android.pocketmate.databinding.ActivityWritePocketBoardBinding

class WritePocketBoardActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val binding = ActivityWritePocketBoardBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbarWritePocketBoard)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
}

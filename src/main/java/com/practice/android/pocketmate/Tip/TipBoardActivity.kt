package com.practice.android.pocketmate.Tip

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.practice.android.pocketmate.databinding.ActivityTipBoardBinding

class TipBoardActivity : AppCompatActivity() {

    lateinit var binding : ActivityTipBoardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTipBoardBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        setSupportActionBar(binding.toolbar)

        binding.writeTipBtn.setOnClickListener {
            switchScreen(this, WriteTipActivity::class.java)
        }
    }

    fun switchScreen(from: AppCompatActivity, to: Class<out AppCompatActivity>) {
        val intent = Intent(from, to)
        from.startActivity(intent)
        from.finish()
    }
}
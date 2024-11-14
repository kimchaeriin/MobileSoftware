package com.practice.android.pocketmate.Auth

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.practice.android.pocketmate.MainActivity
import com.practice.android.pocketmate.R
import com.practice.android.pocketmate.databinding.ActivitySettingBinding
import com.practice.android.pocketmate.util.FBAuth
import com.practice.android.pocketmate.util.FBRef

class SettingActivity : AppCompatActivity() {
    lateinit var binding : ActivitySettingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.setNicknameBtn.setOnClickListener {
            setNickname()
            switchScreen(this, MainActivity::class.java)
            finish()
        }
    }

    private fun setNickname() {
        val nickname = binding.setNicknameArea.text.toString()
        if (nickname.length < 1) {
            Toast.makeText(this, "닉네임은 한 글자 이상이어야 합니다.", Toast.LENGTH_LONG).show()
        }
        else {
        FBRef.nicknameRef.child(FBAuth.getUid()).setValue(binding.setNicknameArea.text.toString())
        }
    }

    private fun switchScreen(from: AppCompatActivity, to: Class<out AppCompatActivity>) {
        val intent = Intent(from, to)
        from.startActivity(intent)
    }
}
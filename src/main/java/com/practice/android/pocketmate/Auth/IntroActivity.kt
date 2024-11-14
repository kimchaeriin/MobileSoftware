package com.practice.android.pocketmate.Auth

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.practice.android.pocketmate.MainActivity
import com.practice.android.pocketmate.databinding.ActivityIntroBinding

class IntroActivity : AppCompatActivity() {

    lateinit var binding : ActivityIntroBinding
    lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIntroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth
        val currentUser = auth.currentUser

        if (currentUser != null) {
            switchScreen(this, MainActivity::class.java)
            finish()
        }

        binding.kakaoLogin.setOnClickListener {
            switchScreen(this, AuthCodeHandlerActivity::class.java)
        }

        binding.emailLogin.setOnClickListener {
            switchScreen(this, LoginActivity::class.java)
        }
    }

    private fun switchScreen(from: AppCompatActivity, to: Class<out AppCompatActivity>) {
        val intent = Intent(from, to)
        from.startActivity(intent)
    }
}
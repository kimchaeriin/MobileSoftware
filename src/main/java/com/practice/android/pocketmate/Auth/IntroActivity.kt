package com.practice.android.pocketmate.Auth

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.practice.android.pocketmate.MainActivity
import com.practice.android.pocketmate.databinding.ActivityIntroBinding
import com.practice.android.pocketmate.util.AppUtils

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
            AppUtils.switchScreen(this, MainActivity::class.java)
            finish()
        }

        handleBtns()
    }

    private fun handleBtns() {
        binding.kakaoLogin.setOnClickListener {
            AppUtils.switchScreen(this, AuthCodeHandlerActivity::class.java)
        }

        binding.emailLogin.setOnClickListener {
            AppUtils.switchScreen(this, LoginActivity::class.java)
        }
    }

}
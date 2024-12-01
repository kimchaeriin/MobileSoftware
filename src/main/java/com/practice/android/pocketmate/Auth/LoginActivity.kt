package com.practice.android.pocketmate.Auth

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.practice.android.pocketmate.MainActivity
import com.practice.android.pocketmate.databinding.ActivityLoginBinding
import com.practice.android.pocketmate.util.ScreenUtils
import kotlinx.coroutines.Dispatchers.Main

class LoginActivity : AppCompatActivity() {

    lateinit var binding : ActivityLoginBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbarLogin)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        

        var moveToActivity : Class<out AppCompatActivity> = MainActivity::class.java
        val isJoin = intent.getStringExtra("join")
        if (isJoin.equals("join")) {
            moveToActivity = SettingActivity::class.java
        }

        auth = Firebase.auth
        if (auth.currentUser != null) {
            ScreenUtils.switchScreen(this, moveToActivity)
            finish()
        }

        handleBtns()
    }

    private fun handleBtns() {
        binding.joinBtn.setOnClickListener {
            ScreenUtils.switchScreen(this, JoinActivity::class.java)
        }
        binding.loginBtn.setOnClickListener {
            login()
        }
        binding.findBtn.setOnClickListener{
            ScreenUtils.switchScreen(this,FindPwActivity::class.java)
        }
    }

    private fun login() {
        val email = binding.emailArea.text.toString().trim()
        val password = binding.passwordArea.text.toString().trim()

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    ScreenUtils.switchScreen(this, MainActivity::class.java)
                } else {
                    Toast.makeText(
                        baseContext,
                        "Authentication failed.",
                        Toast.LENGTH_SHORT,
                    ).show()
                }
            }
    }
}

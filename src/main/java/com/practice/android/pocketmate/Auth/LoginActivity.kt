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
import kotlinx.coroutines.Dispatchers.Main

class LoginActivity : AppCompatActivity() {

    lateinit var binding : ActivityLoginBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        auth = Firebase.auth

        var moveToActivity : Class<out AppCompatActivity> = MainActivity::class.java
        val isJoin = intent.getStringExtra("join")
        if (isJoin.equals("join")) {
            moveToActivity = SettingActivity::class.java
        }

        if (auth.currentUser != null) {
            switchScreen(this, moveToActivity)
            finish()
        }

        binding.joinBtn.setOnClickListener {
            switchScreen(this, JoinActivity::class.java)
        }
        binding.loginBtn.setOnClickListener {
            login()
        }
    }

    private fun login() {
        val email = binding.emailArea.text.toString().trim()
        val password = binding.passwordArea.text.toString().trim()

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {

                } else {
                    Toast.makeText(
                        baseContext,
                        "Authentication failed.",
                        Toast.LENGTH_SHORT,
                    ).show()
                }
            }
    }

    private fun switchScreen(from: AppCompatActivity, to: Class<out AppCompatActivity>) {
        val intent = Intent(from, to)
        from.startActivity(intent)
    }
}
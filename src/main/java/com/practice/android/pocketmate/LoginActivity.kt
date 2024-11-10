package com.practice.android.pocketmate

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.kakao.sdk.user.UserApiClient
import com.practice.android.pocketmate.databinding.ActivityLoginBinding
import com.practice.android.pocketmate.util.FBAuth.Companion.auth

class LoginActivity : AppCompatActivity() {

    lateinit var binding : ActivityLoginBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        auth = Firebase.auth

        binding.joinBtn.setOnClickListener {
            switchScreen(this, JoinActivity::class.java)
        }

        binding.loginBtn.setOnClickListener {
            login()
            if (auth.currentUser != null) {
                switchScreen(this, MainActivity::class.java)
            }
        }
    }

    fun login() {
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

    fun switchScreen(from: AppCompatActivity, to: Class<out AppCompatActivity>) {
        val intent = Intent(from, to)
        from.startActivity(intent)
        from.finish()
    }
}
package com.practice.android.pocketmate.Auth

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.practice.android.pocketmate.databinding.ActivityJoinBinding

class JoinActivity : AppCompatActivity() {

    lateinit var binding : ActivityJoinBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJoinBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth

        binding.joinBtn.setOnClickListener {
            val email = binding.emailArea.text.toString()
            val password = binding.passwordArea.text.toString()
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val intent = Intent(this, LoginActivity::class.java)
                        intent.putExtra("join", "join")
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this, "회원가입에 실패하였습니다.", Toast.LENGTH_LONG).show()
                    }
                }
        }

    }
}
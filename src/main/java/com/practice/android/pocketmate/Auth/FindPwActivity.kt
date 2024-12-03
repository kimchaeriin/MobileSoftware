package com.practice.android.pocketmate.Auth

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.practice.android.pocketmate.R
import com.practice.android.pocketmate.databinding.ActivityFindPwBinding

class FindPwActivity : AppCompatActivity() {
    lateinit var binding : ActivityFindPwBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFindPwBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbarFindPw)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.findBtn.setOnClickListener {
            val email = binding.emailInput.text.toString()

            Firebase.auth.sendPasswordResetEmail(email)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this,"비밀번호 재설정 이메일을 전송하였습니다",Toast.LENGTH_SHORT).show()
                    }
                }

        }
    }
}
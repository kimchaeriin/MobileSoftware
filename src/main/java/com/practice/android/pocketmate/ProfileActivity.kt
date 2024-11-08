package com.practice.android.pocketmate

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.practice.android.pocketmate.databinding.ActivityProfileBinding

class ProfileActivity : AppCompatActivity() {

    lateinit var binding : ActivityProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        binding.profileImageBtn.setOnClickListener {

        }

        binding.duplicateCheckBtn.setOnClickListener {

        }

        binding.friendListBtn.setOnClickListener {
            val intent = Intent(this, SearchIDActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.logoutBtn.setOnClickListener {
            Firebase.auth.signOut()

            //카카오 로그아웃 기능 필요

        }

        binding.withDrawBtn.setOnClickListener {
            val user = Firebase.auth.currentUser!!

            user.delete()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val intent = Intent(this, IntroActivity::class.java)
                        startActivity(intent)
                        finish()

                        //회원 탈퇴 팝업 or IntroActivity에 Toast 메세지

                    }
                }
        }

    }
}
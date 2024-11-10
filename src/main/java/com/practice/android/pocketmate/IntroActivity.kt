package com.practice.android.pocketmate

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import com.practice.android.pocketmate.Tip.WriteTipActivity
import com.practice.android.pocketmate.databinding.ActivityIntroBinding
import com.practice.android.pocketmate.util.FBAuth.Companion.auth
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

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
        }

        binding.kakaoLogin.setOnClickListener {
            switchScreen(this, AuthCodeHandlerActivity::class.java)
        }

        binding.emailLogin.setOnClickListener {
            switchScreen(this, LoginActivity::class.java)
        }

    }

    fun switchScreen(from: AppCompatActivity, to: Class<out AppCompatActivity>) {
        val intent = Intent(from, to)
        from.startActivity(intent)
    }
}
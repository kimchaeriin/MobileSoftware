package com.practice.android.pocketmate

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.kakao.sdk.user.UserApiClient
import com.practice.android.pocketmate.databinding.ActivityIntroBinding

class IntroActivity : AppCompatActivity() {

    lateinit var binding : ActivityIntroBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIntroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        UserApiClient.instance.loginWithKakaoTalk(this) { token, error ->
            if (error != null) {
                Log.e("KakaoLogin", "로그인 실패", error)
            }
            else if (token != null) {
                Log.i("KakaoLogin", "로그인 성공 ${token.accessToken}")
            }
        }
    }
}
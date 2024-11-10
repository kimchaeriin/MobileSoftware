package com.practice.android.pocketmate

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.kakao.sdk.user.UserApiClient
import com.practice.android.pocketmate.databinding.ActivityProfileBinding
import com.practice.android.pocketmate.util.FBAuth

class ProfileActivity : AppCompatActivity() {

    lateinit var binding : ActivityProfileBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        binding.uid.text = FBAuth.getUid()

        binding.profileImageBtn.setOnClickListener {

        }

        binding.copyBtn.setOnClickListener {
            val clip: ClipData = ClipData.newPlainText("회원 아이디", binding.uid.text.toString())
            clipboard.setPrimaryClip(clip)
        }

        binding.friendListBtn.setOnClickListener {
            val intent = Intent(this, SearchIDActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.logoutBtn.setOnClickListener {
            //파이어베이스와 카카오톡 계정 로그아웃 방식 분리
            Firebase.auth.signOut()

            UserApiClient.instance.logout { error ->
                if (error != null) {
                    Log.e("kakaoLogout", "로그아웃 실패. SDK에서 토큰 삭제됨", error)
                }
                else {
                    Log.i("kakaoLogout", "로그아웃 성공. SDK에서 토큰 삭제됨")
                }
            }

        }

        binding.withDrawBtn.setOnClickListener {
            //파이어베이스와 카카오톡 계정 회원 탈퇴 방식 분리

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

            UserApiClient.instance.unlink { error ->
                if (error != null) {
                    Log.e("kakaoSignOut", "연결 끊기 실패", error)
                }
                else {
                    Log.i("kakaoSignOut", "연결 끊기 성공. SDK에서 토큰 삭제 됨")
                    val intent = Intent(this, IntroActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }

    }
}
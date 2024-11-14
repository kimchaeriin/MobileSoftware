package com.practice.android.pocketmate

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.play.integrity.internal.k
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.kakao.sdk.user.UserApiClient
import com.practice.android.pocketmate.Auth.IntroActivity
import com.practice.android.pocketmate.databinding.ActivityProfileBinding
import com.practice.android.pocketmate.util.FBAuth
import com.practice.android.pocketmate.util.FBRef

class ProfileActivity : AppCompatActivity() {

    lateinit var binding : ActivityProfileBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val uid = FBAuth.getUid()

        binding.uid.text = uid

        getNickname { nickname ->
            binding.nickname.setText(nickname)
        }

        binding.profileImageBtn.setOnClickListener {
            //프로필 이미지 바꾸기
        }

        binding.changeBtn.setOnClickListener {
            val newNickname = binding.nickname.text.toString()
            FBRef.nicknameRef.child(uid).setValue(newNickname)
            Toast.makeText(this, "닉네임이 변경되었습니다.", Toast.LENGTH_LONG).show()
        }

        binding.copyBtn.setOnClickListener {
            copyUid()
        }

        binding.friendListBtn.setOnClickListener {
            switchScreen(this, SearchIDActivity::class.java)
        }

        binding.logoutBtn.setOnClickListener {
            Firebase.auth.signOut()
            logoutWithKakao()
            if (Firebase.auth.currentUser == null) {
                switchScreen(this, IntroActivity::class.java)
            }
        }

        binding.withDrawBtn.setOnClickListener {
            if (signOutWithFirebase() || signOutWithKakao()) {
                switchScreen(this, IntroActivity::class.java)
            }
        }
    }

    private fun getNickname(callback: (String) -> Unit) {
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val nickname = dataSnapshot.getValue(String::class.java) ?: ""
                callback(nickname)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                callback("") // 에러가 발생한 경우 빈 문자열을 콜백으로 전달
            }
        }
        FBRef.nicknameRef.child(FBAuth.getUid()).addListenerForSingleValueEvent(postListener)
    }


    private fun logoutWithKakao() { //안될 것 같다
        UserApiClient.instance.logout { error ->
            if (error != null) {
                Log.e("kakaoLogout", "로그아웃 실패. SDK에서 토큰 삭제됨", error)
            }
            else {
                Log.i("kakaoLogout", "로그아웃 성공. SDK에서 토큰 삭제됨")
            }
        }
    }

    private fun signOutWithKakao() : Boolean {
        var success = false

        UserApiClient.instance.unlink { error ->
            if (error != null) {
                Log.e("kakaoSignOut", "연결 끊기 실패", error)
            }
            else {
                Log.i("kakaoSignOut", "연결 끊기 성공. SDK에서 토큰 삭제 됨")
                success = true
            }
        }

        return success
    }

    private fun signOutWithFirebase() : Boolean {
        var success = false
        val user = Firebase.auth.currentUser!!

        user.delete()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    //회원 탈퇴 팝업 or IntroActivity에 Toast 메세지
                    success = true
                }
            }

        return success
    }

    private fun copyUid() {
        val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip: ClipData = ClipData.newPlainText("회원 아이디", binding.uid.text.toString())
        clipboard.setPrimaryClip(clip)
    }

    private fun switchScreen(from: AppCompatActivity, to: Class<out AppCompatActivity>) {
        val intent = Intent(from, to)
        startActivity(intent)
        finish()
    }
}
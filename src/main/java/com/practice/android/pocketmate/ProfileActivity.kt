package com.practice.android.pocketmate

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Contacts.Settings.setSetting
import android.text.Editable
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.content.ContextCompat.startActivity
import com.google.android.play.integrity.internal.k
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.kakao.sdk.user.UserApiClient
import com.practice.android.pocketmate.Auth.IntroActivity
import com.practice.android.pocketmate.databinding.ActivityProfileBinding
import com.practice.android.pocketmate.util.ScreenUtils
import com.practice.android.pocketmate.util.FBAuth
import com.practice.android.pocketmate.util.FBRef

class ProfileActivity : AppCompatActivity() {

    lateinit var binding : ActivityProfileBinding
    var imm : InputMethodManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        binding.navigation.selectedItemId = R.id.nav_profile
        ScreenUtils.setBottomNavigationBar(this, binding.navigation)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?

        getProfile()
        handleBtns()
    }

    private fun getProfile() {
        val uid = FBAuth.getUid()
        binding.uid.text = uid
        getNickname { nickname ->
            binding.nickname.setText(nickname)
        }
    }

    private fun handleBtns() {
        binding.profileImageBtn.setOnClickListener {
            //프로필 이미지 바꾸기
        }

        binding.changeBtn.setOnClickListener {
            changeNickname()
            hideKeyBoard()
        }

        binding.copyBtn.setOnClickListener {
            copyUid()
        }

        binding.friendListBtn.setOnClickListener {
            ScreenUtils.switchScreen(this, SearchIDActivity::class.java)
        }

        binding.logoutBtn.setOnClickListener {
            Firebase.auth.signOut()
            logoutWithKakao()
            if (Firebase.auth.currentUser == null) {
                ScreenUtils.switchScreen(this, IntroActivity::class.java)
            }
        }

        binding.withDrawBtn.setOnClickListener {
            if (signOutWithFirebase() || signOutWithKakao()) {
                ScreenUtils.switchScreen(this, IntroActivity::class.java)
            }
        }
    }

    private fun changeNickname() {
        val uid = FBAuth.getUid()
        val newNickname = binding.nickname.text.toString().trim()
        if (newNickname.length < 1) {
            Toast.makeText(this, "닉네임은 한 글자 이상이어야 합니다.", Toast.LENGTH_LONG).show()
        } else {
            val childUpdates = hashMapOf<String, Any>("/Nicknames/$uid" to newNickname)
            val databaseRef = Firebase.database.reference
            databaseRef.updateChildren(childUpdates).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    FBRef.nicknameRef.child(uid).setValue(newNickname)
                    Toast.makeText(this, "닉네임이 변경되었습니다.", Toast.LENGTH_LONG).show()
                } else {
                    Log.e("TipActivity", "Failed to update agree count.")
                }
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

    fun hideKeyBoard() {
        val view = currentFocus
        if (view != null) {
            imm?.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
}
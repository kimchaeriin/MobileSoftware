package com.practice.android.pocketmate.Tip

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.practice.android.pocketmate.Model.BoardModel
import com.practice.android.pocketmate.databinding.ActivityWriteTipBinding
import com.practice.android.pocketmate.util.FBAuth
import com.practice.android.pocketmate.util.FBRef

class WriteTipActivity : AppCompatActivity() {

    lateinit var binding : ActivityWriteTipBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWriteTipBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.postBtn.setOnClickListener {
            postAndSwitchScreen()
        }
    }

    private fun postAndSwitchScreen() {
        val user = FBAuth.getUid()
        val title = binding.title.text.toString()
        val content = binding.content.text.toString()
        val image = 0 //null일 때와 아닐 때 분리 필요
        val agree = 0
        val disagree = 0

        if (title.isEmpty() || content.isEmpty()) {
            Toast.makeText(this, "제목과 내용은 한 글자 이상 작성해야 합니다.", Toast.LENGTH_SHORT).show()
        }
        else {
            getNickname(user) { nickname ->
                val key = FBRef.tipRef.push().key.toString()
                val tip = BoardModel(user, nickname, title, content, image, agree, disagree)
                FBRef.tipRef.child(key).setValue(tip)
            }
            switchScreen(this, TipBoardActivity::class.java)
        }
    }

    private fun getNickname(user: String, callback: (String) -> Unit) {
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
        FBRef.nicknameRef.child(user).addListenerForSingleValueEvent(postListener)
    }

    private fun switchScreen(from: AppCompatActivity, to: Class<out AppCompatActivity>) {
        val intent = Intent(from, to)
        from.startActivity(intent)
    }
}
package com.practice.android.pocketmate.Tip

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
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

        if (title.isEmpty() || content.isEmpty()) {
            Toast.makeText(this, "제목과 내용은 한 글자 이상 작성해야 합니다.", Toast.LENGTH_SHORT).show()
        }
        else {
            val tip = BoardModel(user, title, content, image)
            val key = FBRef.tipRef.push().key.toString()
            FBRef.tipRef.child(key).setValue(tip)
            switchScreen(this, TipBoardActivity::class.java)
        }
    }

    private fun switchScreen(from: AppCompatActivity, to: Class<out AppCompatActivity>) {
        val intent = Intent(from, to)
        from.startActivity(intent)
    }
}
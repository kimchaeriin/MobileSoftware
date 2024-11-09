package com.practice.android.pocketmate.Tip

import android.content.Intent
import android.os.Bundle
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

        binding.postBtn.setOnClickListener {
            val user = FBAuth.getUid()
            val title = binding.title.text.toString()
            val content = binding.content.text.toString()
            val image = 0 //null일 때와 아닐 때 분리 필요
            val board = BoardModel(user, title, content, image)
            FBRef.tipRef.setValue(board)

            val intent = Intent(this, TipBoardActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
package com.practice.android.pocketmate.Tip

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.practice.android.pocketmate.Model.BoardModel
import com.practice.android.pocketmate.R
import com.practice.android.pocketmate.databinding.ActivityEditTipBinding
import com.practice.android.pocketmate.util.FBAuth
import com.practice.android.pocketmate.util.FBRef

class EditTipActivity : AppCompatActivity() {

    lateinit var binding : ActivityEditTipBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditTipBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val key = intent.getStringExtra("key").toString()

        getTipData(key)

        binding.editBtn.text = "수정하기"
        binding.editBtn.setOnClickListener {
            editAndSwitchScreen(key)
        }
    }

    private fun getTipData(key : String) {
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val tip = dataSnapshot.getValue(BoardModel::class.java)!!
                binding.title.setText(tip.title)
                binding.content.setText(tip.content)
                binding.image.setImageResource(tip.image)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
            }
        }
        FBRef.tipRef.child(key).addValueEventListener(postListener)
    }

    private fun editAndSwitchScreen(key: String) {
        val title = binding.title.text.toString()
        val content = binding.content.text.toString()
        val image = 0 //null일 때와 아닐 때 분리 필요
        val user = FBAuth.getUid()

        if (title.isEmpty() || content.isEmpty()) {
            Toast.makeText(this, "제목과 내용은 한 글자 이상 작성해야 합니다.", Toast.LENGTH_SHORT).show()
        }
        else {
            val tip = BoardModel(user, title, content, image)
            FBRef.tipRef.child(key).setValue(tip)
            switchScreen(this, TipBoardActivity::class.java)
        }
    }

    private fun switchScreen(from: AppCompatActivity, to: Class<out AppCompatActivity>) {
        val intent = Intent(from, to)
        from.startActivity(intent)
    }
}
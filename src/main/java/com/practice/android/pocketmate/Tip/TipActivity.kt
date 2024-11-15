package com.practice.android.pocketmate.Tip

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.practice.android.pocketmate.Adapter.CommentAdapter
import com.practice.android.pocketmate.Model.BoardModel
import com.practice.android.pocketmate.Model.CommentModel
import com.practice.android.pocketmate.databinding.ActivityTipBinding
import com.practice.android.pocketmate.util.FBAuth
import com.practice.android.pocketmate.util.FBRef

class TipActivity : AppCompatActivity() {

    lateinit var binding : ActivityTipBinding
    val commentList = mutableListOf<CommentModel>()
    var agreed = false
    var disagreed = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTipBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val key = intent.getStringExtra("key").toString()

        getTipData(key)
        getCommentData(key)

        binding.commentArea.adapter = CommentAdapter(commentList)
        binding.commentArea.layoutManager = LinearLayoutManager(this)

        handleBtns(key)
    }

    private fun handleBtns(key: String) {
        binding.writeCommentBtn.setOnClickListener {
            writeComment(key)
            binding.writeCommentArea.text.clear()
        }
        binding.editOrDeleteBtn.setOnClickListener {
            editOrDeleteDialog(key)
        }
        binding.agreeBtn.setOnClickListener {
            if (agreed) {
                reduceAgree(key)
            }
            else {
                raiseAgree(key)
            }
            agreed = !agreed
        }
        binding.disagreeBtn.setOnClickListener {
            if (disagreed) {
                reduceDisagree(key)
            }
            else{
                raiseDisagree(key)
            }
            disagreed = !disagreed
        }
    }

    private fun raiseAgree(key: String) {
        val agree = binding.agreeNumber.text.toString().toInt() + 1
        val childUpdates = hashMapOf<String, Any>(
                "/TipBoard/$key/agree" to agree,
            )
        val databaseRef = Firebase.database.reference
        databaseRef.updateChildren(childUpdates).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                binding.agreeNumber.text = agree.toString()
            } else {
                Log.e("TipActivity", "Failed to update agree count.")
            }
        }
    }

    private fun reduceAgree(key: String) {
        val agree = binding.agreeNumber.text.toString().toInt() - 1
        val childUpdates = hashMapOf<String, Any>(
            "/TipBoard/$key/agree" to agree,
        )
        val databaseRef = Firebase.database.reference
        databaseRef.updateChildren(childUpdates).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                binding.agreeNumber.text = agree.toString()
            } else {
                Log.e("TipActivity", "Failed to update agree count.")
            }
        }
    }

    private fun raiseDisagree(key: String) {
        val disagree = binding.disagreeNumber.text.toString().toInt() + 1
        val childUpdates = hashMapOf<String, Any>(
            "/TipBoard/$key/disagree" to disagree,
        )
        val databaseRef = Firebase.database.reference
        databaseRef.updateChildren(childUpdates).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                binding.disagreeNumber.text = disagree.toString()
            } else {
                Log.e("TipActivity", "Failed to update agree count.")
            }
        }
    }

    private fun reduceDisagree(key: String) {
        val disagree = binding.disagreeNumber.text.toString().toInt() - 1
        val childUpdates = hashMapOf<String, Any>(
            "/TipBoard/$key/disagree" to disagree,
        )
        val databaseRef = Firebase.database.reference
        databaseRef.updateChildren(childUpdates).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                binding.disagreeNumber.text = disagree.toString()
            } else {
                Log.e("TipActivity", "Failed to update agree count.")
            }
        }
    }

    private fun getTipData(key : String) {
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val tip = dataSnapshot.getValue(BoardModel::class.java)!!
                binding.title.text = tip.title
                binding.content.text = tip.content
                binding.writer.text = tip.nickname
                binding.agreeNumber.text = tip.agree.toString()
                binding.disagreeNumber.text = tip.disagree.toString()

                if (tip.image == 0) {
                    binding.image.visibility = View.GONE
                }

                getNickname { nickname ->
                    if (tip.uid == FBAuth.getUid()) {
                        binding.editOrDeleteBtn.visibility = View.VISIBLE
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
            }
        }
        FBRef.tipRef.child(key).addValueEventListener(postListener)
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

    private fun getCommentData(key: String) {
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                commentList.clear()

                for (data in dataSnapshot.children) {
                    val comment = data.getValue(CommentModel::class.java)!!
                    binding.emptyCommentText.visibility = View.GONE
                    commentList.add(comment!!)
                }
                binding.commentArea.adapter?.notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
            }
        }
        FBRef.commentRef.child(key).addValueEventListener(postListener)
    }

    private fun writeComment(key: String) {
        val commentKey = FBRef.commentRef.child(key).push().key.toString()
        val commentContent = binding.writeCommentArea.text.toString()

        getNickname { nickname ->
            val comment = CommentModel(0, nickname, commentContent)
            FBRef.commentRef.child(key).child(commentKey).setValue(comment)
        }
    }

    private fun editOrDeleteDialog(key: String) {
        MaterialAlertDialogBuilder(this)
            .setMessage("해당 게시글을 수정하거나 삭제하시겠습니까?")
            .setNeutralButton("취소") { dialog, which ->
                // Respond to neutral button press
            }
            .setNegativeButton("수정") { dialog, which -> //수정 필요
                val intent = Intent(this, EditTipActivity::class.java)
                intent.putExtra("key", key)
                startActivity(intent)
            }
            .setPositiveButton("삭제") { dialog, which ->
                FBRef.tipRef.child(key).removeValue()
                switchScreen(this, TipBoardActivity::class.java)
            }
            .show()
    }

    private fun switchScreen(from: AppCompatActivity, to: Class<out AppCompatActivity>) {
        val intent = Intent(from, to)
        from.startActivity(intent)
    }

}
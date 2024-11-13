package com.practice.android.pocketmate.Tip

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.practice.android.pocketmate.Adapter.CommentAdapter
import com.practice.android.pocketmate.Model.BoardModel
import com.practice.android.pocketmate.Model.CommentModel
import com.practice.android.pocketmate.databinding.ActivityTipBinding
import com.practice.android.pocketmate.util.FBAuth
import com.practice.android.pocketmate.util.FBRef

class TipActivity : AppCompatActivity() {

    lateinit var binding : ActivityTipBinding
    val commentList = mutableListOf<CommentModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTipBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val key = intent.getStringExtra("key").toString()

        getTipData(key)
        getCommentData(key)

        binding.commentArea.adapter = CommentAdapter(commentList)
        binding.commentArea.layoutManager = LinearLayoutManager(this)

        binding.writeCommentBtn.setOnClickListener {
            writeComment(key)
            binding.writeCommentArea.text.clear()
        }
        binding.editOrDeleteBtn.setOnClickListener {
            editOrDeleteDialog(key)
        }
    }

    private fun getTipData(key : String) {
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val tip = dataSnapshot.getValue(BoardModel::class.java)!!
                binding.title.text = tip.title
                binding.content.text = tip.content
                binding.writer.text = tip.writer
                if (tip.image == 0) {
                    binding.image.visibility = View.GONE
                }
                if (tip.writer == FBAuth.getUid()) {
                    binding.editOrDeleteBtn.visibility = View.VISIBLE
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
            }
        }
        FBRef.tipRef.child(key).addValueEventListener(postListener)
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
        val comment = CommentModel(0, FBAuth.getUid(), commentContent)
        FBRef.commentRef.child(key).child(commentKey).setValue(comment)
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
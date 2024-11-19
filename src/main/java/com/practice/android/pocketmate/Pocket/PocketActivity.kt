package com.practice.android.pocketmate.Pocket

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
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
import com.practice.android.pocketmate.R
import com.practice.android.pocketmate.databinding.ActivityPocketBinding
import com.practice.android.pocketmate.databinding.ItemCommentBinding
import com.practice.android.pocketmate.util.FBAuth
import com.practice.android.pocketmate.util.FBAuth.Companion.getNickname
import com.practice.android.pocketmate.util.FBRef
import com.practice.android.pocketmate.util.ScreenUtils


class PocketActivity : AppCompatActivity() {
    lateinit var binding : ActivityPocketBinding
    lateinit var commentBinding: ItemCommentBinding
    val commentList = mutableListOf<CommentModel>()
    var agreed = false
    var disagreed = false
    var bookmarked = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPocketBinding.inflate(layoutInflater)
        commentBinding = ItemCommentBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val key = intent.getStringExtra("key").toString()

        getPocketData(key)
        getCommentData(key)
        handleBtns(key)

        binding.commentArea.adapter = CommentAdapter(commentList)
        binding.commentArea.layoutManager = LinearLayoutManager(this)
    }

    private fun getPocketData(key : String) {
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val pocket = dataSnapshot.getValue(BoardModel::class.java)!!
                binding.title.text = pocket.title
                binding.date.text = pocket.date
                binding.content.text = pocket.content
                binding.content.setTextColor(pocket.color)
                getNickname(FBAuth.getUid()) { nickname -> binding.nickname.text = nickname }
                binding.agreeNumber.text = pocket.agree.toString()
                binding.disagreeNumber.text = pocket.disagree.toString()

                if (pocket.image == 0) {
                    binding.image.visibility = View.GONE
                }
                if (pocket.uid != FBAuth.getUid()) {
                    binding.editBtn.visibility = View.GONE
                    binding.deleteBtn.visibility = View.GONE
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
            }
        }
        FBRef.pocketRef.child(key).addValueEventListener(postListener)
    }

    private fun handleBtns(key: String) {
        binding.writeCommentBtn.setOnClickListener {
            writeComment(key)
            binding.writeCommentArea.text.clear()
        }

        binding.editBtn.setOnClickListener {
            showEditDialog(key)
        }

        binding.deleteBtn.setOnClickListener {
            showDeleteDialog(key)
        }

        binding.agreeBtn.setOnClickListener {
            if (disagreed) {
                Toast.makeText(this, "추천과 비추천을 동시에 할 수 없습니다.", Toast.LENGTH_SHORT).show()
            }
            else {
                if (agreed) {
                    reduceAgree(key)
                    binding.agreeBtn.setImageResource(R.drawable.baseline_thumb_up_border_24)
                } else {
                    raiseAgree(key)
                    binding.agreeBtn.setImageResource(R.drawable.baseline_thumb_up_24)
                }
                agreed = !agreed
            }
        }

        binding.disagreeBtn.setOnClickListener {
            if (agreed) {
                Toast.makeText(this, "추천과 비추천을 동시에 할 수 없습니다.", Toast.LENGTH_SHORT).show()
            }
            else {
                if (disagreed) {
                    reduceDisagree(key)
                    binding.agreeBtn.setImageResource(R.drawable.baseline_thumb_down_border_24)
                } else {
                    raiseDisagree(key)
                    binding.agreeBtn.setImageResource(R.drawable.baseline_thumb_down_24)
                }
                disagreed = !disagreed
            }
        }
    }

    private fun showDeleteCommentDialog(key: String, commentKey: String) {

    }

    private fun showEditDialog(key: String) {
        MaterialAlertDialogBuilder(this)
            .setMessage("수정하시겠습니까?")
            .setNegativeButton("취소") { dialog, which ->
                //do nothing.
            }
            .setPositiveButton("수정") { dialog, which ->
                val intent = Intent(this, EditPocketActivity::class.java)
                intent.putExtra("key", key)
                startActivity(intent)
                finish()
            }
            .show()
    }

    private fun showDeleteDialog(key: String) {
        MaterialAlertDialogBuilder(this)
            .setMessage("삭제하시겠습니까?")
            .setNegativeButton("취소") { dialog, which ->
                //do nothing.
            }
            .setPositiveButton("삭제") { dialog, which ->
                ScreenUtils.switchScreen(this, PocketBoardActivity::class.java)
                FBRef.pocketRef.child(key).removeValue()
                finish()
            }
            .show()
    }

    private fun raiseAgree(key: String) {
        val agree = binding.agreeNumber.text.toString().toInt() + 1
        val childUpdates = hashMapOf<String, Any>(
            "/PocketBoard/$key/agree" to agree,
        )
        val databaseRef = Firebase.database.reference
        databaseRef.updateChildren(childUpdates).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                binding.agreeNumber.text = agree.toString()
            } else {
                Log.e("com.practice.android.pocketmate.Pocket.PocketActivity", "Failed to update agree count.")
            }
        }
    }

    private fun reduceAgree(key: String) {
        val agree = binding.agreeNumber.text.toString().toInt() - 1
        val childUpdates = hashMapOf<String, Any>(
            "/PocketBoard/$key/agree" to agree,
        )
        val databaseRef = Firebase.database.reference
        databaseRef.updateChildren(childUpdates).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                binding.agreeNumber.text = agree.toString()
            } else {
                Log.e("com.practice.android.pocketmate.Pocket.PocketActivity", "Failed to update agree count.")
            }
        }
    }

    private fun raiseDisagree(key: String) {
        val disagree = binding.disagreeNumber.text.toString().toInt() + 1
        val childUpdates = hashMapOf<String, Any>(
            "/PocketBoard/$key/disagree" to disagree,
        )
        val databaseRef = Firebase.database.reference
        databaseRef.updateChildren(childUpdates).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                binding.disagreeNumber.text = disagree.toString()
            } else {
                Log.e("com.practice.android.pocketmate.Pocket.PocketActivity", "Failed to update disagree count.")
            }
        }
    }

    private fun reduceDisagree(key: String) {
        val disagree = binding.disagreeNumber.text.toString().toInt() - 1
        val childUpdates = hashMapOf<String, Any>(
            "/PocketBoard/$key/disagree" to disagree,
        )
        val databaseRef = Firebase.database.reference
        databaseRef.updateChildren(childUpdates).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                binding.disagreeNumber.text = disagree.toString()
            } else {
                Log.e("com.practice.android.pocketmate.Pocket.PocketActivity", "Failed to update disagree count.")
            }
        }
    }

    private fun getCommentData(key: String) {
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                commentList.clear()

                for (data in dataSnapshot.children) {
                    val comment = data.getValue(CommentModel::class.java)!!
                    if (comment.uid != FBAuth.getUid()) {
                        Log.d("Comment", "??")
                        commentBinding.deleteBtn.visibility = View.GONE
                    }
                    binding.emptyCommentText.visibility = View.GONE
                    getNickname(FBAuth.getUid()) { nickname ->
                        binding.nickname.text = nickname
                    }
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
        val newComment = binding.writeCommentArea.text.toString().trim()
        if (newComment.isEmpty()) {
            Toast.makeText(this, "댓글을 입력해주세요.", Toast.LENGTH_SHORT).show()
        }
        else {
            val commentKey = FBRef.commentRef.child(key).push().key.toString()
            val commentContent = newComment
            val comment = CommentModel(0, FBAuth.getUid(), commentContent)
            FBRef.commentRef.child(key).child(commentKey).setValue(comment)
        }
    }
}

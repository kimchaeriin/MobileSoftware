package com.practice.android.pocketmate.Tip

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.practice.android.pocketmate.Adapter.CommentAdapter
import com.practice.android.pocketmate.Bookmark.BookmarkModel
import com.practice.android.pocketmate.Model.BoardModel
import com.practice.android.pocketmate.Model.CommentModel
import com.practice.android.pocketmate.R
import com.practice.android.pocketmate.databinding.ActivityTipBinding
import com.practice.android.pocketmate.databinding.ItemCommentBinding
import com.practice.android.pocketmate.util.ScreenUtils
import com.practice.android.pocketmate.util.FBAuth
import com.practice.android.pocketmate.util.FBAuth.Companion.getNickname
import com.practice.android.pocketmate.util.FBRef
import kotlin.properties.Delegates

class TipActivity : AppCompatActivity() {
    lateinit var binding : ActivityTipBinding
    lateinit var commentBinding: ItemCommentBinding
    private var tip = BoardModel()
    private val bookmarkedIdList = mutableListOf<String>()
    private val commentList = mutableListOf<CommentModel>()
    private val commentKeyList = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTipBinding.inflate(layoutInflater)
        commentBinding = ItemCommentBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val key = intent.getStringExtra("key").toString()
        val agreed = checkAgreed(key)

        bindItems(key)
        handleBtns(key)
        setupCommentView(key)
    }

    private fun setupCommentView(key: String) {
        binding.commentArea.adapter = CommentAdapter(this, tip.uid, commentList, commentKeyList)
        binding.commentArea.layoutManager = LinearLayoutManager(this)
    }

    private fun bindItems(key: String) {
        getTip(key)
        bindCommentData(key)
        getBookmarkedIdList(key)
    }

    private fun checkAgreed(key: String): Boolean {
        var agreed = false
        return agreed
    }

    private fun getTip(key : String) {
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                tip = dataSnapshot.getValue(BoardModel::class.java)!!
                binding.title.text = tip.title
                binding.date.text = tip.date
                binding.content.text = tip.content
                binding.content.setTextColor(tip.color)
                getNickname(tip.uid) { nickname ->
                    Log.d("getTip", "nickname = ${nickname}")
                    binding.nickname.text = nickname
                }
                binding.agreeNumber.text = tip.agree.toString()

                if (tip.image == 0) {
                    binding.image.visibility = View.GONE
                }
                if (tip.uid != FBAuth.getUid()) {
                    binding.editBtn.visibility = View.GONE
                    binding.deleteBtn.visibility = View.GONE
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
            }
        }
        FBRef.tipRef.child(key).addValueEventListener(postListener)
    }

    private fun handleBtns(key: String) {
        val agreed = checkAgreed(key)
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
            if (agreed) { //게시글에 나왔다 들어가면 다시 agree를 할 수 있는 오류 발생
                reduceAgree(key)
                binding.agreeBtn.setImageResource(R.drawable.baseline_thumb_up_border_24)
            }
            else {
                raiseAgree(key)
                binding.agreeBtn.setImageResource(R.drawable.baseline_thumb_up_24)
            }
            updateAgreed(key)
        }
        binding.bookmarkBtn.setOnClickListener {
            if (bookmarkedIdList.contains(key)) {
                cancelBookmark(key)
                binding.bookmarkBtn.setImageResource(R.drawable.baseline_not_bookmarked_24)
            }
            else {
                bookmark(key)
                binding.bookmarkBtn.setImageResource(R.drawable.baseline_bookmarked_24)
            }
        }
    }

    private fun updateAgreed(key: String) {
        //수정
    }

    private fun bookmark(key: String) {
        bookmarkedIdList.add(key)
        FBRef.bookmarkRef.child(FBAuth.getUid()).child(key).setValue(BookmarkModel(true))
    }

    private fun cancelBookmark(key: String) {
        bookmarkedIdList.remove(key)
        FBRef.bookmarkRef.child(FBAuth.getUid()).child(key).removeValue()
    }

    private fun getBookmarkedIdList(key: String) {
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                bookmarkedIdList.clear()
                for (data in dataSnapshot.children) {
                    bookmarkedIdList.add(data.key.toString())
                }

                if (bookmarkedIdList.contains(key)) {
                    binding.bookmarkBtn.setImageResource(R.drawable.baseline_bookmarked_24)
                }
                else {
                    binding.bookmarkBtn.setImageResource(R.drawable.baseline_not_bookmarked_24)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
            }
        }
        FBRef.bookmarkRef.child(FBAuth.getUid()).addValueEventListener(postListener)
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
                val intent = Intent(this, EditTipActivity::class.java)
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
                ScreenUtils.switchScreen(this, TipBoardActivity::class.java)
                FBRef.tipRef.child(key).removeValue()
                finish()
            }
            .show()
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

    private fun bindCommentData(key: String) {
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
                    getNickname(comment.uid) { nickname ->
                        binding.nickname.text = nickname
                    }
                    commentList.add(comment)
                    commentKeyList.add(data.key.toString())
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
            val comment = CommentModel(FBAuth.getUid(), commentContent)
            FBRef.commentRef.child(key).child(commentKey).setValue(comment)
        }
    }
}
package com.practice.android.pocketmate.Tip

import android.annotation.SuppressLint
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
import com.practice.android.pocketmate.Adapter.CommentAdapter
import com.practice.android.pocketmate.Model.BookmarkModel
import com.practice.android.pocketmate.Model.BoardModel
import com.practice.android.pocketmate.Model.CommentModel
import com.practice.android.pocketmate.Model.ReactionModel
import com.practice.android.pocketmate.R
import com.practice.android.pocketmate.databinding.ActivityTipBinding
import com.practice.android.pocketmate.databinding.ItemCommentBinding
import com.practice.android.pocketmate.util.ScreenUtils
import com.practice.android.pocketmate.util.FBAuth
import com.practice.android.pocketmate.util.FBAuth.Companion.getNickname
import com.practice.android.pocketmate.util.FBRef

class TipActivity : AppCompatActivity() {
    lateinit var binding : ActivityTipBinding
    lateinit var commentBinding: ItemCommentBinding
    private var reacted : Boolean? = null
    private var reactionNumber = 0
    private val bookmarkedIdList = mutableListOf<String>()
    private val commentList = mutableListOf<CommentModel>()
    private val commentKeyList = mutableListOf<String>()
    private val agree = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTipBinding.inflate(layoutInflater)
        commentBinding = ItemCommentBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val key = intent.getStringExtra("key").toString()

        getItems(key)
        handleBtns(key)
    }

    private fun getReaction(key: String) {
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                reactionNumber = 0
                reacted = null
                for (data in dataSnapshot.children) {
                    val reaction = data.getValue(ReactionModel::class.java)!!
                    if (reaction.react == agree) {
                        reactionNumber++
                    }
                    binding.agreeNumber.text = reactionNumber.toString()
                    if (currentUserHasResponded(data)) {
                        reacted = reaction.react
                        binding.agreeBtn.setImageResource(R.drawable.baseline_thumb_up_24)
                    }
                }
                binding.agreeNumber.text = reactionNumber.toString()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
            }
        }
        FBRef.reactionRef.child(key).addValueEventListener(postListener)
    }

    private fun currentUserHasResponded(data: DataSnapshot) : Boolean {
        return data.key.toString() == FBAuth.getUid()
    }

    private fun setupCommentView(writerUid: String, key: String) {
        binding.commentArea.adapter = CommentAdapter(this, writerUid, commentList, commentKeyList, key)
        binding.commentArea.layoutManager = LinearLayoutManager(this)
    }

    private fun getItems(key: String) {
        getTip(key)
        getReaction(key)
        getBookmarkedIdList(key)
        bindCommentData(key)
    }

    private fun getTip(key : String)  {
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val tip = dataSnapshot.getValue(BoardModel::class.java)
                binding.title.text = tip?.title
                binding.date.text = tip?.date
                binding.content.text = tip?.content
                tip?.color?.let { binding.content.setTextColor(it) }
                tip?.uid?.let {
                    getNickname(it) { nickname ->
                        binding.nickname.text = nickname
                    }
                }

                if (tip?.image == 0) {
                    binding.image.visibility = View.GONE
                }
                if (tip?.uid != FBAuth.getUid()) {
                    binding.editBtn.visibility = View.GONE
                    binding.deleteBtn.visibility = View.GONE
                }
                tip?.uid?.let { setupCommentView(it, dataSnapshot.key.toString()) }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
            }
        }
        FBRef.tipRef.child(key).addValueEventListener(postListener)
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
            if (reacted == agree) {
                reacted = null
                reduceAgree(key)
                binding.agreeBtn.setImageResource(R.drawable.baseline_thumb_up_border_24)
            }
            else {
                reacted = agree
                raiseAgree(key)
                binding.agreeBtn.setImageResource(R.drawable.baseline_thumb_up_24)
            }
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

    private fun showEditDialog(key: String) {
        MaterialAlertDialogBuilder(this)
            .setMessage("수정하시겠습니까?")
            .setNegativeButton(R.string.cancel) { dialog, which ->
                //do nothing.
            }
            .setPositiveButton(R.string.edit) { dialog, which ->
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
            .setNegativeButton(R.string.cancel) { dialog, which ->
                //do nothing.
            }
            .setPositiveButton(R.string.delete) { dialog, which ->
                ScreenUtils.switchScreen(this, TipBoardActivity::class.java)
                FBRef.tipRef.child(key).removeValue()
                FBRef.commentRef.child(key).child(FBAuth.getUid()).removeValue()
                FBRef.reactionRef.child(key).child(FBAuth.getUid()).removeValue()
                finish()
            }
            .show()
    }

    private fun raiseAgree(key: String) {
        FBRef.reactionRef.child(key).child(FBAuth.getUid()).setValue(ReactionModel(agree))
    }

    private fun reduceAgree(key: String) {
        FBRef.reactionRef.child(key).child(FBAuth.getUid()).removeValue()
    }

    private fun bindCommentData(key: String) {
        val postListener = object : ValueEventListener {
            @SuppressLint("NotifyDataSetChanged")
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
                        commentBinding.nickname.text = nickname
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
            val comment = CommentModel(FBAuth.getUid(), newComment)
            FBRef.commentRef.child(key).child(commentKey).setValue(comment)
        }
    }
}
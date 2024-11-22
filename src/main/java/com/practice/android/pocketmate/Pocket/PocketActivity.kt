package com.practice.android.pocketmate.Pocket

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
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
import com.practice.android.pocketmate.Model.BoardModel
import com.practice.android.pocketmate.Model.CommentModel
import com.practice.android.pocketmate.Model.ReactionModel
import com.practice.android.pocketmate.R
import com.practice.android.pocketmate.databinding.ActivityPocketBinding
import com.practice.android.pocketmate.databinding.ItemCommentBinding
import com.practice.android.pocketmate.util.FBAuth
import com.practice.android.pocketmate.util.FBAuth.Companion.getNickname
import com.practice.android.pocketmate.util.FBRef
import com.practice.android.pocketmate.util.ScreenUtils

data class ReactionNumber (
    var agree : Int,
    var disagree : Int
)

class PocketActivity : AppCompatActivity() {
    lateinit var binding : ActivityPocketBinding
    lateinit var commentBinding: ItemCommentBinding
    private var reactionNumber = ReactionNumber(0, 0)
    private var reacted: Boolean? = null
    private val commentList = mutableListOf<CommentModel>()
    private val commentKeyList = mutableListOf<String>()
    private val agree = true
    private val disagree = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPocketBinding.inflate(layoutInflater)
        commentBinding = ItemCommentBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val key = intent.getStringExtra("key").toString()

        bindItems(key)
        handleBtns(key)
    }

    private fun setupCommentView(writerUid: String, key: String) {
        binding.commentArea.adapter = CommentAdapter(this, writerUid, commentList, commentKeyList, key)
        binding.commentArea.layoutManager = LinearLayoutManager(this)
    }

    private fun bindItems(key: String) {
        getPocket(key)
        getReaction(key)
        getCommentData(key)
    }

    private fun getReaction(key: String) {
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                reactionNumber.agree = 0
                reactionNumber.disagree = 0
                reacted = null
                for (data in dataSnapshot.children) {
                    val reaction = data.getValue(ReactionModel::class.java)!!
                    if (reaction.react == agree) {
                        reactionNumber.agree ++
                    }
                    else if (reaction.react == disagree) {
                        reactionNumber.disagree ++
                    }
                    if (currentUserHasResponded(data)) {
                        reacted = reaction.react
                        if (reaction.react == agree) {
                            binding.agreeBtn.setImageResource(R.drawable.baseline_thumb_up_24)
                        }
                        else if(reaction.react == disagree) {
                            binding.disagreeBtn.setImageResource(R.drawable.baseline_thumb_down_24)
                        }
                    }
                }
                binding.agreeNumber.text = reactionNumber.agree.toString()
                binding.disagreeNumber.text = reactionNumber.disagree.toString()
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

    private fun getPocket(key : String) {
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val pocket = dataSnapshot.getValue(BoardModel::class.java)!!
                binding.title.text = pocket.title
                binding.date.text = pocket.date
                binding.content.text = pocket.content
                binding.content.setTextColor(pocket.color)
                getNickname(pocket.uid) { nickname -> binding.nickname.text = nickname }
                if (pocket.image == 0) {
                    binding.image.visibility = View.GONE
                }
                if (pocket.uid != FBAuth.getUid()) {
                    binding.editBtn.visibility = View.GONE
                    binding.deleteBtn.visibility = View.GONE
                }
                setupCommentView(pocket.uid, dataSnapshot.key.toString())
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
            if (reacted == disagree) {
                Toast.makeText(this, "추천과 비추천을 동시에 할 수 없습니다.", Toast.LENGTH_SHORT).show()
            }
            else {
                if(reacted == agree) {
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
        }

        binding.disagreeBtn.setOnClickListener {
            if (reacted == agree) {
                Toast.makeText(this, "추천과 비추천을 동시에 할 수 없습니다.", Toast.LENGTH_SHORT).show()
            }
            else {
                if (reacted == disagree) {
                    reacted = null
                    reduceDisagree(key)
                    binding.disagreeBtn.setImageResource(R.drawable.baseline_thumb_down_border_24)
                } else {
                    reacted = disagree
                    raiseDisagree(key)
                    binding.disagreeBtn.setImageResource(R.drawable.baseline_thumb_down_24)
                }
            }
        }
    }

    private fun showEditDialog(key: String) {
        MaterialAlertDialogBuilder(this)
            .setMessage("수정하시겠습니까?")
            .setNegativeButton(R.string.cancel) { dialog, which ->
                //do nothing.
            }
            .setPositiveButton(R.string.edit) { dialog, which ->
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
            .setNegativeButton(R.string.cancel) { dialog, which ->
                //do nothing.
            }
            .setPositiveButton(R.string.delete) { dialog, which ->
                ScreenUtils.switchScreen(this, PocketBoardActivity::class.java)
                FBRef.pocketRef.child(key).removeValue()
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

    private fun raiseDisagree(key: String) {
        FBRef.reactionRef.child(key).child(FBAuth.getUid()).setValue(ReactionModel(disagree))
    }

    private fun reduceDisagree(key: String) {
        FBRef.reactionRef.child(key).child(FBAuth.getUid()).removeValue()
    }

    private fun getCommentData(key: String) {
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
            val comment = CommentModel(FBAuth.getUid(), newComment)
            FBRef.commentRef.child(key).child(commentKey).setValue(comment)
        }
    }
}

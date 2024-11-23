package com.practice.android.pocketmate.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.practice.android.pocketmate.Model.BoardModel
import com.practice.android.pocketmate.Model.CommentModel
import com.practice.android.pocketmate.Model.ReactionModel
import com.practice.android.pocketmate.Pocket.PocketActivity
import com.practice.android.pocketmate.Tip.TipActivity
import com.practice.android.pocketmate.databinding.ItemBoardBinding
import com.practice.android.pocketmate.util.FBAuth
import com.practice.android.pocketmate.util.FBRef

class BoardViewHolder(val binding: ItemBoardBinding) : RecyclerView.ViewHolder(binding.root)

open class BoardAdapter(val context: Context,
                      val items: MutableList<BoardModel>,
                      val keyList: MutableList<String>) : RecyclerView.Adapter<BoardViewHolder>() {
    val agree = true
    val disagree = false

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BoardViewHolder =
        BoardViewHolder(ItemBoardBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: BoardViewHolder, position: Int) {
    }


    fun showPostReaction(key: String, binding: ItemBoardBinding) {
        var agreeCount = 0
        var disagreeCount = 0
        FBRef.reactionRef.child(key).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                agreeCount = 0
                disagreeCount = 0
                for (data in dataSnapshot.children) {
                    val reaction = data.getValue(ReactionModel::class.java)
                    if (reaction?.react == agree) {
                        agreeCount ++
                    }
                    else if (reaction?.react == disagree) {
                        disagreeCount ++
                    }
                }
                binding.agreeNumber.text = agreeCount.toString()
                binding.disagreeNumber.text = disagreeCount.toString()
            }

            override fun onCancelled(error: DatabaseError) {
                //읽기 실패
            }
        })
    }

    fun showCommentCount(key: String, binding: ItemBoardBinding) {
        var commentCount = 0
        FBRef.commentRef.child(key).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                commentCount = 0
                for (data in dataSnapshot.children) {
                    commentCount ++
                }
                binding.commentNumber.text = commentCount.toString()
            }

            override fun onCancelled(error: DatabaseError) {
                //읽기 실패
            }
        })
    }
}
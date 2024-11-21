package com.practice.android.pocketmate.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practice.android.pocketmate.Model.CommentModel
import com.practice.android.pocketmate.databinding.ItemCommentBinding
import com.practice.android.pocketmate.util.FBAuth
import com.practice.android.pocketmate.util.FBRef

class CommentViewHolder(val binding: ItemCommentBinding) : RecyclerView.ViewHolder(binding.root)

class CommentAdapter(val boardKey: String, val comments: MutableList<CommentModel>, val commentKeyList: MutableList<String>) : RecyclerView.Adapter<CommentViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        return CommentViewHolder(ItemCommentBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        holder.binding.comment.text = comments[position].comment
        val uid = comments[position].uid
        FBAuth.getNickname(uid) { nickname ->
            holder.binding.nickname.text = nickname
        }
        if (FBAuth.getUid() == comments[position].uid) {
            holder.binding.deleteBtn.visibility = View.VISIBLE
        }
        holder.binding.deleteBtn.setOnClickListener {
            comments.remove(comments[position])
            FBRef.commentRef.child(boardKey).child(commentKeyList[position]).removeValue()
        }
    }

    override fun getItemCount(): Int {
        return comments.size
    }
}
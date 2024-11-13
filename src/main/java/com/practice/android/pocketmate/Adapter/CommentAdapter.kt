package com.practice.android.pocketmate.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practice.android.pocketmate.Model.CommentModel
import com.practice.android.pocketmate.databinding.ItemCommentBinding

class CommentViewHolder(val binding: ItemCommentBinding) : RecyclerView.ViewHolder(binding.root)

class CommentAdapter(val comments: MutableList<CommentModel>) : RecyclerView.Adapter<CommentViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        return CommentViewHolder(ItemCommentBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        holder.binding.comment.text = comments[position].comment
        holder.binding.nickname.text = comments[position].nickname
        //이미지
    }

    override fun getItemCount(): Int {
        return comments.size
    }

}
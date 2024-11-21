package com.practice.android.pocketmate.Adapter

import android.app.Dialog
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.practice.android.pocketmate.Model.BoardModel
import com.practice.android.pocketmate.Model.CommentModel
import com.practice.android.pocketmate.databinding.ItemCommentBinding
import com.practice.android.pocketmate.util.FBAuth
import com.practice.android.pocketmate.util.FBRef

class CommentViewHolder(val binding: ItemCommentBinding) : RecyclerView.ViewHolder(binding.root)

class CommentAdapter(val context: Context, val uid: String, val comments: MutableList<CommentModel>, val commentKeyList: MutableList<String>) : RecyclerView.Adapter<CommentViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        return CommentViewHolder(ItemCommentBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        holder.binding.comment.text = comments[position].comment
        val uid = comments[position].uid
        FBAuth.getNickname(uid) { nickname ->
            holder.binding.nickname.text = nickname
        }
        if (uid == comments[position].uid) {
            Log.d("CommentAdapter", "${uid == comments[position].uid}")
            holder.binding.writerMark.visibility = View.VISIBLE
        }
        if (FBAuth.getUid() == comments[position].uid) {
            holder.binding.deleteBtn.visibility = View.VISIBLE
        }
        holder.binding.deleteBtn.setOnClickListener {
            showDialog(comments[position], commentKeyList[position])
        }
    }

    private fun showDialog(comment: CommentModel, commentKey: String) {
        MaterialAlertDialogBuilder(context)
            .setMessage("댓글을 삭제하시겠습니까?")
            .setNegativeButton("취소") { dialog, which ->
            }
            .setPositiveButton("삭제") { dialog, which ->
                comments.remove(comment)
                FBRef.commentRef.child(uid).child(commentKey).removeValue() //문제 발생
            }
            .show()
    }

    override fun getItemCount(): Int {
        return comments.size
    }
}
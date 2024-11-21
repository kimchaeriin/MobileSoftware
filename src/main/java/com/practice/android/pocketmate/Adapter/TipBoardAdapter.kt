package com.practice.android.pocketmate.Adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.View
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.practice.android.pocketmate.Bookmark.BookmarkModel
import com.practice.android.pocketmate.Model.BoardModel
import com.practice.android.pocketmate.Model.CommentModel
import com.practice.android.pocketmate.R
import com.practice.android.pocketmate.Tip.TipActivity
import com.practice.android.pocketmate.databinding.ItemBoardBinding
import com.practice.android.pocketmate.util.FBAuth
import com.practice.android.pocketmate.util.FBAuth.Companion.getNickname
import com.practice.android.pocketmate.util.FBRef

class TipBoardAdapter(context: Context,
                      items: MutableList<BoardModel>,
                      keyList: MutableList<String>,
                      val bookmarkIdList: MutableList<String>)
    : BoardAdapter(context, items, keyList) {

    override fun onBindViewHolder(holder: BoardViewHolder, position: Int) {
        val binding = holder.binding
        val key = keyList[position]
        val tip = items[position]
        bindItems(binding, tip, key)
        binding.root.setOnClickListener {
            switchScreenToTip(key)
        }

        binding.bookmarkBtn.setOnClickListener {
            if (bookmarkIdList.contains(key)) {
                unBookmark(binding, key)
            }
            else {
                bookmark(binding, key)
            }
        }
    }

    fun getCommentNumber(key: String) : String {
        var commentNumber = 0
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                commentNumber = 0

                for (data in dataSnapshot.children) {
                        commentNumber++
                }
//                CommentAdapter?.notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError) {
            // Getting Post failed, log a message
            }
        }
        FBRef.commentRef.child(key).addValueEventListener(postListener)
        return commentNumber.toString()
    }


    fun getAgreeNumber() {

    }

    private fun bookmark(binding: ItemBoardBinding, key: String) {
        FBRef.bookmarkRef.child(FBAuth.getUid()).child(key).setValue(BookmarkModel(true))
        bookmarkIdList.add(key)
        binding.bookmarkBtn.setImageResource(R.drawable.baseline_not_bookmarked_24)
    }

    private fun unBookmark(binding: ItemBoardBinding, key: String) {
        FBRef.bookmarkRef.child(FBAuth.getUid()).child(key).removeValue()
        bookmarkIdList.remove(key)
        binding.bookmarkBtn.setImageResource(R.drawable.baseline_bookmarked_24)
    }

    private fun switchScreenToTip(key: String) {
        val intent = Intent(context, TipActivity::class.java).apply {
            putExtra("key", key)
        }
        context.startActivity(intent)
    }

    private fun bindItems(binding:ItemBoardBinding, tip: BoardModel, key: String) {
        binding.boardTitle.text = tip.title
        binding.boardContent.text = tip.content
        binding.commentNumber.text = getCommentNumber(key)
        binding.bookmarkBtn.visibility = View.VISIBLE
        binding.disagreeImage.visibility = View.GONE
        binding.disagreeNumber.visibility = View.GONE
        if (tip.image == 0) {
            binding.boardImage.visibility = View.GONE
        }
        else {
            binding.boardImage.setImageResource(tip.image)
        }
        if (bookmarkIdList.contains(key)) {
            binding.bookmarkBtn.setImageResource(R.drawable.baseline_bookmarked_24)
        }
        else {
            binding.bookmarkBtn.setImageResource(R.drawable.baseline_not_bookmarked_24)
        }
    }

}
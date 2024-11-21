package com.practice.android.pocketmate.Adapter

import android.content.Context
import android.content.Intent
import android.view.View
import com.practice.android.pocketmate.Bookmark.BookmarkModel
import com.practice.android.pocketmate.Model.BoardModel
import com.practice.android.pocketmate.R
import com.practice.android.pocketmate.Tip.TipActivity
import com.practice.android.pocketmate.util.FBAuth
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
            binding.boardTitle.text = tip.title
            binding.boardContent.text = tip.content
            binding.bookmarkBtn.visibility = View.VISIBLE
            binding.disagreeImage.visibility = View.GONE
            binding.disagreeNumber.visibility = View.GONE
            if (tip.image == 0) {
                binding.boardImage.visibility = View.GONE
            }
            else {
                binding.boardImage.setImageResource(tip.image)
            }
            binding.root.setOnClickListener {
                val intent = Intent(context, TipActivity::class.java).apply {
                    putExtra("key", key)
                }
                context.startActivity(intent)
            }

            if (bookmarkIdList.contains(key)) {
                binding.bookmarkBtn.setImageResource(R.drawable.baseline_bookmarked_24)
            }
            else {
                binding.bookmarkBtn.setImageResource(R.drawable.baseline_not_bookmarked_24)
            }

            binding.bookmarkBtn.setOnClickListener {
                if (bookmarkIdList.contains(key)) {
                    FBRef.bookmarkRef.child(FBAuth.getUid()).child(key).removeValue()
                    bookmarkIdList.remove(key)
                    binding.bookmarkBtn.setImageResource(R.drawable.baseline_bookmarked_24)
                }
                else {
                    FBRef.bookmarkRef.child(FBAuth.getUid()).child(key).setValue(BookmarkModel(true))
                    bookmarkIdList.add(key)
                    binding.bookmarkBtn.setImageResource(R.drawable.baseline_not_bookmarked_24)
                }
            }
        }

    }
package com.practice.android.pocketmate.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practice.android.pocketmate.Model.BoardModel
import com.practice.android.pocketmate.R
import com.practice.android.pocketmate.Tip.TipActivity
import com.practice.android.pocketmate.databinding.ActivityBookmarkTipListBinding
import com.practice.android.pocketmate.databinding.ItemBoardBinding

class BookmarkViewHolder(val binding: ItemBoardBinding): RecyclerView.ViewHolder(binding.root)

class BookmarkAdapter(val context: Context,
                      val tipList: MutableList<BoardModel>,
                      val keyList: MutableList<String>,
                      val bookmarkIdList: MutableList<String>)
    : RecyclerView.Adapter<BookmarkViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookmarkViewHolder {
        return BookmarkViewHolder(ItemBoardBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return tipList.size
    }

    override fun onBindViewHolder(holder: BookmarkViewHolder, position: Int) {
        bindItems(holder.binding, tipList[position], keyList[position])
    }

    private fun bindItems(binding: ItemBoardBinding, tip: BoardModel, key: String) {
        binding.boardTitle.text = tip.title
        binding.boardContent.text = tip.content
        binding.root.setOnClickListener {
            val intent = Intent(context, TipActivity::class.java)
            intent.putExtra("key", key)
            context.startActivity(intent)
        }
    }
}
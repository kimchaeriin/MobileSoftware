package com.practice.android.pocketmate.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practice.android.pocketmate.Model.BoardModel
import com.practice.android.pocketmate.R
import com.practice.android.pocketmate.databinding.ItemBoardBinding

class BookmarkViewHolder(val binding: ItemBoardBinding): RecyclerView.ViewHolder(binding.root)

class BookmarkAdapter(val context: Context,
                      val tipList: MutableList<BoardModel>,
                      val keyList: MutableList<String>,
                      val bookmarkIdList: MutableList<String>)
    : RecyclerView.Adapter<BookmarkViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookmarkViewHolder {
        val view = ItemBoardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BookmarkViewHolder(view)
    }

    override fun getItemCount(): Int {
        return tipList.size
    }

    override fun onBindViewHolder(holder: BookmarkViewHolder, position: Int) {
        holder.binding.boardTitle.text = tipList[position].title
    }
}
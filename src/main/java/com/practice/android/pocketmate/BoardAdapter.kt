package com.practice.android.pocketmate

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practice.android.pocketmate.databinding.ItemBoardBinding

class BoardViewHolder(val binding: ItemBoardBinding) : RecyclerView.ViewHolder(binding.root)

class BoardAdapter (val items: MutableList<BoardModel>): RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        BoardViewHolder(ItemBoardBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val binding = (holder as BoardViewHolder).binding
        binding.boardTitle.text = items[position].title
        binding.boardContent.text = items[position].content
        binding.boardImage.setImageResource(items[position].image)
    }

    override fun getItemCount(): Int {
        return items.size
    }
}

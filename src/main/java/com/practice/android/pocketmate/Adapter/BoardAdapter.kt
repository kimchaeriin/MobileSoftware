package com.practice.android.pocketmate.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.practice.android.pocketmate.Model.BoardModel
import com.practice.android.pocketmate.Tip.TipActivity
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
//        binding.root.setOnClickListener { //수정 필요
//            val intent = Intent(context, TipActivity::class.java)
//            intent.putExtra("key", holder.itemId)
//            context.startActivity(intent)
//        }
        binding.root.setOnClickListener {

        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
}

package com.practice.android.pocketmate.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.practice.android.pocketmate.Model.BoardModel
import com.practice.android.pocketmate.Tip.TipActivity
import com.practice.android.pocketmate.databinding.ItemBoardBinding

class BoardViewHolder(val binding: ItemBoardBinding) : RecyclerView.ViewHolder(binding.root)

class BoardAdapter (val context: Context, val items: MutableList<BoardModel>, val keyList: MutableList<String>)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        BoardViewHolder(ItemBoardBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val binding = (holder as BoardViewHolder).binding
        binding.boardTitle.text = items[position].title
        binding.boardContent.text = items[position].content
        if (items[position].image == 0) {
            binding.boardImage.visibility = View.GONE
        }
        else {
            binding.boardImage.setImageResource(items[position].image)
        }
        binding.root.setOnClickListener {
            val intent = Intent(context, TipActivity::class.java).apply {
                putExtra("key", keyList[position])
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
}

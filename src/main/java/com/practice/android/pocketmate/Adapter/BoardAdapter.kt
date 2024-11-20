package com.practice.android.pocketmate.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practice.android.pocketmate.Model.BoardModel
import com.practice.android.pocketmate.Tip.TipActivity
import com.practice.android.pocketmate.databinding.ItemBoardBinding

class BoardViewHolder(val binding: ItemBoardBinding) : RecyclerView.ViewHolder(binding.root)

open class BoardAdapter(val context: Context,
                      val items: MutableList<BoardModel>,
                      val keyList: MutableList<String>) : RecyclerView.Adapter<BoardViewHolder>() {
    override fun getItemCount(): Int {
        return items.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BoardViewHolder =
        BoardViewHolder(ItemBoardBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: BoardViewHolder, position: Int) {
        val binding = holder.binding
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
}

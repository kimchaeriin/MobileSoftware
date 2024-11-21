package com.practice.android.pocketmate.Adapter

import android.content.Context
import android.content.Intent
import android.view.View
import com.practice.android.pocketmate.Model.BoardModel
import com.practice.android.pocketmate.Pocket.PocketActivity
import com.practice.android.pocketmate.databinding.ItemBoardBinding

class PocketBoardAdapter(context: Context,
                         items: MutableList<BoardModel>,
                         keyList: MutableList<String>)
    : BoardAdapter(context, items, keyList) {

    override fun onBindViewHolder(holder: BoardViewHolder, position: Int) {
        val binding = holder.binding
        val key = keyList[position]
        val tip = items[position]
        bindItems(binding, tip)
        binding.root.setOnClickListener {
            switchScreenToPocket(key)
        }
    }

    private fun switchScreenToPocket(key: String) {
        val intent = Intent(context, PocketActivity::class.java).apply {
            putExtra("key", key)
        }
        context.startActivity(intent)
    }

    private fun bindItems(binding: ItemBoardBinding, tip: BoardModel) {
        binding.boardTitle.text = tip.title
        binding.boardContent.text = tip.content
        if (tip.image == 0) {
            binding.boardImage.visibility = View.GONE
        }
        else {
            binding.boardImage.setImageResource(tip.image)
        }
    }
}
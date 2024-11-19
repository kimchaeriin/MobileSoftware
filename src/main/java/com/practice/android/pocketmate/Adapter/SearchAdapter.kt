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

class SearchViewHolder(val binding: ItemBoardBinding) : RecyclerView.ViewHolder(binding.root)

class SearchAdapter(
    private val context: Context,
    private val itemList: MutableList<BoardModel>,
    private val keyList: MutableList<String>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var filteredItemList: MutableList<BoardModel> = itemList.toMutableList()
    private var filteredKeyList: MutableList<String> = keyList.toMutableList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = ItemBoardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SearchViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val binding = (holder as SearchViewHolder).binding
        val currentItem = filteredItemList[position]

        binding.boardTitle.text = currentItem.title
        binding.boardContent.text = currentItem.content

        if (currentItem.image == 0) {
            binding.boardImage.visibility = View.GONE
        } else {
            binding.boardImage.setImageResource(currentItem.image)
        }

        binding.root.setOnClickListener {
            val intent = Intent(context, TipActivity::class.java).apply {
                putExtra("key", filteredKeyList[position])
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = filteredItemList.size

    fun filter(query: String) {
        if (query.isNotEmpty()) {
            val filteredPairs = itemList.zip(keyList).filter {
                it.first.title.contains(query, ignoreCase = true)
            }
            filteredItemList = filteredPairs.map { it.first }.toMutableList()
            filteredKeyList = filteredPairs.map { it.second }.toMutableList()
        } else {
            filteredItemList = itemList.toMutableList()
            filteredKeyList = keyList.toMutableList()
        }
        notifyDataSetChanged()
    }
}


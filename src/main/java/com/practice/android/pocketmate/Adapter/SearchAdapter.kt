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
    private val itemList: MutableList<BoardModel>, // 원본 데이터
    private val keyList: MutableList<String> // 원본 키 리스트
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    // 필터링된 데이터 목록
    private var filteredItemList: MutableList<BoardModel> = itemList.toMutableList()
    private var filteredKeyList: MutableList<String> = keyList.toMutableList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = ItemBoardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BoardViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val binding = (holder as BoardViewHolder).binding
        val currentItem = filteredItemList[position]

        binding.boardTitle.text = currentItem.title
        binding.boardContent.text = currentItem.content

        if (currentItem.image == 0) {
            binding.boardImage.visibility = View.GONE
        } else {
            binding.boardImage.setImageResource(currentItem.image)
        }

        // 클릭 이벤트 처리
        binding.root.setOnClickListener {
            val intent = Intent(context, TipActivity::class.java).apply {
                putExtra("key", filteredKeyList[position])
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = filteredItemList.size

    // 검색 필터링 함수
    fun filter(query: String) {
        if (query.isNotEmpty()) {
            // 검색 조건에 맞는 데이터만 필터링
            val filteredPairs = itemList.zip(keyList).filter {
                it.first.title.contains(query, ignoreCase = true)
            }
            filteredItemList = filteredPairs.map { it.first }.toMutableList()
            filteredKeyList = filteredPairs.map { it.second }.toMutableList()
        } else {
            // 검색어가 없으면 원본 데이터로 복원
            filteredItemList = itemList.toMutableList()
            filteredKeyList = keyList.toMutableList()
        }
        notifyDataSetChanged() // RecyclerView 업데이트
    }
}

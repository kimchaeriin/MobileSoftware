package com.practice.android.pocketmate.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.practice.android.pocketmate.Model.BoardModel
import com.practice.android.pocketmate.Model.BookmarkModel
import com.practice.android.pocketmate.Model.ReactionModel
import com.practice.android.pocketmate.R
import com.practice.android.pocketmate.Tip.TipActivity
import com.practice.android.pocketmate.databinding.ItemBoardBinding
import com.practice.android.pocketmate.util.FBAuth
import com.practice.android.pocketmate.util.FBRef

class SearchViewHolder(val binding: ItemBoardBinding) : RecyclerView.ViewHolder(binding.root)

class SearchAdapter(
    private val context: Context,
    private val itemList: MutableList<BoardModel>,
    private val keyList: MutableList<String>,
    private val bookmarkIdList: MutableList<String>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val agree = true
    private val disagree = false
    private var filteredItemList: MutableList<BoardModel> = itemList.toMutableList()
    private var filteredKeyList: MutableList<String> = keyList.toMutableList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = ItemBoardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SearchViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val binding = (holder as SearchViewHolder).binding
        val currentItem = filteredItemList[position]
        val key = filteredKeyList[position]

        binding.boardTitle.text = currentItem.title
        binding.boardContent.text = currentItem.content
        binding.bookmarkBtn.visibility = View.VISIBLE
        binding.disagreeImage.visibility = View.GONE
        binding.disagreeNumber.visibility = View.GONE
        if (bookmarkIdList.contains(key)) {
            binding.bookmarkBtn.setImageResource(R.drawable.baseline_bookmarked_24)
        }
        else {
            binding.bookmarkBtn.setImageResource(R.drawable.baseline_not_bookmarked_24)
        }

        if (currentItem.image == 0) {
            binding.boardImage.visibility = View.GONE
        } else {
            binding.boardImage.setImageResource(currentItem.image)
        }

        binding.root.setOnClickListener {
            val intent = Intent(context, TipActivity::class.java).apply {
                putExtra("key", key)
            }
            context.startActivity(intent)
        }

        binding.bookmarkBtn.setOnClickListener {
            if (bookmarkIdList.contains(key)) {
                unBookmark(binding, key)
            }
            else {
                bookmark(binding, key)
            }
        }
        showCommentCount(key, binding)
        showPostReaction(key, binding)
    }

    private fun bookmark(binding: ItemBoardBinding, key: String) {
        FBRef.bookmarkRef.child(FBAuth.getUid()).child(key).setValue(BookmarkModel(true))
        bookmarkIdList.add(key)
        binding.bookmarkBtn.setImageResource(R.drawable.baseline_not_bookmarked_24)
    }

    private fun unBookmark(binding: ItemBoardBinding, key: String) {
        FBRef.bookmarkRef.child(FBAuth.getUid()).child(key).removeValue()
        bookmarkIdList.remove(key)
        binding.bookmarkBtn.setImageResource(R.drawable.baseline_bookmarked_24)
    }

    private fun showPostReaction(key: String, binding: ItemBoardBinding) {
        var agreeCount = 0
        var disagreeCount = 0
        FBRef.reactionRef.child(key).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                agreeCount = 0
                disagreeCount = 0
                for (data in dataSnapshot.children) {
                    val reaction = data.getValue(ReactionModel::class.java)
                    if (reaction?.react == agree) {
                        agreeCount ++
                    }
                    else if (reaction?.react == disagree) {
                        disagreeCount ++
                    }
                }
                binding.agreeNumber.text = agreeCount.toString()
                binding.disagreeNumber.text = disagreeCount.toString()
            }

            override fun onCancelled(error: DatabaseError) {
                //읽기 실패
            }
        })
    }

    fun showCommentCount(key: String, binding: ItemBoardBinding) {
        var commentCount = 0
        FBRef.commentRef.child(key).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                commentCount = 0
                for (data in dataSnapshot.children) {
                    commentCount ++
                }
                binding.commentNumber.text = commentCount.toString()
            }

            override fun onCancelled(error: DatabaseError) {
                //읽기 실패
            }
        })
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
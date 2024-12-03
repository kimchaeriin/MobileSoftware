package com.practice.android.pocketmate.Bookmark

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.practice.android.pocketmate.Adapter.TipBoardAdapter
import com.practice.android.pocketmate.Model.BoardModel
import com.practice.android.pocketmate.R
import com.practice.android.pocketmate.databinding.ActivityBookmarkTipListBinding
import com.practice.android.pocketmate.util.FBAuth
import com.practice.android.pocketmate.util.FBRef
import com.practice.android.pocketmate.util.FBRef.Companion.bookmarkRef
import com.practice.android.pocketmate.util.FBRef.Companion.tipRef
import com.practice.android.pocketmate.util.ScreenUtils.Companion.setBottomNavigationBar

class BookmarkTipListActivity : AppCompatActivity() {
    lateinit var binding : ActivityBookmarkTipListBinding
    private val tipList = mutableListOf<BoardModel>()
    private val keyList = mutableListOf<String>()
    private var bookmarkIdList = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookmarkTipListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setNavigationBar()
        getBookmarkedList()
        if (bookmarkIdList.isNotEmpty()) {
            binding.noTipText.visibility = View.GONE
        }

        setupRecyclerView()
    }

    private fun setNavigationBar() {
        binding.navigation.selectedItemId = R.id.nav_bookmark
        setBottomNavigationBar(this, binding.navigation)
    }

    private fun setupRecyclerView() {
        binding.recycler.adapter = TipBoardAdapter(this, tipList, keyList, bookmarkIdList)
        binding.recycler.layoutManager = LinearLayoutManager(this)
    }

    private fun getBookmarkedList() {
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                bookmarkIdList.clear()
                if (bookmarkIdList.isEmpty()) {
                    binding.noTipText.visibility = View.VISIBLE
                }
                for (data in dataSnapshot.children) {
                    binding.noTipText.visibility = View.GONE
                    bookmarkIdList.add(data.key.toString())
                }
                binding.recycler.adapter?.notifyDataSetChanged()
                bookmarkIdList.reverse()
                Log.e("BookmarkTipListActivity", "load bookmarkedTipList")
                getTipList()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e("BookmarkTipListActivity", "failed to get bookmarkedTipList")
            }
        }
        bookmarkRef.child(FBAuth.getUid()).addValueEventListener(postListener)
    }

    private fun getTipList() {
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                tipList.clear()
                keyList.clear()
                for (data in dataSnapshot.children) {
                    val key = data.key.toString()
                    if (bookmarkIdList.contains(key)) {
                        val tip = data.getValue(BoardModel::class.java)
                        tipList.add(tip!!)
                        keyList.add(data.key.toString())
                    }
                    if (tipList.isEmpty()) {
                        binding.noTipText.visibility = View.VISIBLE
                    }
                }
                binding.recycler.adapter?.notifyDataSetChanged()
                Log.e("BookmarkTipListActivity", "get tipList")
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e("BookmarkTipListActivity", "failed to get TipList")
            }
        }
        tipRef.addValueEventListener(postListener)
    }
}
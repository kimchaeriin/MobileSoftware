package com.practice.android.pocketmate.Bookmark

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.practice.android.pocketmate.Adapter.BookmarkAdapter
import com.practice.android.pocketmate.Model.BoardModel
import com.practice.android.pocketmate.R
import com.practice.android.pocketmate.databinding.ActivityBookmarkTipListBinding
import com.practice.android.pocketmate.util.FBAuth
import com.practice.android.pocketmate.util.FBRef
import com.practice.android.pocketmate.util.ScreenUtils

class BookmarkTipListActivity : AppCompatActivity() {
    lateinit var binding : ActivityBookmarkTipListBinding
    private val tipList = mutableListOf<BoardModel>()
    private val keyList = mutableListOf<String>()
    private var bookmarkIdList = mutableListOf<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookmarkTipListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.navigation.selectedItemId = R.id.nav_bookmark
        ScreenUtils.setBottomNavigationBar(this, binding.navigation)
    }

    override fun onResume() {
        super.onResume()
        getBookmarkedList()
        binding.recycler.adapter = BookmarkAdapter(this, tipList, keyList, bookmarkIdList)
        binding.recycler.layoutManager = LinearLayoutManager(this)
    }

    private fun getBookmarkedList() {
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (data in dataSnapshot.children) {
                    val key = data.getValue().toString()
                    bookmarkIdList.add(key)
                }
                getTipList()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                ///
            }
        }
        FBRef.bookmarkRef.child(FBAuth.getUid()).addValueEventListener(postListener)
    }

    private fun getTipList() {
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (data in dataSnapshot.children) {
                    val key = data.key.toString()
                    if (bookmarkIdList.contains(key)) {
                        val tip = data.getValue(BoardModel::class.java)
                        tipList.add(tip!!)
                        keyList.add(data.key.toString())
                    }
                }
                binding.recycler.adapter?.notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        }
        FBRef.tipRef.addValueEventListener(postListener)
    }
}
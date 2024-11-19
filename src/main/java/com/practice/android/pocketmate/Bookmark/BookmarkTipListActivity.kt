package com.practice.android.pocketmate.Bookmark

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.practice.android.pocketmate.Adapter.BoardAdapter
import com.practice.android.pocketmate.Adapter.BookmarkAdapter
import com.practice.android.pocketmate.Model.BoardModel
import com.practice.android.pocketmate.databinding.ActivityBookmarkTipListBinding
import com.practice.android.pocketmate.util.FBAuth
import com.practice.android.pocketmate.util.FBRef

class BookmarkTipListActivity : AppCompatActivity() {
    lateinit var binding : ActivityBookmarkTipListBinding
    lateinit var tipList: MutableList<BoardModel>
    lateinit var keyList: MutableList<String>
    lateinit var bookmarkIdList: MutableList<String>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookmarkTipListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        tipList = getTipList()
        keyList = getKeyList()
        binding.recycler.adapter = BookmarkAdapter(this, tipList, keyList, bookmarkIdList)
        binding.recycler.layoutManager = LinearLayoutManager(this)
    }

    private fun getKeyList(): MutableList<String> {
        val keyList = mutableListOf<String>()
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (data in dataSnapshot.children) {
                    keyList.add(data.key.toString())
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                ///
            }
        }
        FBRef.bookmarkRef.child(FBAuth.getUid()).addValueEventListener(postListener)

        return keyList
    }

    private fun getTipList(): MutableList<BoardModel> {
        val tipList = mutableListOf<BoardModel>()
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (data in dataSnapshot.children) {
                    val tip = data.getValue(BoardModel::class.java)
                    tipList.add(tip!!)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        }
        FBRef.bookmarkRef.child(FBAuth.getUid()).addValueEventListener(postListener)

        return tipList
    }
}
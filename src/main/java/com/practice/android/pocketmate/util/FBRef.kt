package com.practice.android.pocketmate.util

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.practice.android.pocketmate.Model.BoardModel

class FBRef {
    companion object {
        private val database = Firebase.database

        val kakaoRef = database.getReference("kakaoUsers")
        val tipRef = database.getReference("TipBoard")
        val pocketRef = database.getReference("PocketBoard")
        val friendsRef = database.getReference("FriendsList")
        val commentRef = database.getReference("Comments")
        val nicknameRef = database.getReference("Nicknames")
        val bookmarkRef = database.getReference("Bookmarks")
    }
}
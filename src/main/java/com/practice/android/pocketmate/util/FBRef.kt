package com.practice.android.pocketmate.util

import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class FBRef {
    companion object {
        private val database = Firebase.database

        val kakaoRef = database.getReference("kakaoUsers")
        val tipRef = database.getReference("TipBoard")
        val pocketRef = database.getReference("PocketBoard")
        val friendsRef = database.getReference("FriendsList")
        val commentRef = database.getReference("Comments")

    }
}
package com.practice.android.pocketmate.util

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class FBAuth {
    companion object {
        lateinit var auth : FirebaseAuth

        fun getUid() : String {
            auth = FirebaseAuth.getInstance()
            return auth.currentUser?.uid.toString()
        }

        fun getNickname(uid: String, callback: (String) -> Unit) {
            val postListener = object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val nickname = dataSnapshot.getValue(String::class.java) ?: ""
                    callback(nickname)
                }
                override fun onCancelled(databaseError: DatabaseError) {
                    callback("")
                }
            }
            FBRef.nicknameRef.child(uid).addListenerForSingleValueEvent(postListener)
        }
    }
}
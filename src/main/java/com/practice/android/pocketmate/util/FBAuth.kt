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
                    // Getting Post failed, log a message
                    callback("") // 에러가 발생한 경우 빈 문자열을 콜백으로 전달
                }
            }
            FBRef.nicknameRef.child(uid).addListenerForSingleValueEvent(postListener)
        }
    }
}
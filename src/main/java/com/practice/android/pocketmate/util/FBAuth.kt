package com.practice.android.pocketmate.util

import com.google.firebase.auth.FirebaseAuth

class FBAuth {

    companion object {

        lateinit var auth : FirebaseAuth

        fun getUid() : String {
            auth = FirebaseAuth.getInstance()
            return auth.currentUser?.uid.toString()
        }

    }
}
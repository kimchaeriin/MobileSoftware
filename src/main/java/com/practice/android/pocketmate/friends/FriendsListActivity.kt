package com.practice.android.pocketmate.friends

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.practice.android.pocketmate.databinding.ActivityFriendsListBinding
import com.practice.android.pocketmate.util.FBAuth
import com.practice.android.pocketmate.util.FBRef


class FriendsListActivity : AppCompatActivity() {
    lateinit var binding: ActivityFriendsListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityFriendsListBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbarFriends)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        supportFragmentManager.beginTransaction()
            .replace(binding.fragmentFriends.id, FriendsListFragment())
            .addToBackStack(null)
            .commit()

    }

    private fun setOnQueryTextListener() {
        binding.searchFriend.setOnQueryTextListener(object: androidx.appcompat.widget.SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {

                return true
            }
        })
    }

    private fun addFriends(friendId:String){
        val user = Firebase.auth.currentUser
        val uid = FBAuth.getUid()

        if(user != null){
            // User is signed in

            FBRef.friendsRef.child(uid).child("friends").push().setValue(friendId)
                .addOnSuccessListener {
                    Log.d(uid,"친구 추가 성공")
                    FBRef.friendsRef.child(friendId).child("friends").push().setValue(uid)
                }
                .addOnFailureListener{ Log.d(uid,"친구 추가 실패")}

        } else{
            Toast.makeText(this,"로그인을 해주세요",Toast.LENGTH_SHORT).show()
        }

    }

    private fun getFriendsList(callback: (List<String>) -> Unit){
        val uid = FBAuth.getUid()
        FBRef.friendsRef.child(uid).child("friends").get()
            .addOnSuccessListener { snapshot ->
            if(snapshot.exists()){
                val friends = snapshot.children.mapNotNull { it.key }
                callback(friends)
            }
        }
            .addOnFailureListener {
                Log.d("listFriends","fail")
                callback(emptyList())
            }
    }
}

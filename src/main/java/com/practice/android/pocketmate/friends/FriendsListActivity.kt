package com.practice.android.pocketmate.friends

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.practice.android.pocketmate.R
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
        replaceFragment(FriendsListFragment())

        binding.searchFriend.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                replaceFragment(FriendsOnSearchFragment())

                val fid = query.toString().trim()

                val eventHandlerAdd = object : DialogInterface.OnClickListener {
                    override fun onClick(dialogInterface: DialogInterface?,whilch:Int){
                        if(whilch == DialogInterface.BUTTON_POSITIVE) {
                            addFriends(fid)
                            Toast.makeText(this@FriendsListActivity,"추가하였습니다",Toast.LENGTH_SHORT).show()
                        }
                    }
                }

                if (query != null) {
                    alreadyFriendAdded(fid){ isadded ->
                        val fragment = supportFragmentManager.findFragmentById(R.id.fragment_friends) as? FriendsOnSearchFragment
                        FBRef.nicknameRef.child(fid).get()
                            .addOnSuccessListener { snapshot ->
                                if(snapshot.exists()) {
                                    val nickname = snapshot.getValue(String::class.java)

                                    if (nickname != null)
                                        fragment?.changeNickname(nickname)
                                }
                            }

                        if(isadded) {
                            fragment?.addedFriend()
                        }
                        else{
                            fragment?.addFriend()
                            fragment?.setAddButtonClickListener(View.OnClickListener {

                                AlertDialog.Builder(this@FriendsListActivity).run {
                                    setTitle("친구 추가")
                                    setMessage("정말로 친구를 추가하시겠습니까?")
                                    setPositiveButton("확인",eventHandlerAdd)
                                    setNegativeButton("취소"){d,w -> Toast.makeText(this@FriendsListActivity,"취소하였습니다",Toast.LENGTH_SHORT).show() }
                                    show()
                                }
                            })
                        }
                    }

                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })
    }
    private fun replaceFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction()
            .replace(binding.fragmentFriends.id, fragment)
            .addToBackStack(null)
            .commit()
    }

    private fun addFriends(friendId:String){
        val user = Firebase.auth.currentUser //현재 로그인한 사용자 가져오기

        if(user != null){
            // User is signed in
            val uid = FBAuth.getUid()
            var userRef = FBRef.friendsRef.child(uid)

            userRef.child(friendId).setValue(true)
                .addOnSuccessListener {
                    Toast.makeText(this,"친구를 추가하였습니다",Toast.LENGTH_SHORT).show()
                    userRef = FBRef.friendsRef.child(friendId)
                    userRef.child(uid).setValue(true)
                }
                .addOnFailureListener{ Toast.makeText(this,"친구 추가 실패",Toast.LENGTH_LONG).show()}

        } else{
            Toast.makeText(this,"로그인을 해주세요",Toast.LENGTH_SHORT).show()
        }

    }

    private fun alreadyFriendAdded(friendId: String,callback: (Boolean) -> Unit){
        val userRef = FBRef.friendsRef.child(FBAuth.getUid()).child(friendId)

        userRef.get().addOnSuccessListener { snapshot ->
            if(snapshot.exists()){
                callback(true)
            }
            else{
                callback(false)
            }
        }
            .addOnFailureListener {
                Log.d("alreadyFriendAdded","checkFailure")
                callback(false)
            }
    }

}

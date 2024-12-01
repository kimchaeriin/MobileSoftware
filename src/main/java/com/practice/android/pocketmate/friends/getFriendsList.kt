package com.practice.android.pocketmate.friends

import com.practice.android.pocketmate.Model.FriendModel
import com.practice.android.pocketmate.util.FBAuth
import com.practice.android.pocketmate.util.FBRef

 fun getFriendsList(callback: (List<FriendModel>) -> Unit){

    val uid = FBAuth.getUid()
     val fRef = FBRef.friendsRef.child(uid)

    fRef.get()
        .addOnSuccessListener { snapshot ->
            if(snapshot.exists()){
                val friends = snapshot.children.mapNotNull { it.key }
                val friendList = mutableListOf<FriendModel>()

                if(friends.isEmpty()){
                    callback(emptyList())
                    return@addOnSuccessListener
                }

                friends.forEach{ fid ->
                    FBRef.nicknameRef.child(fid).get()
                        .addOnSuccessListener { snapshot ->
                            val nickname = snapshot.value.toString()

                            if (nickname != null) {
                                friendList.add(FriendModel(0,nickname))
                            }

                            if(friendList.size == friends.size)
                                callback(friendList)
                        }

                }

                if(friends.isEmpty()){
                    callback(emptyList())
                }

            }
        }

}
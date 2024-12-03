package com.practice.android.pocketmate.Adapter

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.practice.android.pocketmate.Model.FriendModel
import com.practice.android.pocketmate.databinding.ItemProfileBinding
import com.practice.android.pocketmate.util.FBAuth
import com.practice.android.pocketmate.util.FBRef

class FriendViewHolder(val binding: ItemProfileBinding): RecyclerView.ViewHolder(binding.root){
    val btn = binding.btnDel
}

class FriendAdapter (
    private val context: Context,
    private val fList: MutableList<FriendModel>,
): RecyclerView.Adapter<FriendViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendViewHolder =
        FriendViewHolder(ItemProfileBinding.inflate(LayoutInflater.from(parent.context),parent,false))

    override fun onBindViewHolder(holder: FriendViewHolder, position: Int) {
        val friend = fList[position]
        holder.binding.nickname.text = friend.nickname
        //Glide.with(holder.itemView.context).load(friend.image).into(holder.binding.pocketImg)
        holder.btn.setOnClickListener {
            AlertDialog.Builder(context).run {
                setTitle("친구 삭제")
                setMessage("정말로 친구를 삭제하시겠습니까?")
                setPositiveButton("확인"){d,w ->
                    removeItem(position)
                }
                setNegativeButton("취소"){d, w -> Toast.makeText(context,"취소하였습니다", Toast.LENGTH_SHORT).show()}
                show()
            }
        }
    }

    override fun getItemCount() = fList.size

    private fun removeItem(position:Int){
        val friend = fList[position]
        delFriend(friend.uid)
        fList.removeAt(position)
        notifyItemRemoved(position)
        notifyItemChanged(position,fList.size-position)
    }

    private fun delFriend(friendId: String){
        var userRef = FBRef.friendsRef.child(FBAuth.getUid()).child(friendId)
        userRef.removeValue()
            .addOnSuccessListener {
                Toast.makeText(context,"친구를 삭제했습니다", Toast.LENGTH_SHORT).show()
                userRef = FBRef.friendsRef.child(friendId).child(FBAuth.getUid())
                userRef.removeValue()
            }
            .addOnFailureListener{
                Toast.makeText(context,"친구 삭제를 실패했습니다", Toast.LENGTH_SHORT).show()
            }
    }
}

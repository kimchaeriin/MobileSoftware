package com.practice.android.pocketmate.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
//import com.bumptech.glide.Glide
import com.practice.android.pocketmate.Model.FriendModel
import com.practice.android.pocketmate.databinding.ItemProfileBinding

class FriendViewHolder(val binding: ItemProfileBinding): RecyclerView.ViewHolder(binding.root)

class FriendAdapter (private val fList: List<FriendModel> ): RecyclerView.Adapter<FriendViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendViewHolder =
        FriendViewHolder(ItemProfileBinding.inflate(LayoutInflater.from(parent.context),parent,false))

    override fun onBindViewHolder(holder: FriendViewHolder, position: Int) {
        val friend = fList[position]
        holder.binding.nickname.text = friend.uid
        //Glide.with(holder.itemView.context).load(friend.image).into(holder.binding.pocketImg)
    }

    override fun getItemCount() = fList.size

}
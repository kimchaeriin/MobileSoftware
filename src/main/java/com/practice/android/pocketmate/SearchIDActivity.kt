package com.practice.android.pocketmate

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.RecyclerView
import com.practice.android.pocketmate.databinding.ActivitySearchIdactivityBinding
import com.practice.android.pocketmate.databinding.ItemBoardBinding
import com.practice.android.pocketmate.databinding.ItemFriendBinding

data class FriendModel(
    val image : Int = 0,
    val nickname: String = ""
)

class FriendViewHolder(val binding: ItemFriendBinding): RecyclerView.ViewHolder(binding.root)

class FriendAdapter(val items: MutableList<FriendModel>):RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        FriendViewHolder(ItemFriendBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val binding = (holder as FriendViewHolder).binding
        binding.image.setImageResource(items[position].image)
        binding.nickname.text = items[position].nickname
    }

}

class SearchIDActivity : AppCompatActivity() {

    lateinit var binding : ActivitySearchIdactivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchIdactivityBinding.inflate(layoutInflater)
        setContentView(binding.root)



    }
}
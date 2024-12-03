package com.practice.android.pocketmate.friends

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.practice.android.pocketmate.databinding.FragmentFriendsOnSearchBinding

class FriendsOnSearchFragment : Fragment() {
    private lateinit var binding : FragmentFriendsOnSearchBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentFriendsOnSearchBinding.inflate(layoutInflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.onSearchBtnAdd.setOnClickListener{

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFriendsOnSearchBinding.inflate(layoutInflater)
        return binding.root
    }

    fun addFriend(){
        binding.onSearchBtnAdd.isVisible = true
    }

    fun addedFriend(){
        binding.onSearchBtnAdd.isVisible = false
    }

    fun setAddButtonClickListener(listener: View.OnClickListener){
        binding.onSearchBtnAdd.setOnClickListener(listener)
    }

    fun changeNickname(nickname:String){
        binding.onSearchNickname.text = nickname
    }
}

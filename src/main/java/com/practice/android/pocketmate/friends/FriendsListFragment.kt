package com.practice.android.pocketmate.friends

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.practice.android.pocketmate.R
import com.practice.android.pocketmate.databinding.FragmentFriendsListBinding

class FriendsListFragment : Fragment() {
    lateinit var binding : FragmentFriendsListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFriendsListBinding.inflate(layoutInflater)
        return binding.root
    }

}

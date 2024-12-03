package com.practice.android.pocketmate.friends

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.practice.android.pocketmate.Adapter.FriendAdapter
import com.practice.android.pocketmate.databinding.FragmentFriendsListBinding

class FriendsListFragment : Fragment() {
    private lateinit var binding : FragmentFriendsListBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.friendsRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        getFriendsList { friends ->
            val adapter = FriendAdapter(friends)
            binding.friendsRecyclerView.adapter = adapter
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFriendsListBinding.inflate(layoutInflater)
        return binding.root
    }


}

package com.practice.android.pocketmate.Tip

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.practice.android.pocketmate.Adapter.BoardAdapter
import com.practice.android.pocketmate.Adapter.SearchAdapter
import com.practice.android.pocketmate.Adapter.TipBoardAdapter
import com.practice.android.pocketmate.Model.BoardModel
import com.practice.android.pocketmate.databinding.FragmentMyTipBinding
import com.practice.android.pocketmate.util.FBAuth
import com.practice.android.pocketmate.util.FBRef
import kotlinx.coroutines.NonCancellable.children

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class MyTipFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private var _binding: FragmentMyTipBinding? = null
    private val binding get() = _binding!!
    private val tipList = mutableListOf<BoardModel>()
    private val keyList = mutableListOf<String>()
    private val bookmarkedIdList = mutableListOf<String>()
    lateinit var boardAdapter: BoardAdapter
    lateinit var searchAdapter: SearchAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentMyTipBinding.inflate(inflater, container, false)

        getMyTipList()
        getBookmarkedIdList()

        boardAdapter = TipBoardAdapter(requireContext(), tipList, keyList, bookmarkedIdList)

        binding.recycler.adapter = boardAdapter
        binding.recycler.layoutManager = LinearLayoutManager(context)

        return binding.root
    }

    private fun getMyTipList() : MutableList<BoardModel> {
        FBRef.tipRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                keyList.clear()
                tipList.clear()

                for (data in dataSnapshot.children) {
                    binding.noTipText.visibility = View.GONE
                    val tip = data.getValue(BoardModel::class.java)
                    if (tip!!.uid == FBAuth.getUid()) {
                        tipList.add(tip)
                        keyList.add(data.key.toString())
                    }
                }
                binding.recycler.adapter?.notifyDataSetChanged()
                tipList.reverse()
                keyList.reverse()
            }

            override fun onCancelled(error: DatabaseError) {
                //읽기 실패
            }
        })

        return tipList
    }

    private fun getBookmarkedIdList() {
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                bookmarkedIdList.clear()
                for (data in dataSnapshot.children) {
                    bookmarkedIdList.add(data.key.toString())
                }
                binding.recycler.adapter?.notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
            }
        }
        FBRef.bookmarkRef.child(FBAuth.getUid()).addValueEventListener(postListener)
    }
}
package com.practice.android.pocketmate.Tip

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.practice.android.pocketmate.Adapter.BoardAdapter
import com.practice.android.pocketmate.Model.BoardModel
import com.practice.android.pocketmate.R
import com.practice.android.pocketmate.databinding.ActivityTipBoardBinding
import com.practice.android.pocketmate.databinding.FragmentAllTipBoardBinding
import com.practice.android.pocketmate.util.FBAuth
import com.practice.android.pocketmate.util.FBRef

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AllTipBoardFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AllTipBoardFragment : Fragment() {
    private var _binding : FragmentAllTipBoardBinding? = null
    private val binding get() = _binding!!
    private val tipList = mutableListOf<BoardModel>()
    private val keyList = mutableListOf<String>()
    lateinit var boardAdapter: BoardAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAllTipBoardBinding.inflate(inflater, container, false)

        getTipList()

        if (tipList.isNotEmpty()) {
            binding.noTipText.visibility = View.GONE
        }
        boardAdapter = BoardAdapter(requireContext(), tipList, keyList)

        binding.recycler.adapter = boardAdapter
        binding.recycler.layoutManager = LinearLayoutManager(context)

        return binding.root
    }

    private fun getTipList() {
        FBRef.tipRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                tipList.clear()
                keyList.clear()

                for (data in dataSnapshot.children) {
                    binding.noTipText.visibility = View.GONE
                    val tip = data.getValue(BoardModel::class.java)
                    tipList.add(tip!!)
                    keyList.add(data.key.toString())
                }
                binding.recycler.adapter?.notifyDataSetChanged()
                tipList.reverse()
                keyList.reverse()
            }

            override fun onCancelled(error: DatabaseError) {
                //읽기 실패
            }
        })
    }
}

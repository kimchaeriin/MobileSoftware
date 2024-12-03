package com.practice.android.pocketmate.Pocket

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.practice.android.pocketmate.Adapter.BoardAdapter
import com.practice.android.pocketmate.Adapter.PocketBoardAdapter
import com.practice.android.pocketmate.Adapter.SearchAdapter
import com.practice.android.pocketmate.Model.BoardModel
import com.practice.android.pocketmate.R
import com.practice.android.pocketmate.databinding.FragmentMyPocketBoardBinding
import com.practice.android.pocketmate.util.FBAuth
import com.practice.android.pocketmate.util.FBRef

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MyPocketBoardFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MyPocketBoardFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private val pocketList = mutableListOf<BoardModel>()
    private val keyList = mutableListOf<String>()
    lateinit var binding : FragmentMyPocketBoardBinding
    lateinit var boardAdapter: BoardAdapter

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
        binding = FragmentMyPocketBoardBinding.inflate(inflater, container, false)
        getMyPocketList()
        boardAdapter = PocketBoardAdapter(requireContext(), pocketList, keyList)
        setupRecyclerView()
        return binding.root
    }

    private fun setupRecyclerView() {
        binding.recycler.adapter = boardAdapter
        binding.recycler.layoutManager = LinearLayoutManager(context)
    }

    private fun getMyPocketList() {
        FBRef.pocketRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                pocketList.clear()
                keyList.clear()
                for (data in dataSnapshot.children) {
                    binding.noPocketText.visibility = View.GONE
                    val pocket = data.getValue(BoardModel::class.java)
                    if (pocket!!.uid == FBAuth.getUid()) {
                        pocketList.add(pocket)
                        keyList.add(data.key.toString())
                    }
                }
                if (pocketList.isEmpty()) {
                    binding.noPocketText.visibility = View.VISIBLE
                }
                binding.recycler.adapter?.notifyDataSetChanged()
                pocketList.reverse()
                keyList.reverse()
            }

            override fun onCancelled(error: DatabaseError) {
                //읽기 실패
            }
        })
    }
}
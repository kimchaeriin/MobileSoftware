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
import com.practice.android.pocketmate.Model.BoardModel
import com.practice.android.pocketmate.R
import com.practice.android.pocketmate.databinding.FragmentAllPocketBinding
import com.practice.android.pocketmate.util.FBRef

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AllPocketFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AllPocketFragment : Fragment() {
    private var _binding : FragmentAllPocketBinding? = null

    private val binding get() = _binding!!
    lateinit var boardAdapter: BoardAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAllPocketBinding.inflate(inflater, container, false)

        val pocketList = getPocketList()
        val keyList = getKeyList()
        if (pocketList.isNotEmpty()) {
            binding.noPocketText.visibility = View.GONE
        }
        boardAdapter = BoardAdapter(requireContext(), pocketList, keyList)

        binding.recycler.adapter = boardAdapter
        binding.recycler.layoutManager = LinearLayoutManager(context)

        return binding.root
    }

    private fun getPocketList() : MutableList<BoardModel> {
        val pocketList = mutableListOf<BoardModel>()

        FBRef.pocketRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                pocketList.clear()

                for (data in dataSnapshot.children) {
                    binding.noPocketText.visibility = View.GONE
                    val pocket = data.getValue(BoardModel::class.java)
                    pocketList.add(pocket!!)
                }
                binding.recycler.adapter?.notifyDataSetChanged()
                pocketList.reverse()
            }

            override fun onCancelled(error: DatabaseError) {
                //읽기 실패
            }
        })

        return pocketList
    }

    private fun getKeyList() : MutableList<String> {
        val keyList = mutableListOf<String>()

        FBRef.pocketRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                keyList.clear()
                for (data in dataSnapshot.children) {
                    keyList.add(data.key.toString())
                }
                binding.recycler.adapter?.notifyDataSetChanged()
                keyList.reverse()
            }

            override fun onCancelled(error: DatabaseError) {
                //읽기 실패
            }
        })

        return keyList
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        binding.buttonFirst.setOnClickListener {
//            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
//        }
    }
}
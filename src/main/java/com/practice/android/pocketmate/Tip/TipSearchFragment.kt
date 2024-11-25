package com.practice.android.pocketmate.Tip

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.practice.android.pocketmate.Adapter.BoardAdapter
import com.practice.android.pocketmate.Adapter.SearchAdapter
import com.practice.android.pocketmate.Model.BoardModel
import com.practice.android.pocketmate.R
import com.practice.android.pocketmate.databinding.ActivityTipBoardBinding
import com.practice.android.pocketmate.databinding.FragmentTipSearchBinding
import com.practice.android.pocketmate.util.FBRef

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [TipSearchFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TipSearchFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    lateinit var binding: FragmentTipSearchBinding
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
        binding = FragmentTipSearchBinding.inflate(inflater, container, false)
        val tipBoardBinding = ActivityTipBoardBinding.inflate(layoutInflater)

        val tipList = getTipList()
        val keyList = getKeyList()

        tipBoardBinding.searchView.visibility = View.VISIBLE

        searchAdapter = SearchAdapter(requireContext(), tipList, keyList)
        return binding.root
    }

    fun filter(query: String) {
        searchAdapter.filter(query)
    }

    private fun getTipList() : MutableList<BoardModel> {
        val tipList = mutableListOf<BoardModel>()

        FBRef.tipRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                tipList.clear()

                for (data in dataSnapshot.children) {
                    val tip = data.getValue(BoardModel::class.java)
                    tipList.add(tip!!)
                }
                binding.recycler.adapter?.notifyDataSetChanged()
                tipList.reverse()
            }

            override fun onCancelled(error: DatabaseError) {
                //읽기 실패
            }
        })

        return tipList
    }

    private fun getKeyList() : MutableList<String> {
        val keyList = mutableListOf<String>()

        FBRef.tipRef.addValueEventListener(object : ValueEventListener {
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

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment TipSearchFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            TipSearchFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
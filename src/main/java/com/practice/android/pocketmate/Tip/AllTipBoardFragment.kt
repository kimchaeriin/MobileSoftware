package com.practice.android.pocketmate.Tip

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
import com.practice.android.pocketmate.databinding.FragmentAllTipBoardBinding
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAllTipBoardBinding.inflate(inflater, container, false)

        val tipList = getTipList()
        binding.recycler.adapter = BoardAdapter(tipList)
        binding.recycler.layoutManager = LinearLayoutManager(context)

        return binding.root
    }

    fun getTipList() : MutableList<BoardModel> {
        val tipList = mutableListOf<BoardModel>()

        FBRef.tipRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                tipList.clear()

                for (data in dataSnapshot.children) {
                    val tip = data.getValue(BoardModel::class.java)
                    tipList.add(tip!!)
                }
                binding.recycler.adapter?.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                //읽기 실패
            }
        })

        return tipList
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        binding.buttonFirst.setOnClickListener {
//            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
//        }
    }

}

package com.practice.android.pocketmate.Tip

import android.os.Bundle
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
import com.practice.android.pocketmate.Adapter.SearchAdapter
import com.practice.android.pocketmate.Model.BoardModel
import com.practice.android.pocketmate.R
import com.practice.android.pocketmate.databinding.ActivityTipBoardBinding
import com.practice.android.pocketmate.databinding.FragmentTipSearchBinding
import com.practice.android.pocketmate.util.FBRef

class TipSearchFragment : Fragment() {
    private val tipList = mutableListOf<BoardModel>()
    private val keyList = mutableListOf<String>()
    private lateinit var binding: FragmentTipSearchBinding
    private lateinit var searchAdapter: SearchAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTipSearchBinding.inflate(inflater, container, false)

        // 데이터 가져오기
        getTipList()

        return binding.root
    }

    private fun getTipList() {
        FBRef.tipRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                tipList.clear()
                keyList.clear()
                for (data in dataSnapshot.children) {
                    val tip = data.getValue(BoardModel::class.java)
                    if (tip != null) {
                        tipList.add(tip)
                        keyList.add(data.key.toString())
                    }
                }
                tipList.reverse()
                keyList.reverse()

                binding.searchView.visibility = View.VISIBLE
                searchAdapter = SearchAdapter(requireContext(), tipList, keyList)
                binding.recycler.adapter = searchAdapter
                binding.recycler.layoutManager = LinearLayoutManager(requireContext())
                // 어댑터에 데이터 변경 알림
                searchAdapter.notifyDataSetChanged()
                // SearchView 설정
                binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        return false
                    }

                    override fun onQueryTextChange(newText: String?): Boolean {
                        searchAdapter.filter(newText ?: "") // 검색어 변경 시 필터 호출
                        return true
                    }
                })
            }

            override fun onCancelled(error: DatabaseError) {
                // 읽기 실패 처리
            }
        })
    }
}
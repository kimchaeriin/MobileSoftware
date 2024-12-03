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
import com.practice.android.pocketmate.Model.BoardModel
import com.practice.android.pocketmate.R
import com.practice.android.pocketmate.databinding.FragmentFriendsPocketBinding
import com.practice.android.pocketmate.util.FBAuth
import com.practice.android.pocketmate.util.FBRef

class FriendsPocketFragment : Fragment() {
    private lateinit var binding: FragmentFriendsPocketBinding
    private val pocketList = mutableListOf<BoardModel>()
    private val keyList = mutableListOf<String>()
    lateinit var boardAdapter: BoardAdapter
    private val friendsUidList = mutableListOf<String>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFriendsPocketBinding.inflate(inflater, container, false)
        getFriendsUid()
        getFriendsPocketList()
        boardAdapter = PocketBoardAdapter(requireContext(), pocketList, keyList)
        setupRecyclerView()
        return binding.root
    }

    private fun setupRecyclerView() {
        binding.recycler.adapter = boardAdapter
        binding.recycler.layoutManager = LinearLayoutManager(context)
    }

    private fun getFriendsUid() {
        FBRef.friendsRef.child(FBAuth.getUid()).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                friendsUidList.clear()
                for (data in dataSnapshot.children) {
                    binding.noPocketText.visibility = View.GONE
                    friendsUidList.add(data.value.toString())
                }
                if (pocketList.isEmpty()) {
                    binding.noPocketText.visibility = View.VISIBLE
                }
                pocketList.reverse()
                keyList.reverse()
            }

            override fun onCancelled(error: DatabaseError) {
                //읽기 실패
            }
        })
    }

    private fun getFriendsPocketList() {
        FBRef.pocketRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                pocketList.clear()
                keyList.clear()
                for (data in dataSnapshot.children) {
                    binding.noPocketText.visibility = View.GONE
                    val pocket = data.getValue(BoardModel::class.java)
                    if (pocket!!.uid in friendsUidList) {
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

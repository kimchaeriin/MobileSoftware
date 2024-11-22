package com.practice.android.pocketmate

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.practice.android.pocketmate.Tip.TipBoardActivity
import com.practice.android.pocketmate.databinding.ActivityMainBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.practice.android.pocketmate.Adapter.ViewPagerAdapter
import com.practice.android.pocketmate.Model.BoardModel
import com.practice.android.pocketmate.Pocket.PocketBoardActivity
import com.practice.android.pocketmate.friends.FriendsListActivity
import com.practice.android.pocketmate.util.ScreenUtils
import com.practice.android.pocketmate.util.FBRef

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private val pocket = false
    private val tip = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbarMain)
        val toggle = ActionBarDrawerToggle(this, binding.drawerMain, binding.toolbarMain, R.string.drawer_opened, R.string.drawer_closed)
        binding.drawerMain.addDrawerListener(toggle)
        toggle.syncState()

        binding.navigationDrawer.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_drawer_friends -> { ScreenUtils.switchScreen(this, FriendsListActivity::class.java)}
                R.id.nav_drawer_settings -> { ScreenUtils.switchScreen(this, SettingsActivity::class.java) }
            }
            binding.drawerMain.closeDrawers()
            true
        }
        getRecentTip()
        getRecentPocket()
        binding.pocketViewPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        binding.tipViewPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL

        ScreenUtils.setBottomNavigationBar(this, binding.navigation)
    }

    private fun getRecentTip() {
        val tipList = mutableListOf<BoardModel>()
        val tipKeyList = mutableListOf<String>()
        FBRef.tipRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                tipList.clear()
                tipKeyList.clear()
                val dataList = dataSnapshot.children.reversed()
                for (data in dataList) {
                    val tip = data.getValue(BoardModel::class.java)
                    tipList.add(tip!!)
                    tipKeyList.add(data.key.toString())
                }
                binding.tipViewPager.adapter = ViewPagerAdapter(this@MainActivity, tipList, tipKeyList, tip)
            }
            override fun onCancelled(error: DatabaseError) {
                //읽기 실패
            }
        })
    }

    private fun getRecentPocket() {
        val pocketList = mutableListOf<BoardModel>()
        val pocketKeyList = mutableListOf<String>()
        FBRef.pocketRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                pocketList.clear()
                pocketKeyList.clear()
                val dataList = dataSnapshot.children.reversed()
                for (data in dataList) {
                    val pocket = data.getValue(BoardModel::class.java)
                    pocketList.add(pocket!!)
                    pocketKeyList.add(data.key.toString())
                }
                binding.pocketViewPager.adapter = ViewPagerAdapter(this@MainActivity, pocketList, pocketKeyList, pocket)
            }

            override fun onCancelled(error: DatabaseError) {
                //읽기 실패
            }
        })
    }
}
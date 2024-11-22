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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var tipAdapter : ViewPagerAdapter
    private lateinit var pocketAdapter: ViewPagerAdapter
    private lateinit var binding: ActivityMainBinding
    private val pocket = false
    private val tip = true
    private val scope = CoroutineScope(Dispatchers.Main)
    private var currentTipPosition = 0
    private var currentPocketPosition = 0

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

    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()
        scope.cancel()
    }

    private fun startAutoTipScroll() {
        scope.launch {
            while (isActive) {
                delay(3000)
                if (tipAdapter.itemCount > 0) {
                    currentTipPosition = (currentTipPosition + 1) % tipAdapter.itemCount
                    binding.tipViewPager.setCurrentItem(currentTipPosition, true)
                }
            }
        }
    }

    private fun startAutoPocketScroll() {
        scope.launch {
            while (isActive) {
                delay(3000)
                if (pocketAdapter.itemCount > 0) {
                    currentPocketPosition = (currentPocketPosition + 1) % pocketAdapter.itemCount
                    binding.pocketViewPager.setCurrentItem(currentPocketPosition, true)
                }
            }
        }
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
                tipAdapter = ViewPagerAdapter(this@MainActivity, tipList, tipKeyList, tip)
                binding.tipViewPager.adapter = tipAdapter
                startAutoTipScroll()
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
                pocketAdapter = ViewPagerAdapter(this@MainActivity, pocketList, pocketKeyList, pocket)
                binding.pocketViewPager.adapter = pocketAdapter
                startAutoPocketScroll()
            }

            override fun onCancelled(error: DatabaseError) {
                //읽기 실패
            }
        })
    }
}
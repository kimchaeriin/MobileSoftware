package com.practice.android.pocketmate

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import com.practice.android.pocketmate.Tip.TipBoardActivity
import com.practice.android.pocketmate.databinding.ActivityMainBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.practice.android.pocketmate.Model.BoardModel
import com.practice.android.pocketmate.Pocket.PocketBoardActivity
import com.practice.android.pocketmate.friends.FriendsListActivity
import com.practice.android.pocketmate.util.ScreenUtils
import com.practice.android.pocketmate.util.FBRef


class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

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
                R.id.nav_drawer_friends -> { ScreenUtils.switchScreen(this,FriendsListActivity::class.java)}
                R.id.nav_drawer_settings -> { ScreenUtils.switchScreen(this,SettingsActivity::class.java) }
            }
            binding.drawerMain.closeDrawers()
            true
        }

        getRecentTip()
        getRecentPocket()
        setClickTextView()

        ScreenUtils.setBottomNavigationBar(this, binding.navigation)
    }

    private fun getRecentTip() {
        FBRef.tipRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (data in dataSnapshot.children.reversed()) {
                    val tip = data.getValue(BoardModel::class.java)
                    binding.tipTitle.text = tip?.title
                    binding.tipContent.text = tip?.content
                    break
                }
            }
            override fun onCancelled(error: DatabaseError) {
                //읽기 실패
            }
        })
    }

    private fun getRecentPocket() {
        FBRef.pocketRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (data in dataSnapshot.children.reversed()) {
                    val pocket = data.getValue(BoardModel::class.java)
                    binding.pocketTitle.text = pocket?.title
                    binding.pocketContent.text = pocket?.content
                    break
                }
            }
            override fun onCancelled(error: DatabaseError) {
                //읽기 실패
            }
        })
    }

    private fun setClickTextView() {
        binding.pocket.setOnClickListener(){
            val intent: Intent = Intent(this, PocketBoardActivity::class.java)
            startActivity(intent)

        }
        binding.tip.setOnClickListener(){
            val intent: Intent = Intent(this, TipBoardActivity::class.java)
            startActivity(intent)
        }
    }


}
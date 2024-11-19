package com.practice.android.pocketmate

import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.kakao.sdk.common.util.Utility
import com.practice.android.pocketmate.Tip.TipBoardActivity
import com.practice.android.pocketmate.Tip.WriteTipActivity
import com.practice.android.pocketmate.databinding.ActivityMainBinding
import androidx.appcompat.app.ActionBarDrawerToggle
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.practice.android.pocketmate.Model.BoardModel
import com.practice.android.pocketmate.Pocket.PocketBoardActivity
import com.practice.android.pocketmate.Tip.TipActivity
import com.practice.android.pocketmate.util.ScreenUtils
import com.practice.android.pocketmate.util.ScreenUtils.Companion.setBottomNavigationBar
import com.practice.android.pocketmate.util.FBAuth
import com.practice.android.pocketmate.util.FBAuth.Companion.getNickname
import com.practice.android.pocketmate.util.FBRef


class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
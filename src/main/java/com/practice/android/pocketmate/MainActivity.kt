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
import com.kakao.sdk.common.util.Utility
import com.practice.android.pocketmate.Tip.TipBoardActivity
import com.practice.android.pocketmate.Tip.WriteTipActivity
import com.practice.android.pocketmate.databinding.ActivityMainBinding
import androidx.appcompat.app.ActionBarDrawerToggle


class MainActivity : AppCompatActivity() {
    lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        toggle = ActionBarDrawerToggle(this, binding.drawerMain, R.string.drawer_opened, R.string.drawer_closed)
        binding.drawerMain.addDrawerListener(toggle)
        toggle.syncState()

        binding.writePocketBtn.setOnClickListener(){
            val intent: Intent = Intent(this, WriteTipActivity::class.java)
            startActivity(intent)
        }

        binding.profileBtn.setOnClickListener(){
            val intent: Intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }

        binding.tipBoardBtn.setOnClickListener(){
            switchScreen(this, TipBoardActivity::class.java)
        }

        binding.navigation.setNavigationItemSelectedListener{ menuItem->
            when(menuItem.itemId){
                R.id.menu_schedule -> navigateToMenuSchedule()
                R.id.menu_board -> navigateToMenuBoard()
                R.id.menu_profile -> navigateToMenuProfile()
            }
            binding.drawerMain.closeDrawers()
            true
        }
    }

    fun switchScreen(from: AppCompatActivity, to: Class<out AppCompatActivity>) {
        val intent = Intent(from, to)
        from.startActivity(intent)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item))
            return true
        return super.onOptionsItemSelected(item)
    }

    private fun navigateToMenuSchedule(){
        val intent: Intent = Intent(this,PocketBoardActivity::class.java)
        startActivity(intent)
    }

    private fun navigateToMenuBoard(){
        val intent: Intent = Intent(this,TipBoardActivity::class.java)
        startActivity(intent)
    }

    private fun navigateToMenuProfile(){
        val intent: Intent = Intent(this,ProfileActivity::class.java)
        startActivity(intent)
    }
}

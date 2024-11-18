package com.practice.android.pocketmate.Pocket

import MyPocketBoardActivity
import PocketAdapter
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.practice.android.pocketmate.Model.PocketModel
import com.practice.android.pocketmate.R
import com.practice.android.pocketmate.databinding.ActivityPocketBoardBinding

class PocketBoardActivity : AppCompatActivity() {
    lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityPocketBoardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        toggle = ActionBarDrawerToggle(this, binding.drawer, R.string.drawer_opened, R.string.drawer_closed)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toggle.syncState()
        binding.drawerPocket.addDrawerListener(toggle)

        val fragment = PocketBoardFragment()
        val fm:FragmentManager = supportFragmentManager
        val transaction : FragmentTransaction = fm.beginTransaction()
        transaction.add(binding.fragmentPocket.id,fragment)
        transaction.commit()

        binding.fbtnOrigin.setOnClickListener(){
            if(binding.fbtnOrigin.text == "+"){
                binding.fbtnWrite.visibility = View.VISIBLE
                binding.fbtnFriend.visibility = View.VISIBLE
                binding.fbtnMine.visibility = View.VISIBLE
                binding.fbtnOrigin.text = "x"
            }

            else{
                binding.fbtnWrite.visibility = View.GONE
                binding.fbtnFriend.visibility = View.GONE
                binding.fbtnMine.visibility = View.GONE
                binding.fbtnOrigin.text = "+"
            }
        }

        binding.fbtnWrite.setOnClickListener(){
            val intent: Intent = Intent(this,WritePocketBoardActivity::class.java)
            startActivity(intent)
        }

        binding.fbtnMine.setOnClickListener(){
            val fragment = MyPocketFragment()
            val fm:FragmentManager = supportFragmentManager
            val transaction : FragmentTransaction = fm.beginTransaction()
            transaction.replace(binding.fragmentPocket.id,fragment)
            transaction.commit()
        }

        binding.fbtnFriend.setOnClickListener(){
            val fragment = FriendsPocketFragment()
            val fm:FragmentManager = supportFragmentManager
            val transaction : FragmentTransaction = fm.beginTransaction()
            transaction.replace(binding.fragmentPocket.id,fragment)
            transaction.commit()
        }

        binding.navigation.setNavigationItemSelectedListener{ menuItem->
            Log.d("navigation","in")
            when(menuItem.itemId){
                R.id.menu_schedule -> navigateToMenuSchedule()
                R.id.menu_board -> navigateToMenuBoard()
                R.id.menu_profile -> navigateToMenuProfile()
            }
            binding.drawerPocket.closeDrawers()
            true
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(toggle.onOptionsItemSelected(item))
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

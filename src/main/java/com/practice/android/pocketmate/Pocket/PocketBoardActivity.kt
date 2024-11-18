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
//        binding.navigation.selectedItemId = R.id.nav_pocket
//        AppUtils.setBottomNavigationBar(this, binding.navigation)


        toggle = ActionBarDrawerToggle(this, binding.drawer, R.string.drawer_opened, R.string.drawer_closed)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toggle.syncState()

        val items = mutableListOf<PocketModel>() //input data
        for(i in 1..5){
            items.add(PocketModel("제목", "내용", R.drawable.ic_launcher_background))
        }
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = PocketAdapter(items)
        binding.recyclerView.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))

        binding.fbtn1.setOnClickListener(){
            if(binding.fbtn1.text == "+"){
                binding.fbtn2.visibility = View.VISIBLE
                binding.fbtn3.visibility = View.VISIBLE
                binding.fbtn4.visibility = View.VISIBLE
                binding.fbtn1.text = "x"
            }

            else{
                binding.fbtn2.visibility = View.GONE
                binding.fbtn3.visibility = View.GONE
                binding.fbtn4.visibility = View.GONE
                binding.fbtn1.text = "+"
            }
        }

        binding.fbtn2.setOnClickListener(){
            val intent: Intent = Intent(this, WritePocketBoardActivity::class.java)
            startActivity(intent)
        }

        binding.fbtn4.setOnClickListener(){
            val intent: Intent = Intent(this, MyPocketBoardActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(toggle.onOptionsItemSelected(item))
            return true
        return super.onOptionsItemSelected(item)
    }

}

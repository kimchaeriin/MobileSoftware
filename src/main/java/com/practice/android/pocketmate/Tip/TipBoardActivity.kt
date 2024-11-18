package com.practice.android.pocketmate.Tip

import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import com.practice.android.pocketmate.Adapter.BoardAdapter
import com.practice.android.pocketmate.R
import com.practice.android.pocketmate.databinding.ActivityTipBoardBinding
import com.practice.android.pocketmate.databinding.ContentMainBinding
import com.practice.android.pocketmate.util.ScreenUtils

class TipBoardActivity : AppCompatActivity() {

    lateinit var binding : ActivityTipBoardBinding
    private var isFabOpen = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTipBoardBinding.inflate(layoutInflater)

        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        binding.navigation.selectedItemId = R.id.nav_tip
        ScreenUtils.setBottomNavigationBar(this, binding.navigation)
        setOnQueryTextListener()

        handleBtns()
    }

    private fun setOnQueryTextListener() {
        binding.searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_content_main)
                val currentFragment = navHostFragment?.childFragmentManager?.fragments?.get(0)

                if (currentFragment is TipSearchFragment) {
                    currentFragment.filter(newText.orEmpty())
                }
                return true
            }
        })
    }

    fun handleBtns() {
        val navController = findNavController(R.id.nav_host_fragment_content_main)

        binding.fabMain.setOnClickListener {
            toggleFab()
        }

        binding.fabWrite.setOnClickListener {
            ScreenUtils.switchScreen(this, WriteTipActivity::class.java)
            toggleFab()
        }

        binding.fabMine.setOnClickListener {
            if (navController.currentDestination?.id == R.id.AllTipBoardFragment) {
                navController.navigate(R.id.action_AllTipBoardFragment_to_MyTipFragment)
            }
            else {
                navController.navigate(R.id.action_tipSearchFragment_to_MyTipFragment)
            }
            toggleFab()
        }

        binding.fabSearch.setOnClickListener{
            if (navController.currentDestination?.id == R.id.AllTipBoardFragment) {
                navController.navigate(R.id.action_AllTipBoardFragment_to_tipSearchFragment)
            }
            else {
                navController.navigate(R.id.action_MyTipFragment_to_tipSearchFragment)
            }
            toggleFab()
        }
    }

    private fun toggleFab() {
        if (isFabOpen) {
            ObjectAnimator.ofFloat(binding.fabMine, "translationY", -200f).apply { start() }
            ObjectAnimator.ofFloat(binding.fabSearch, "translationY", -400f).apply { start() }
            ObjectAnimator.ofFloat(binding.fabWrite, "translationY", -600f).apply { start() }
            binding.fabMain.setImageResource(R.drawable.baseline_close_24)
            // 플로팅 액션 버튼 열기 - 닫혀있는 플로팅 버튼 꺼내는 애니메이션 세팅
        } else {
            ObjectAnimator.ofFloat(binding.fabMine, "translationY", 0f).apply { start() }
            ObjectAnimator.ofFloat(binding.fabSearch, "translationY", 0f).apply { start() }
            ObjectAnimator.ofFloat(binding.fabWrite, "translationY", 0f).apply { start() }
            binding.fabMain.setImageResource(R.drawable.baseline_add_24)
        }
        isFabOpen = !isFabOpen
    }
}
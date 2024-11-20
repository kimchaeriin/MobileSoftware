package com.practice.android.pocketmate.Pocket

import android.animation.ObjectAnimator
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.practice.android.pocketmate.R
import com.practice.android.pocketmate.databinding.ActivityPocketBoardBinding
import com.practice.android.pocketmate.util.ScreenUtils

class PocketBoardActivity : AppCompatActivity() {

    lateinit var binding : ActivityPocketBoardBinding
    private var isFabOpen = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPocketBoardBinding.inflate(layoutInflater)

        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        binding.navigation.selectedItemId = R.id.nav_pocket
        ScreenUtils.setBottomNavigationBar(this, binding.navigation)
//        setOnQueryTextListener()

        handleBtns()
    }

//    private fun setOnQueryTextListener() {
//        binding.searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
//            override fun onQueryTextSubmit(query: String?): Boolean {
//                return false
//            }
//
//            override fun onQueryTextChange(newText: String?): Boolean {
//                val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_content_main)
//                val currentFragment = navHostFragment?.childFragmentManager?.fragments?.get(0)
//
//                if (currentFragment is PocketSearchFragment) {
//                    currentFragment.filter(newText.orEmpty())
//                }
//                return true
//            }
//        })
//    }

    fun handleBtns() {
        val navController = findNavController(R.id.nav_host_fragment_content_main)

        binding.fabMain.setOnClickListener {
            toggleFab()
        }
//
        binding.fabWrite.setOnClickListener {
            ScreenUtils.switchScreen(this, WritePocketBoardActivity::class.java)
            toggleFab()
        }

        binding.fabMine.setOnClickListener {
            if (navController.currentDestination?.id == R.id.allPocketFragment) {
                navController.navigate(R.id.action_AllTipBoardFragment_to_MyTipFragment)
            }
            else if (navController.currentDestination?.id == R.id.friendsPocketFragment) {
                navController.navigate(R.id.action_friendsPocketFragment_to_myPocketBoardFragment)
            }
            toggleFab()
        }
        binding.fabFriends.setOnClickListener {
            if (navController.currentDestination?.id == R.id.allPocketFragment) {
                navController.navigate(R.id.action_allPocketFragment_to_friendsPocketFragment)
            }
            else if (navController.currentDestination?.id == R.id.myPocketBoardFragment) {
                navController.navigate(R.id.action_MyTipFragment_to_tipSearchFragment)
            }
            toggleFab()
        }
    }

    private fun toggleFab() {
        if (isFabOpen) {
            ObjectAnimator.ofFloat(binding.fabMine, "translationY", -200f).apply { start() }
            ObjectAnimator.ofFloat(binding.fabFriends, "translationY", -400f).apply { start() }
            ObjectAnimator.ofFloat(binding.fabWrite, "translationY", -600f).apply { start() }
            binding.fabMain.setImageResource(R.drawable.baseline_close_24)
            // 플로팅 액션 버튼 열기 - 닫혀있는 플로팅 버튼 꺼내는 애니메이션 세팅
        } else {
            ObjectAnimator.ofFloat(binding.fabMine, "translationY", 0f).apply { start() }
            ObjectAnimator.ofFloat(binding.fabFriends, "translationY", 0f).apply { start() }
            ObjectAnimator.ofFloat(binding.fabWrite, "translationY", 0f).apply { start() }
            binding.fabMain.setImageResource(R.drawable.baseline_add_24)
        }
        isFabOpen = !isFabOpen
    }
}

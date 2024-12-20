package com.practice.android.pocketmate.Tip

import android.animation.ObjectAnimator
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.navigation.findNavController
import com.practice.android.pocketmate.R
import com.practice.android.pocketmate.databinding.ActivityTipBoardBinding
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

        handleBtns()
    }

    private fun handleBtns() {
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
    }

    private fun toggleFab() {
        if (isFabOpen) {
            ObjectAnimator.ofFloat(binding.fabMine, "translationY", -200f).apply { start() }
            ObjectAnimator.ofFloat(binding.fabWrite, "translationY", -400f).apply { start() }
            binding.fabMain.setImageResource(R.drawable.baseline_close_24)
        } else {
            ObjectAnimator.ofFloat(binding.fabMine, "translationY", 0f).apply { start() }
            ObjectAnimator.ofFloat(binding.fabWrite, "translationY", 0f).apply { start() }
            binding.fabMain.setImageResource(R.drawable.baseline_add_24)
        }
        isFabOpen = !isFabOpen
    }
}
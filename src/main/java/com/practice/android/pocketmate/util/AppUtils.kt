package com.practice.android.pocketmate.util

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.practice.android.pocketmate.MainActivity
import com.practice.android.pocketmate.Pocket.PocketBoardActivity
import com.practice.android.pocketmate.ProfileActivity
import com.practice.android.pocketmate.R
import com.practice.android.pocketmate.Tip.TipBoardActivity

class AppUtils {

    companion object {

        fun switchScreen(from: AppCompatActivity, to: Class<out AppCompatActivity>) {
            val intent = Intent(from, to)
            from.startActivity(intent)
        }

        fun setBottomNavigationBar(currentActivity: AppCompatActivity, bottomNavigationView: BottomNavigationView) {
            bottomNavigationView.setOnItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.nav_main -> {
                        switchScreen(currentActivity, MainActivity::class.java)
                        true
                    }
                    R.id.nav_profile -> {
                        switchScreen(currentActivity, ProfileActivity::class.java)
                        true
                    }
                    R.id.nav_pocket -> {
                        switchScreen(currentActivity, PocketBoardActivity::class.java)
                        true
                    }
                    R.id.nav_tip -> {
                        switchScreen(currentActivity, TipBoardActivity::class.java)
                        true
                    }
                    else -> false
                }
            }
        }
    }
}
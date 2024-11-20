package com.practice.android.pocketmate.util

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.practice.android.pocketmate.Bookmark.BookmarkTipListActivity
import com.practice.android.pocketmate.MainActivity
import com.practice.android.pocketmate.Pocket.PocketBoardActivity
import com.practice.android.pocketmate.ProfileActivity
import com.practice.android.pocketmate.R
import com.practice.android.pocketmate.Tip.TipBoardActivity

class ScreenUtils {

    companion object {
        fun switchScreen(from: AppCompatActivity, to: Class<out AppCompatActivity>) {
            val intent = Intent(from, to)
            from.startActivity(intent)
            from.overridePendingTransition(R.anim.loadfadein, R.anim.loadfadeout)
        }

        fun setBottomNavigationBar(currentActivity: AppCompatActivity, bottomNavigationView: BottomNavigationView) {
            bottomNavigationView.setOnItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.nav_main -> {
                        switchScreen(currentActivity, MainActivity::class.java)
                        currentActivity.finish()
                        true
                    }
                    R.id.nav_profile -> {
                        switchScreen(currentActivity, ProfileActivity::class.java)
                        currentActivity.finish()
                        true
                    }
                    R.id.nav_pocket -> {
                        switchScreen(currentActivity, PocketBoardActivity::class.java)
                        currentActivity.finish()
                        true
                    }
                    R.id.nav_tip -> {
                        switchScreen(currentActivity, TipBoardActivity::class.java)
                        currentActivity.finish()
                        true
                    }
                    R.id.nav_bookmark -> {
                        switchScreen(currentActivity, BookmarkTipListActivity::class.java)
                        currentActivity.finish()
                        true
                    }
                    else -> false
                }
            }
        }
    }
}
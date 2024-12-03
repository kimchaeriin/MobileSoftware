package com.practice.android.pocketmate.Settings

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.practice.android.pocketmate.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {
    lateinit var binding : ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportFragmentManager.beginTransaction()
            .replace(android.R.id.content, SettingsFragment())
            .commit()
    }
}
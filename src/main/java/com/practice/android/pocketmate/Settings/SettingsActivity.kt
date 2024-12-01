package com.practice.android.pocketmate.Settings

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.practice.android.pocketmate.R
import com.practice.android.pocketmate.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {
    lateinit var binding : ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbarSettings)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_settings, SettingsFragment())
            .commit()

    }

    override fun onSupportNavigateUp(): Boolean {
        if(supportFragmentManager.backStackEntryCount > 0){
            supportFragmentManager.popBackStack()

            val toolbar = findViewById<Toolbar>(R.id.toolbar_settings)
            toolbar?.title = "설정"
            return true
        }
        return super.onSupportNavigateUp()
    }
}
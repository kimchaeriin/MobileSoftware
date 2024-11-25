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

    /* 이메일 변경
    val user = Firebase.auth.currentUser

    user!!.updateEmail("user@example.com")
    .addOnCompleteListener { task ->
        if (task.isSuccessful) {
            Log.d(TAG, "User email address updated.")
        }
    }

     비밀번호 변경
    val user = Firebase.auth.currentUser
    val newPassword = "SOME-SECURE-PASSWORD"

    user!!.updatePassword(newPassword)
    .addOnCompleteListener { task ->
        if (task.isSuccessful) {
            Log.d(TAG, "User password updated.")
        }
    }

    회원 탈퇴
    val user = Firebase.auth.currentUser!!

    user.delete()
    .addOnCompleteListener { task ->
        if (task.isSuccessful) {
            Log.d(TAG, "User account deleted.")
        }
    }
    */
}

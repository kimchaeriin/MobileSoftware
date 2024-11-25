package com.practice.android.pocketmate.Settings

import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.preference.EditTextPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.practice.android.pocketmate.ProfileActivity
import com.practice.android.pocketmate.R
import com.practice.android.pocketmate.util.FBAuth
import com.practice.android.pocketmate.util.FBRef


class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings,rootKey)

        findPreference<Preference>("alarm")?.setOnPreferenceClickListener {
            val transaction = parentFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_settings,SettingsAlarmFragment())
            transaction.addToBackStack(null)
            transaction.commit()
            true
        }

        findPreference<Preference>("notice")?.setOnPreferenceClickListener {
            val transaction = parentFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_settings,SettingsNoticeFragment())
            transaction.addToBackStack(null)
            transaction.commit()
            true
        }

        val nicknamePreference = findPreference<Preference>("show_nickname")
        nicknamePreference?.summary = "temp" //파이어베이스에서 닉네임 가져오기

    }
}
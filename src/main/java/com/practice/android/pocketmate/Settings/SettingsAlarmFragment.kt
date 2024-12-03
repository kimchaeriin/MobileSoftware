package com.practice.android.pocketmate.Settings

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.practice.android.pocketmate.R

class SettingsAlarmFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings_alarm,rootKey)

        val toolbar = requireActivity().findViewById<Toolbar>(R.id.toolbar_settings)
        toolbar?.title = "알림"
    }

}
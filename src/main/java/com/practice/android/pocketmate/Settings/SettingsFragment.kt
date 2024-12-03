package com.practice.android.pocketmate.Settings

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceFragmentCompat
import com.practice.android.pocketmate.R

/**
 * A simple [Fragment] subclass.
 * Use the [SettingsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings, rootKey)
    }

}
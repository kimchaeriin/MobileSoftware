package com.practice.android.pocketmate.Settings

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.EditTextPreference
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import com.practice.android.pocketmate.R
import com.practice.android.pocketmate.util.FBAuth
import com.practice.android.pocketmate.util.FBRef


class SettingsFragment : PreferenceFragmentCompat(){

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings,rootKey)

        findPreference<Preference>("setting_account")?.setOnPreferenceClickListener {

            val transaction = parentFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_settings, ReAuthenFragment())
            transaction.addToBackStack(null)
            transaction.commit()
            true
        }

        val changeEmail = findPreference<EditTextPreference>("feedback")
        changeEmail?.onPreferenceChangeListener =
            Preference.OnPreferenceChangeListener{ preference, newValue ->
                val enquiry = newValue as String
                addEnquiry(enquiry)
                true
            }

        findPreference<ListPreference>("theme")?.setOnPreferenceChangeListener {preference, value ->
            val selected = value.toString()
            val prefs = PreferenceManager.getDefaultSharedPreferences(requireContext())

            if(selected.equals("value2")) {
                changeTheme(AppCompatDelegate.MODE_NIGHT_YES)
                saveThemePreference(requireContext(),AppCompatDelegate.MODE_NIGHT_YES)
            }
            else {
                changeTheme(AppCompatDelegate.MODE_NIGHT_NO)
                saveThemePreference(requireContext(),AppCompatDelegate.MODE_NIGHT_NO)
            }
            true
        }
    }

    private fun addEnquiry(enquiry:String){
        val uid = FBAuth.getUid()
        val enquiryRef = FBRef.enquiryRef.child(uid)
        val inquiryId = enquiryRef.push().key

        if (inquiryId != null) {
            enquiryRef.child(inquiryId).setValue(enquiry)
                .addOnSuccessListener {
                    Toast.makeText(requireContext(),"전송했습니다",Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Toast.makeText(requireContext(),"실패했습니다",Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun changeTheme(mode:Int){
        AppCompatDelegate.setDefaultNightMode(mode)
    }

    private fun saveThemePreference(context: Context, mode:Int){
        val pref = context.getSharedPreferences("theme_prefs", Context.MODE_PRIVATE)
        pref.edit().putInt("theme_mode",mode).apply()
    }

}

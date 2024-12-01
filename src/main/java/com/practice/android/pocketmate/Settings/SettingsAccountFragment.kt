package com.practice.android.pocketmate.Settings

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.preference.EditTextPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.auth.ktx.auth
import com.practice.android.pocketmate.Auth.IntroActivity
import com.practice.android.pocketmate.Auth.LoginActivity
import com.practice.android.pocketmate.R
import com.practice.android.pocketmate.util.ScreenUtils

class SettingsAccountFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings_account,rootKey)

        val changeEmail = findPreference<EditTextPreference>("change_email")
        changeEmail?.onPreferenceChangeListener =
            Preference.OnPreferenceChangeListener{ preference, newValue ->
                val email = newValue as  String
                if(email.isBlank()){
                    Toast.makeText(context,"값을 입력하세요",Toast.LENGTH_SHORT).show()
                    return@OnPreferenceChangeListener false
                }

                updateEmail(email)
                true
            }

        val changePw = findPreference<EditTextPreference>("change_pw")
        changePw?.onPreferenceChangeListener =
            Preference.OnPreferenceChangeListener{ preference, newValue ->
                val pw = newValue as  String

                if(pw.isBlank()){
                    Toast.makeText(context,"값을 입력하세요",Toast.LENGTH_SHORT).show()
                    return@OnPreferenceChangeListener false
                }
                updatePW(pw)
                true
            }

        findPreference<Preference>("log_out")?.setOnPreferenceClickListener {
            com.google.firebase.ktx.Firebase.auth.signOut()
            val intent = Intent(requireContext(),IntroActivity::class.java)
            startActivity(intent)
            Toast.makeText(requireContext(),"로그아웃",Toast.LENGTH_SHORT).show()
            true
        }

        findPreference<Preference>("account_del")?.setOnPreferenceClickListener {
            val user = Firebase.auth.currentUser!!

            user.delete()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(requireContext(),"계정을 탈퇴했습니다",Toast.LENGTH_SHORT).show()
                        val intent = Intent(requireContext(),IntroActivity::class.java)
                        startActivity(intent)
                    }
                }

            true
        }
    }

    private fun updateEmail(email:String){
        val user = FirebaseAuth.getInstance().currentUser

        user!!.verifyBeforeUpdateEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(requireContext(),"변경을 성공했습니다",Toast.LENGTH_SHORT).show()
                }
                else{
                    task.exception?.let{
                        Toast.makeText(requireContext(),"변경을 실패했습니다",Toast.LENGTH_SHORT).show()
                    }
                }
            }
    }

    private fun updatePW(pw:String){
        val user = Firebase.auth.currentUser

        user!!.updatePassword(pw)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(requireContext(),"변경을 성공했습니다",Toast.LENGTH_SHORT).show()
                }
            }
    }
}
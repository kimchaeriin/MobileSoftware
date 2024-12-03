package com.practice.android.pocketmate.Settings

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.google.firebase.Firebase
import com.google.firebase.auth.EmailAuthCredential
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.auth
import com.practice.android.pocketmate.R
import com.practice.android.pocketmate.databinding.FragmentReAuthenBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ReAuthenFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ReAuthenFragment : Fragment() {
    lateinit var binding: FragmentReAuthenBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val toolbar = requireActivity().findViewById<Toolbar>(R.id.toolbar_settings)
        toolbar?.title = "계정 재인증"

        binding.BtnReAuthen.setOnClickListener {
            val email = binding.emailReAuthen.text.toString()
            val pw = binding.passwordReAuthen.text.toString()

            val user = Firebase.auth.currentUser!!
            val credential = EmailAuthProvider
                .getCredential(email,pw)

            user.reauthenticate(credential)
                .addOnSuccessListener {
                    Toast.makeText(requireContext(),"인증에 성공했습니다", Toast.LENGTH_SHORT).show()

                    val transaction = parentFragmentManager.beginTransaction()
                    transaction.replace(R.id.fragment_settings,SettingsAccountFragment())
                    transaction.commit()
                    toolbar?.title = "계정 관리"
                }
                .addOnFailureListener {
                    Toast.makeText(requireContext(),"인증에 실패했습니다",Toast.LENGTH_SHORT).show()
                }
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentReAuthenBinding.inflate(layoutInflater)
        return binding.root
    }

}
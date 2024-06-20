package com.sampahplus.ui.profile

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.userProfileChangeRequest
import com.google.firebase.ktx.Firebase
import com.sampahplus.R
import com.sampahplus.databinding.FragmentProfileBinding
import com.sampahplus.ui.main.MainActivity

class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private lateinit var emailTextView: TextView
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {

        super.onViewCreated(view, savedInstanceState)
        emailTextView = view.findViewById(R.id.email_user)

        // Get the email address of the currently logged-in user
        val currentUser = FirebaseAuth.getInstance().currentUser
        setClickListener()
                binding.nameEditText.setText( currentUser?.displayName)
                binding.emailEditText.setText(currentUser?.email)
                binding.emailUser.text=currentUser?.displayName

        binding. btnLogout.setOnClickListener(){
            Firebase.auth.signOut()
            startActivity(
                Intent(requireContext(), MainActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                }
            )
        }
    }

    private fun setClickListener() {
        binding.btnEditUsername.setOnClickListener {
            binding.nameInputLayout.isEnabled = true
            binding.emailInputLayout.isVisible = false
            binding.btnSaveUsername.isVisible = true

            binding.btnEditUsername.isVisible = false

        }
        binding.btnSaveUsername.setOnClickListener {
            val username = binding.nameEditText.text.toString()
            updateProfile(username)
        }
    }

    private fun updateProfile(username: String) {
        val user = FirebaseAuth.getInstance().currentUser
        val profileUpdates = userProfileChangeRequest {
            displayName = username
        }
        user?.updateProfile(profileUpdates)
            ?.addOnCompleteListener { updateProfileTask ->
                if (updateProfileTask.isSuccessful) {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.text_success_notif),
                        Toast.LENGTH_SHORT,
                    ).show()
                    Log.d("RegisterActivity", "User profile updated.")
                    startActivity(Intent(requireContext(), MainActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    })
                } else {
                    Log.w("RegisterActivity", "Failed to update profile.", updateProfileTask.exception)
                }
            }
    }
}
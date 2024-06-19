package com.sampahplus.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.sampahplus.R

class ProfileFragment : Fragment() {

    private lateinit var emailTextView: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        emailTextView = view.findViewById(R.id.email_user)

        // Get the email address of the currently logged-in user
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser!= null) {
            emailTextView.text = currentUser.email
        } else {
            // User is not logged in, so show a default message
            emailTextView.text = getString(R.string.email_user)
        }

//        editProfileTextView.setOnClickListener {
//            val intent = Intent(activity, EditProfileActivity::class.java)
//            intent.putExtra("EMAIL", emailTextView.text.toString())
//            startActivity(intent)
//        }

        return view
    }
}
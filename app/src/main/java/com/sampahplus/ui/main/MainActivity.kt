package com.sampahplus.ui.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.sampahplus.R
import com.sampahplus.databinding.ActivityMainBinding
import com.sampahplus.ui.welcome.WelcomeActivity

class MainActivity : AppCompatActivity() {
    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // Initialize Firebase
        FirebaseApp.initializeApp(this)

        // Initialize FirebaseAuth
        auth = FirebaseAuth.getInstance()

        // Check if the user is logged in
        checkUserLoggedIn()

        // Set up bottom navigation
        setBottomNav()
    }

    private fun setBottomNav() {
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        binding.navView.setupWithNavController(navController)

        // Ensure the correct fragment is shown when a bottom navigation item is selected
        binding.navView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menu_tab_home -> {
                    navController.navigate(R.id.menu_tab_home)
                    true
                }

                R.id.menu_tab_profile -> {
                    navController.navigate(R.id.menu_tab_profile)
                    true
                }

                R.id.menu_tab_predict -> {
                    navController.navigate(R.id.menu_tab_predict)
                    true
                }

                else -> false
            }
        }

        // Add a destination change listener to handle the back navigation logic
        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.menu_tab_home) {
                this.onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
                    override fun handleOnBackPressed() {
                        finish() // Exit the app
                    }
                })
            } else {
                this.onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
                    override fun handleOnBackPressed() {
                        navController.navigate(R.id.menu_tab_home)
                    }
                })
            }
        }
    }

    override fun onBackPressed() {
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // If the current fragment is home, exit the app
        if (navController.currentDestination?.id == R.id.menu_tab_home) {
            finish()
        } else {
            // Navigate back to the home fragment
            navController.navigate(R.id.menu_tab_home)
        }
    }

    private fun checkUserLoggedIn() {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            // User is logged in
            val email = currentUser.email
            Log.d("MainActivity", "User is logged in: $email")
            // Proceed to the next screen
        } else {
            // User is not logged in
            Log.d("MainActivity", "User is not logged in")
            // Navigate to the login screen or show the login UI
            val intent = Intent(this, WelcomeActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}

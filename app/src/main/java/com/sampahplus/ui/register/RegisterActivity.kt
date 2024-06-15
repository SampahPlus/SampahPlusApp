package com.sampahplus.ui.register

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.sampahplus.ui.login.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import com.sampahplus.R

class RegisterActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        val namaEditText = findViewById<EditText>(R.id.edit_text_nama)
        val emailEditText = findViewById<EditText>(R.id.edit_text_email)
        val passwordEditText = findViewById<EditText>(R.id.edit_text_password)
        val registerButton = findViewById<Button>(R.id.button_daftar)

        registerButton.setOnClickListener {
            val nama = namaEditText.text.toString().trim()
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()
            if (email.isNotEmpty() && password.isNotEmpty() && nama.isNotEmpty()) {
                registerUser(nama, email, password)
            } else {
                Toast.makeText(this, "Nama, Email, dan Password tidak boleh kosong", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun registerUser(nama: String, email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Registration successful, navigate to main activity or login activity
                    Log.d("RegisterActivity", "createUserWithEmail:success")
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    // If registration fails, display a message to the user.
                    Log.w("RegisterActivity", "createUserWithEmail:failure", task.exception)
                    Toast.makeText(this, "Authentication failed.", Toast.LENGTH_SHORT).show()
                }
            }
    }
}

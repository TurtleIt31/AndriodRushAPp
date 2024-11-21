package com.example.rushapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.ComponentActivity

class NewUserActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register_account) // Load the register account layout

        val dataHandler = DataHandler(this) // Initialize DataHandler


        // Register button logic
        val registerButton = findViewById<Button>(R.id.registerTextBtn2)
        registerButton.setOnClickListener {
            handleRegistration(dataHandler)
        }

        val backButton = findViewById<Button>(R.id.backButton)
        backButton.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
            finish() // Optional: Finish the current activity
        }
    }

    private fun navigateToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish() // Prevents returning to this screen
    }

    private fun handleRegistration(dataHandler: DataHandler) {
        val usernameField = findViewById<EditText>(R.id.emailEdt)
        val passwordField = findViewById<EditText>(R.id.passwordEdt)
        val nameField = findViewById<EditText>(R.id.nameEdt)
        val phoneField = findViewById<EditText>(R.id.phoneEdt)
        val userTypeField = findViewById<EditText>(R.id.userTypeEdt)

        val email = usernameField.text.toString().trim()
        val password = passwordField.text.toString().trim()
        val name = nameField.text.toString().trim()
        val phone = phoneField.text.toString().trim()
        val userType = userTypeField.text.toString().trim()

        // Validate user input
        if (!validateInput(email, password, name, phone, userType)) return

        try {
            // Check if email already exists
            val emailExists = dataHandler.doesUserExist(email)
            if (emailExists) {
                showToast("A user with this email already exists. Please use another email.")
                return
            }

            // Attempt to register the new user
            val isInserted = dataHandler.insertNewUser(name, email, password, phone, userType)
            if (isInserted) {
                showToast("Registration successful!")
                navigateToLogin() // Navigate to login on success
            } else {
                showToast("Registration failed. Please try again.")
            }
        } catch (e: Exception) {
            Log.e("NewUserActivity", "Error during registration: ${e.message}")
            showToast("An unexpected error occurred. Please try again later.")
        }
    }

    private fun validateInput(email: String, password: String, name: String, phone: String, userType: String): Boolean {
        return when {
            email.isEmpty() || password.isEmpty() || name.isEmpty() || phone.isEmpty() || userType.isEmpty() -> {
                showToast("All fields are required.")
                false
            }
            !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                showToast("Please enter a valid email address.")
                false
            }
            password.length < 6 -> {
                showToast("Password must be at least 6 characters long.")
                false
            }
            else -> true
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}

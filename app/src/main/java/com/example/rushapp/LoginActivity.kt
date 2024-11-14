package com.example.rushapp

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.appcompat.widget.AppCompatButton

class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login) // Load the login layout

        // Reference the login fields and the check button from the login layout
        val loginButton = findViewById<AppCompatButton>(R.id.loginBtn)
        val usernameField = findViewById<EditText>(R.id.emailEdt)
        val passwordField = findViewById<EditText>(R.id.passwordEdt)

        // Set up the login check logic
        loginButton.setOnClickListener {
            val email = usernameField.text.toString() // Assuming you're using email for login
            val password = passwordField.text.toString()

            // Replace with your actual database helper instance
            val dbHelper = DatabaseHelper(this)
            val db = dbHelper.readableDatabase

            val cursor = db.query(
                "Users", // Ensure this matches your actual table name
                arrayOf("userType"), // Replace with the actual column name for user type if necessary
                "email = ? AND passwordEntry = ?", // Adjust column names as per your schema
                arrayOf(email, password),
                null,
                null,
                null
            )

            if (cursor.moveToFirst()) {
                // User found
                val userType = cursor.getString(cursor.getColumnIndexOrThrow("userType")) // Ensure column name is correct
                Toast.makeText(this, "Login Successful. User type: $userType", Toast.LENGTH_SHORT).show()

                // Navigate to ProfileActivity
                val intent = Intent(this, ProfileActivity::class.java)
                // Optionally, you can pass data (e.g., userType) to the ProfileActivity
                intent.putExtra("userType", userType)
                startActivity(intent)

                // Finish current activity if you want to prevent the user from returning to the login screen
                finish()
            } else {
                // Invalid credentials
                Toast.makeText(this, "Invalid Credentials", Toast.LENGTH_SHORT).show()
            }

            cursor.close()
            db.close()
        }

    }
}

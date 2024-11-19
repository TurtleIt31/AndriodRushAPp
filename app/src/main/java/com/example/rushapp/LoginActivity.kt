package com.example.rushapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.appcompat.widget.AppCompatButton

class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login) // Load the login layout

        // Reference the login fields and the check button from the login layout
        val registerButton = findViewById<Button>(R.id.registerTextBtn)
        val loginButton = findViewById<AppCompatButton>(R.id.loginBtn)
        val usernameField = findViewById<EditText>(R.id.emailEdt)
        val passwordField = findViewById<EditText>(R.id.passwordEdt)

        // Set up the login check logic
        loginButton.setOnClickListener {
            val email = usernameField.text.toString() // Assuming you're using email for login
            val password = passwordField.text.toString()

            val dbHelper = DatabaseHelper(this)
            val db = dbHelper.readableDatabase

            val cursor = db.query(
                "Users", // Ensure this matches your actual table name
                arrayOf("email", "userType"), // Retrieve email and userType
                "email = ? AND passwordEntry = ?", // Adjust column names as per your schema
                arrayOf(email, password),
                null,
                null,
                null
            )

            if (cursor.moveToFirst()) {
                // User found
                val userEmail = cursor.getString(cursor.getColumnIndexOrThrow("email")) // Retrieve email
                val userType = cursor.getString(cursor.getColumnIndexOrThrow("userType")) // Retrieve userType
                // Continue with navigation
                Toast.makeText(this, "Login Successful. User type: $userType", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, ProfileActivity::class.java)
                intent.putExtra("userType", userType) // Optionally, pass userType if needed
                intent.putExtra("email", userEmail) // Optionally, pass email if needed
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
        //register new user
        registerButton.setOnClickListener {
            val intent = Intent(this, NewUserActivity::class.java)
            startActivity(intent)
        }
    }
}

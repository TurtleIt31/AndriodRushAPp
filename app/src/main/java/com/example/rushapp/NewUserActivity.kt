package com.example.rushapp

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.example.rushapp.ui.theme.RushAppTheme

class NewUserActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register_account) // Load the main layout
        val dataHandler = DataHandler(this)
        dataHandler.populateSampleData()

        // Start the Register activity when button is clicked
        val button = findViewById<Button>(R.id.registerTextBtn)
        button?.setOnClickListener {
            val usernameField = findViewById<EditText>(R.id.emailEdt)
            val passwordField = findViewById<EditText>(R.id.passwordEdt)
            val nameField = findViewById<EditText>(R.id.nameEdt)
            val phoneField = findViewById<EditText>(R.id.phoneEdt)
            val userTypeField = findViewById<EditText>(R.id.userTypeEdt) // This can be a dropdown select later

            val email = usernameField.text.toString().trim()
            val password = passwordField.text.toString().trim()
            val name = nameField.text.toString().trim()
            val phone = phoneField.text.toString().trim()
            val userType = userTypeField.text.toString().trim()

            // Basic input validation
            if (email.isEmpty() || password.isEmpty() || name.isEmpty() || phone.isEmpty() || userType.isEmpty()) {
                Toast.makeText(this, "All fields are required.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, "Please enter a valid email address.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password.length < 6) {
                Toast.makeText(this, "Password must be at least 6 characters long.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val dbHelper = DatabaseHelper(this)
            val db = dbHelper.writableDatabase // Use writable database for insertion

            try {
                // Check if the user already exists by querying the email
                val cursor = db.query(
                    "Users", // Ensure this matches your actual table name
                    arrayOf("userType"),
                    "email = ?",
                    arrayOf(email),
                    null,
                    null,
                    null
                )

                if (cursor.moveToFirst()) {
                    // User found, indicate a duplicate user
                    Toast.makeText(this, "Duplicate user found. Please reset your password.", Toast.LENGTH_SHORT).show()
                } else {
                    // User does not exist, insert a new user
                    val values = ContentValues().apply {
                        put("name", name)
                        put("email", email)
                        put("passwordEntry", password)
                        put("phone", phone)
                        put("userType", userType)
                        // You can add "customerId" or "mechanicId" here if applicable
                        put("customerId", generateCustomerId()) // Example function to generate a customerId
                        putNull("mechanicId")
                    }

                    val newRowId = db.insert("Users", null, values)
                    if (newRowId != -1L) {
                        // Successful insertion
                        Toast.makeText(this, "Registration successful!", Toast.LENGTH_SHORT).show()

                        // Navigate to LoginActivity
                        val intent = Intent(this, LoginActivity::class.java)
                        startActivity(intent)
                        finish() // Prevents the user from returning to this screen
                    } else {
                        // Insertion failed
                        Toast.makeText(this, "Registration failed. Please try again.", Toast.LENGTH_SHORT).show()
                    }
                }

                cursor.close()
            } catch (e: Exception) {
                Toast.makeText(this, "An error occurred: ${e.message}", Toast.LENGTH_LONG).show()
            } finally {
                db.close()
            }
        }
    }

    // Example function to generate a customer ID. Replace this with your actual logic to generate or fetch a customerId
    private fun generateCustomerId(): Long {
        // Replace this with your logic to generate or fetch a valid customer ID from the Customers table
        return System.currentTimeMillis() // Example implementation, not recommended for production use
    }
}

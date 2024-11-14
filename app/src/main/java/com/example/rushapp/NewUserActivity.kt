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
            val userTypeField = findViewById<EditText>(R.id.userTypeEdt)//this must be a drop down select

            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            val email = usernameField.text.toString() // Assuming you're using email for login
            val password = passwordField.text.toString()
            val name = nameField.text.toString()
            val phone = phoneField.text.toString()
            val userType = userTypeField.text.toString()

            val dbHelper = DatabaseHelper(this)
            val db = dbHelper.readableDatabase
            val cursor = db.query(
                "Users", // Ensure this matches your actual table name
                arrayOf("userType"), // Replace with the actual column name for user type if necessary
                "email = ?", // Adjust column names as per your schema
                arrayOf(email),
                null,
                null,
                null
            )

            if (cursor.moveToFirst()) {
                // User found
                val userType = cursor.getString(cursor.getColumnIndexOrThrow("userType")) // Ensure column name is correct
                Toast.makeText(this, "Duplicate user found. Please reset your password.", Toast.LENGTH_SHORT).show()




            } else {
                // User does not exist, insert a new user
                val values = ContentValues().apply {
                    put("name", name)
                    put("email", email)
                    put("password", password)
                    put("phone", phone)
                    put("userType", userType)
                    // Add other values as needed based on your schema
                }

                val newRowId = db.insert("Users", null, values)
                if (newRowId != -1L) {
                    // Successful insertion
                    Toast.makeText(this, "Registration successful!", Toast.LENGTH_SHORT).show()

                    // Navigate to LoginActivity
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                } else {
                    // Insertion failed
                    Toast.makeText(this, "Registration failed. Please try again.", Toast.LENGTH_SHORT).show()
                }



            }


        }


    }
}
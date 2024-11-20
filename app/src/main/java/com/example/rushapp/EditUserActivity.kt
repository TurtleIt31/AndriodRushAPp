package com.example.rushapp

import android.content.ContentValues
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.example.rushapp.data.model.User

class EditUserActivity : ComponentActivity() {

    private var originalUser: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edituser)

        // Find the views
        val nameEditText = findViewById<EditText>(R.id.nameEditText)
        val emailEditText = findViewById<EditText>(R.id.emailEditText)
        val phoneEditText = findViewById<EditText>(R.id.phoneEditText)
        val userTypeEditText = findViewById<EditText>(R.id.userTypeEditText)
        val passwordEditText = findViewById<EditText>(R.id.passwordEditText)
        val saveButton = findViewById<Button>(R.id.saveButton)

        // Get the email passed via Intent
        val email = intent.getStringExtra("email")

        if (email != null) {
            // Fetch the logged-in user's data from the database
            originalUser = fetchLoggedInUserFromDatabase(email)

            if (originalUser != null) {
                // Populate the EditText fields with user data
                nameEditText.setText(originalUser!!.name)
                emailEditText.setText(originalUser!!.email)
                phoneEditText.setText(originalUser!!.phone)
                userTypeEditText.setText(originalUser!!.userType)
                passwordEditText.setText(originalUser!!.password) // Populate password for editing
            } else {
                Toast.makeText(this, "No user found with the provided email.", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "No email passed to the activity.", Toast.LENGTH_SHORT).show()
        }

        // Save button functionality
        saveButton.setOnClickListener {
            val updatedName = nameEditText.text.toString()
            val updatedEmail = emailEditText.text.toString()
            val updatedPhone = phoneEditText.text.toString()
            val updatedUserType = userTypeEditText.text.toString()
            val updatedPassword = passwordEditText.text.toString()

            // Update only changed fields
            val changes = mutableMapOf<String, String>()
            if (originalUser != null) {
                if (updatedName != originalUser!!.name) changes["name"] = updatedName
                if (updatedEmail != originalUser!!.email) changes["email"] = updatedEmail
                if (updatedPhone != originalUser!!.phone) changes["phone"] = updatedPhone
                if (updatedUserType != originalUser!!.userType) changes["userType"] = updatedUserType

                // Update password only if it has been changed
                if (updatedPassword != originalUser!!.password) changes["passwordEntry"] = updatedPassword
            }

            if (changes.isNotEmpty()) {
                val success = updateUserInDatabase(originalUser!!.email, changes)
                if (success) {
                    Toast.makeText(this, "User details updated successfully!", Toast.LENGTH_SHORT).show()
                    finish() // Close the activity
                } else {
                    Toast.makeText(this, "Failed to update user details.", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "No changes made.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun fetchLoggedInUserFromDatabase(email: String): User? {
        val dbHelper = DatabaseHelper(this)
        val db = dbHelper.readableDatabase
        var loggedInUser: User? = null

        val cursor = db.query(
            "Users", // Table name
            arrayOf("name", "email", "phone", "userType", "passwordEntry"), // Include password column
            "email = ?", // WHERE clause
            arrayOf(email), // WHERE args
            null, // No group by
            null, // No having
            null  // No order by
        )

        if (cursor.moveToFirst()) {
            val name = cursor.getString(cursor.getColumnIndexOrThrow("name"))
            val phone = cursor.getString(cursor.getColumnIndexOrThrow("phone"))
            val userType = cursor.getString(cursor.getColumnIndexOrThrow("userType"))
            val password = cursor.getString(cursor.getColumnIndexOrThrow("passwordEntry")) // Retrieve password

            loggedInUser = User(
                userId = null,
                mechanicId = null,
                customerId = null,
                name = name,
                userType = userType,
                email = email,
                phone = phone,
                password = password // Pass the password
            )
        }

        cursor.close()
        db.close()
        return loggedInUser
    }

    private fun updateUserInDatabase(
        email: String,
        changes: Map<String, String>
    ): Boolean {
        val dbHelper = DatabaseHelper(this)
        val db = dbHelper.writableDatabase

        val values = ContentValues()
        for ((key, value) in changes) {
            values.put(key, value)
        }

        val rowsUpdated = db.update(
            "Users", // Table name
            values,
            "email = ?", // WHERE clause
            arrayOf(email) // WHERE args
        )

        db.close()
        return rowsUpdated > 0
    }
}

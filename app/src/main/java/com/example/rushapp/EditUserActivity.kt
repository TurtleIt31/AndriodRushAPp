package com.example.rushapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.rushapp.data.model.User
import com.example.rushapp.ui.theme.RushAppTheme

class EditUserActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edituser)
        val users = fetchUsersFromDatabase()
        // You can log the fetched users or use them as needed
        println("Fetched users: $users")

    }
    private fun fetchUsersFromDatabase(): List<User> {
        val dbHelper = DatabaseHelper(this)
        val db = dbHelper.readableDatabase
        val userList = mutableListOf<User>()

        val cursor = db.query(
            "Users", // Table name
            null, // Select all columns
            null, // No WHERE clause
            null, // No selection args
            null, // No group by
            null, // No having
            null  // No order by
        )

        if (cursor.count == 0) {
            // Log if no records found
            println("No users found in the database.")
        }

        while (cursor.moveToNext()) {
            val name = cursor.getString(cursor.getColumnIndexOrThrow("name"))
            val email = cursor.getString(cursor.getColumnIndexOrThrow("email"))
            val phone = cursor.getString(cursor.getColumnIndexOrThrow("phone"))
            val userType = cursor.getString(cursor.getColumnIndexOrThrow("userType"))
            val password = cursor.getString(cursor.getColumnIndexOrThrow("passwordEntry"))

            userList.add(
                User(
                    userId = null, // Adjust these fields as necessary
                    mechanicId = null,
                    customerId = null,
                    name = name,
                    userType = userType,
                    email = email,
                    phone = phone,
                    password = password
                )
            )
        }

        cursor.close()
        db.close()
        return userList
    }



}


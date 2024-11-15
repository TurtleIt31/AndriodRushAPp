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
        enableEdgeToEdge()

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

        while (cursor.moveToNext()) {
            val name = cursor.getString(cursor.getColumnIndexOrThrow("name"))
            val email = cursor.getString(cursor.getColumnIndexOrThrow("email"))
            val phone = cursor.getString(cursor.getColumnIndexOrThrow("phone"))
            val userType = cursor.getString(cursor.getColumnIndexOrThrow("userType"))
            val password = cursor.getString(cursor.getColumnIndexOrThrow("passwordEntry"))

            // Create a User object with only the relevant data
            userList.add(
                User(
                    userId = null, // Unused/immutable field
                    mechanicId = null, // Unused/immutable field
                    customerId = null, // Unused/immutable field
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


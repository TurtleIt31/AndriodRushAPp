package com.example.rushapp

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MechanicsActivity : ComponentActivity() {
    private lateinit var dataHandler: DataHandler
    private lateinit var addMechanicButton: Button
    private val userType by lazy { intent.getStringExtra("userType") ?: "" } // Retrieve userType from Intent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mechanics)

        dataHandler = DataHandler(this) // Initialize DataHandler

        // Fetch mechanics from the database
        val db = dataHandler.dbHelper.readableDatabase
        val mechanics = dataHandler.getAllMechanics(db)
        db.close()

        // Set up RecyclerView
        val recyclerView: RecyclerView = findViewById(R.id.mechanicsRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = MechanicsAdapter(mechanics)

        // Add Mechanic Button
        addMechanicButton = findViewById(R.id.addMechanicButton)
        if (userType != "Admin") {
            addMechanicButton.visibility = View.GONE // Hide button for non-admin users
        } else {
            addMechanicButton.setOnClickListener { showAddMechanicDialog() }
        }

        val email = intent.getStringExtra("email") ?: ""
        val addServiceButton = findViewById<Button>(R.id.AddServiceButton)

        addServiceButton.setOnClickListener {
            val intent = Intent(this, ServicesActivity::class.java)
            intent.putExtra("email", email) // Pass the email to ServiceActivity
            startActivity(intent)
        }

    }

    private fun showAddMechanicDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_view, null)
        val nameField = dialogView.findViewById<EditText>(R.id.mechanicNameField)
        val emailField = dialogView.findViewById<EditText>(R.id.mechanicEmailField)
        val phoneField = dialogView.findViewById<EditText>(R.id.mechanicPhoneField)
        val passwordField = dialogView.findViewById<EditText>(R.id.mechanicPasswordField)

        AlertDialog.Builder(this)
            .setTitle("Add New Mechanic")
            .setView(dialogView)
            .setPositiveButton("Add") { _, _ ->
                val name = nameField.text.toString()
                val email = emailField.text.toString()
                val phone = phoneField.text.toString()
                val password = passwordField.text.toString()

                if (name.isBlank() || email.isBlank() || phone.isBlank() || password.isBlank()) {
                    Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }

                val db = dataHandler.dbHelper.writableDatabase
                val success = dataHandler.insertNewMechanic(name, email, password, phone, null) // Add mechanic
                db.close()

                if (success) {
                    Toast.makeText(this, "Mechanic added successfully!", Toast.LENGTH_SHORT).show()
                    recreate() // Refresh the activity
                } else {
                    Toast.makeText(this, "Failed to add mechanic", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}

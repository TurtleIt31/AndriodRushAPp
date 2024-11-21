package com.example.rushapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import androidx.activity.ComponentActivity

class VehicleActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vehiclelist)

        /*
        val dbHelper = DatabaseHelper(this)
        val vehicleListView = findViewById<ListView>(R.id.vehicleListView)
        Fetch vehicle data and display it in the ListView
        val vehicleList = dbHelper.getAllVehicles()
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, vehicleList)
        vehicleListView.adapter = adapter
        */

        val addNewVehicleButton = findViewById<Button>(R.id.addNewVehicleButton)
        addNewVehicleButton.setOnClickListener {
            // Handle adding a new vehicle (e.g., open a dialog or another activity)
            Toast.makeText(this, "Add New Vehicle button clicked", Toast.LENGTH_SHORT).show()
        }

        val removeVehicleButton = findViewById<Button>(R.id.removeVehicleButton)
        removeVehicleButton.setOnClickListener {
            // Handle removing a vehicle (e.g., show a confirmation dialog)
            Toast.makeText(this, "Remove Vehicle button clicked", Toast.LENGTH_SHORT).show()
        }

        val backButton = findViewById<Button>(R.id.backButton)
        backButton.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
            finish() // Optional: Finish the current activity
        }
    }
}
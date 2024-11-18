package com.example.rushapp

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.ComponentActivity
import java.util.Calendar

class BookingsActivity : ComponentActivity() {

    private var selectedDate: String? = null // Variable to store the selected date

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_appointments) // Set the content view to the XML layout

        // Initialize the database helper
        val db = DatabaseHelper(this)

        // Find the date button by its ID and set an onClickListener
        findViewById<Button>(R.id.dateButton).setOnClickListener { dateButton ->
            showDatePicker(dateButton as Button) // Show date picker when button is clicked
        }

        // Example button to create booking
        findViewById<Button>(R.id.addAppointmentButton).setOnClickListener {
            createBookingWithForeignKeys(db)
        }
    }

    // Function to display a DatePickerDialog
    private fun showDatePicker(button: Button) {
        val calendar = Calendar.getInstance()
        DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                selectedDate = "$year-${month + 1}-$dayOfMonth"
                button.text = "Selected Date: $selectedDate" // Update button text with selected date
                Toast.makeText(this, "Selected Date: $selectedDate", Toast.LENGTH_SHORT).show() // Optional feedback
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    // Function to create a booking using foreign keys and selected date
    private fun createBookingWithForeignKeys(db: DatabaseHelper) {
        // Example fetching of foreign key values (replace with your actual logic)
        val serviceID: Long? = fetchForeignKeyID(db, "services", "criteria")?.toLong()
        val mechanicID: Long? = fetchForeignKeyID(db, "mechanics", "criteria")?.toLong()
        val vehicleID: Long? = fetchForeignKeyID(db, "vehicles", "criteria")?.toLong()
        val customerID: Long = getSignedInCustomerID().toLong() // Assuming it returns an Int

        // Check for valid foreign key IDs and selected date
        if (serviceID == null || mechanicID == null || vehicleID == null || selectedDate == null) {
            Toast.makeText(this, "Error: Missing required data or date.", Toast.LENGTH_SHORT).show()
            return
        }

        // Insert the booking with foreign keys
        val result = db.insertBooking(serviceID, mechanicID, vehicleID, customerID, selectedDate!!)
        Toast.makeText(this, result, Toast.LENGTH_SHORT).show()
    }

    // Function to fetch foreign key ID from the database (simplified example)
    private fun fetchForeignKeyID(db: DatabaseHelper, tableName: String, criteria: String): Int? {
        val dbReadable = db.readableDatabase
        val cursor = dbReadable.rawQuery("SELECT id FROM $tableName WHERE criteriaColumn = ?", arrayOf(criteria))
        return if (cursor.moveToFirst()) {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
            cursor.close()
            id
        } else {
            cursor.close()
            null
        }
    }

    // Dummy function to retrieve the signed-in customer ID (replace with your actual logic)
    private fun getSignedInCustomerID(): Int {
        val sharedPreferences = getSharedPreferences("app_prefs", MODE_PRIVATE)
        return sharedPreferences.getInt("customerID", -1) // Replace "customerID" with your actual key
    }
}

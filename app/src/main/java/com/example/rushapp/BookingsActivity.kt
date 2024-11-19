package com.example.rushapp

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
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
        findViewById<Button>(R.id.dateButton)?.setOnClickListener { dateButton ->
            showDatePicker(dateButton as Button) // Show date picker when button is clicked
        } ?: Log.e("BookingsActivity", "Error: dateButton not found in layout")

        // Example button to create booking
        findViewById<Button>(R.id.addAppointmentButton)?.setOnClickListener {
            createBookingWithForeignKeys(db)
        } ?: Log.e("BookingsActivity", "Error: addAppointmentButton not found in layout")
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
        try {
            // Example fetching of foreign key values (replace with your actual logic)
            val serviceID: Long? = fetchForeignKeyID(db, "Services", "desired_criteria")
            val mechanicID: Long? = fetchForeignKeyID(db, "Mechanics", "desired_criteria")
            val vehicleID: Long? = fetchForeignKeyID(db, "Vehicles", "desired_criteria")
            val customerId: Long = getSignedInCustomerID()

            // Check for valid foreign key IDs and selected date
            if (serviceID == null) {
                Log.e("BookingsActivity", "Error: serviceID is null")
                Toast.makeText(this, "Error: Service ID not found.", Toast.LENGTH_SHORT).show()
                return
            }
            if (mechanicID == null) {
                Log.e("BookingsActivity", "Error: mechanicID is null")
                Toast.makeText(this, "Error: Mechanic ID not found.", Toast.LENGTH_SHORT).show()
                return
            }
            if (vehicleID == null) {
                Log.e("BookingsActivity", "Error: vehicleID is null")
                Toast.makeText(this, "Error: Vehicle ID not found.", Toast.LENGTH_SHORT).show()
                return
            }
            if (selectedDate == null) {
                Log.e("BookingsActivity", "Error: selectedDate is null")
                Toast.makeText(this, "Error: Please select a date.", Toast.LENGTH_SHORT).show()
                return
            }

            // Insert the booking with foreign keys (Assuming `insertBooking` method exists and is defined correctly)
            val resultMessage = db.insertBooking(serviceID, mechanicID, vehicleID, customerId, selectedDate!!)
            Toast.makeText(this, resultMessage, Toast.LENGTH_SHORT).show()

        } catch (e: Exception) {
            Log.e("BookingsActivity", "Error creating booking: ${e.message}", e)
            Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    // Function to fetch foreign key ID from the database (simplified example)
    private fun fetchForeignKeyID(db: DatabaseHelper, tableName: String, criteria: String): Long? {
        val dbReadable = db.readableDatabase
        var cursor: android.database.Cursor? = null
        return try {
            cursor = dbReadable.rawQuery("SELECT id FROM $tableName WHERE criteriaColumn = ?", arrayOf(criteria))
            if (cursor.moveToFirst()) {
                cursor.getLong(cursor.getColumnIndexOrThrow("id"))
            } else {
                Log.e("BookingsActivity", "Error: No matching ID found in $tableName for criteria $criteria")
                null
            }
        } catch (e: Exception) {
            Log.e("BookingsActivity", "Error fetching foreign key ID from $tableName: ${e.message}", e)
            null
        } finally {
            cursor?.close()
        }
    }

    // Dummy function to retrieve the signed-in customer ID (replace with your actual logic)
    private fun getSignedInCustomerID(): Long {
        val sharedPreferences = getSharedPreferences("app_prefs", MODE_PRIVATE)
        return sharedPreferences.getLong("customerID", -1L) // Replace "customerID" with your actual key
    }
}

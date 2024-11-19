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
            createBooking(db)
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

    // Function to create a booking with only the date
    private fun createBooking(db: DatabaseHelper) {
        try {
            if (selectedDate == null) {
                Log.e("BookingsActivity", "Error: selectedDate is null")
                Toast.makeText(this, "Error: Please select a date.", Toast.LENGTH_SHORT).show()
                return
            }

            // Insert the booking with only the date
            val resultMessage = db.insertBooking(selectedDate!!)
            Toast.makeText(this, resultMessage, Toast.LENGTH_SHORT).show()

        } catch (e: Exception) {
            Log.e("BookingsActivity", "Error creating booking: ${e.message}", e)
            Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }
}

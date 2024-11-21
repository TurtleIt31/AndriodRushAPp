package com.example.rushapp

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import androidx.activity.ComponentActivity
import java.util.Calendar

class BookingsActivity : ComponentActivity() {

    private var selectedDate: String? = null // Variable to store the selected date

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_appointments) // Set the content view to the XML layout
        val backButton = findViewById<Button>(R.id.backButton)

        backButton.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }


/*   // Back button logic
        val backButton = findViewById<Button>(R.id.backButtonAppointments)
        backButton.setOnClickListener {
            navigateToProfile()
        }
    }

    private fun navigateToProfile() {
        val intent = Intent(this, ProfileActivity::class.java)
        startActivity(intent)
        finish() // Prevents returning to this screen
    } */


    // Initialize the database helper
        val db = DatabaseHelper(this)

        // Display bookings for the logged-in user
        displayUserBookings(db)

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

            // Example: Fetch required IDs from the database
            val serviceId = 1L // Replace with logic to get serviceId
            val mechanicId = 1L // Replace with logic to get mechanicId
            val vehicleId = 1L // Replace with logic to get vehicleId
            val customerId = 1L // Replace with logic to get customerId

            // Insert the booking
            val resultMessage = db.insertBooking(
                db.writableDatabase,
                serviceId,
                mechanicId,
                vehicleId,
                customerId,
                selectedDate!!,
                "Scheduled"
            )
            Toast.makeText(this, resultMessage, Toast.LENGTH_SHORT).show()

            // Refresh the displayed bookings
            displayUserBookings(db)

        } catch (e: Exception) {
            Log.e("BookingsActivity", "Error creating booking: ${e.message}", e)
            Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }


    // Function to fetch and display bookings for the logged-in user
    private fun displayUserBookings(db: DatabaseHelper) {
        val userEmail = getSignedInEmail() // Retrieve the logged-in user's email from the intent
        val cursor = db.getBookingsForUser(db.readableDatabase, userEmail)

        val bookingsList = mutableListOf<String>() // List of strings to collect booking details

        if (cursor.moveToFirst()) {
            do {
                val bookingDate = cursor.getString(cursor.getColumnIndexOrThrow("bookingDate"))
                val status = cursor.getString(cursor.getColumnIndexOrThrow("status"))
                bookingsList.add("Date: $bookingDate, Status: $status")
            } while (cursor.moveToNext())
        } else {
            bookingsList.add("No bookings found for the user.")
            Log.d("BookingsActivity", "No bookings found for the user.")
        }
        cursor.close()

        // Display the bookings in a Toast message
        val message = bookingsList.joinToString(separator = "\n")
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    // Function to retrieve the email passed from the login activity
    private fun getSignedInEmail(): String {
        val email = intent.getStringExtra("email") ?: ""
        Log.d("BookingsActivity", "Signed-in email: $email") // Log the email for debugging
        return email
    }



}

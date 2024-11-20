package com.example.rushapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.ComponentActivity

class ProfileActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        val email = intent.getStringExtra("email") ?: ""
        val userType = intent.getStringExtra("userType") ?: ""
        val appointmentsButton = findViewById<Button>(R.id.scheduleAppBtn)
        val editProfileButton = findViewById<Button>(R.id.EditProfileBtn)
        val vehiclesButton = findViewById<Button>(R.id.ManageVehiclesBtn)
        val invoicesButton = findViewById<Button>(R.id.ViewInvoicesBtn)
        val mechanicsButton = findViewById<Button>(R.id.MyMechanicsBtn)

        appointmentsButton.setOnClickListener {
            val intent = Intent(this, BookingsActivity::class.java)
            intent.putExtra("email", email) // Pass the email to BookingsActivity
            startActivity(intent)
        }
        editProfileButton.setOnClickListener {
            val intent = Intent(this, EditUserActivity::class.java)
            intent.putExtra("email", email) // Pass the email to BookingsActivity
            startActivity(intent)
        }
        vehiclesButton.setOnClickListener {
            val intent = Intent(this, VehicleActivity::class.java)
            intent.putExtra("userType", userType)
            intent.putExtra("email", email) // Pass the email to BookingsActivity
            startActivity(intent)
        }
        invoicesButton.setOnClickListener {
            val intent = Intent(this, InvoicesActivity::class.java)
            intent.putExtra("userType", userType)
            intent.putExtra("email", email) // Pass the email to BookingsActivity
            startActivity(intent)
        }
        mechanicsButton.setOnClickListener {
            val intent = Intent(this, BookingsActivity::class.java)
            intent.putExtra("email", email) // Pass the email to BookingsActivity
            startActivity(intent)
        }



    }
}


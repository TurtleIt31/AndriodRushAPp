package com.example.rushapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
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
import com.example.rushapp.ui.theme.RushAppTheme

class ProfileActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val appointmentsButton = findViewById<Button>(R.id.scheduleAppBtn)
        val editProfileButton = findViewById<Button>(R.id.EditProfileBtn)
        val vehiclesButton = findViewById<Button>(R.id.ManageVehiclesBtn)
        val invoicesButton = findViewById<Button>(R.id.ViewInvoicesBtn)
        val mechanicsButton = findViewById<Button>(R.id.MyMechanicsBtn)

        appointmentsButton.setOnClickListener {
            val intent = Intent(this, BookingsActivity::class.java)
            startActivity(intent)
        }
        editProfileButton.setOnClickListener {
            val intent = Intent(this, EditUserActivity::class.java)
            startActivity(intent)
        }
        vehiclesButton.setOnClickListener {
            val intent = Intent(this, VehicleActivity::class.java)
            startActivity(intent)
        }
        invoicesButton.setOnClickListener {
            val intent = Intent(this, InvoicesActivity::class.java)
            startActivity(intent)
        }
        mechanicsButton.setOnClickListener {
            val intent = Intent(this, BookingsActivity::class.java)
            startActivity(intent)
        }










    }
}


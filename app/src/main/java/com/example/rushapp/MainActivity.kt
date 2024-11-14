package com.example.rushapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.ComponentActivity
import com.example.rushapp.ui.theme.RushAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main) // Load the main layout
        val dataHandler = DataHandler(this)
        dataHandler.populateSampleData()

        // Start the login activity when button is clicked
        val button = findViewById<Button>(R.id.startBtn)
        button?.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}

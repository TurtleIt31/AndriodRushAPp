package com.example.rushapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MechanicsActivity : ComponentActivity() {
    private lateinit var dataHandler: DataHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mechanics)

        dataHandler = DataHandler(this) // Initialize DataHandler

        // Get the database instance
        val db = dataHandler.dbHelper.readableDatabase

        // Fetch mechanics from the database
        val mechanics = dataHandler.getAllMechanics(db)

        // Find RecyclerView and set up adapter
        val recyclerView: RecyclerView = findViewById(R.id.mechanicsRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = MechanicsAdapter(mechanics)

        // Close database after use
        db.close()
    }
}



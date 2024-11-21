package com.example.rushapp

import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity


class InvoicesActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_invoices)

        val userType = intent.getStringExtra("userType") ?: ""
        val email = intent.getStringExtra("email")?: ""

        // Find the button in the layout
        val createInvoiceButton = findViewById<View>(R.id.createInvoiceButton)

        // Set visibility based on userType
        if (userType == "Mechanic" || userType == "Admin") {
            createInvoiceButton.visibility = View.VISIBLE
        } else {
            createInvoiceButton.visibility = View.GONE
        }
    }
}

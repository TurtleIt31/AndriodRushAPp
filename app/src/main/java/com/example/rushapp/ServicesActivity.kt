package com.example.rushapp

import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.rushapp.models.ServiceItem
import com.example.rushapp.ui.theme.RushAppTheme

class ServicesActivity : ComponentActivity() {

    private lateinit var servicesRecyclerView: RecyclerView
    private lateinit var dataHandler: DataHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_service)

        servicesRecyclerView = findViewById(R.id.servicesRecyclerView)
        servicesRecyclerView.layoutManager = LinearLayoutManager(this)

        dataHandler = DataHandler(this)

        // Fetch and display services
        fetchAndDisplayServices()
    }
    private fun fetchAndDisplayServices() {
        val db = dataHandler.dbHelper.readableDatabase
        val services = getAllServices(db)
        db.close()

        if (services.isNotEmpty()) {
            servicesRecyclerView.adapter = ServicesAdapter(services)
        } else {
            Toast.makeText(this, "No services available.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getAllServices(db: SQLiteDatabase): List<ServiceItem> {
        val services = mutableListOf<ServiceItem>()
        val cursor = db.query(
            "Services",
            arrayOf("serviceId", "mechanicId", "vehicleId", "date", "serviceType", "serviceDescription"),
            null,
            null,
            null,
            null,
            "date ASC" // Order by date
        )
        cursor.use {
            while (it.moveToNext()) {
                services.add(
                    ServiceItem(
                        serviceId = it.getLong(it.getColumnIndexOrThrow("serviceId")),
                        mechanicId = it.getLong(it.getColumnIndexOrThrow("mechanicId")),
                        vehicleId = it.getLong(it.getColumnIndexOrThrow("vehicleId")),
                        date = it.getString(it.getColumnIndexOrThrow("date")),
                        serviceType = it.getString(it.getColumnIndexOrThrow("serviceType")),
                        serviceDescription = it.getString(it.getColumnIndexOrThrow("serviceDescription"))
                    )
                )
            }
        }
        return services
    }

    // Adapter for RecyclerView
    class ServicesAdapter(private val services: List<ServiceItem>) :
        RecyclerView.Adapter<ServicesAdapter.ServiceViewHolder>() {

        class ServiceViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val serviceType: TextView = view.findViewById(R.id.serviceType)
            val serviceDate: TextView = view.findViewById(R.id.serviceDate)
            val serviceDescription: TextView = view.findViewById(R.id.serviceDescription)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServiceViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_service, parent, false)
            return ServiceViewHolder(view)
        }

        override fun onBindViewHolder(holder: ServiceViewHolder, position: Int) {
            val service = services[position]
            holder.serviceType.text = service.serviceType
            holder.serviceDate.text = service.date
            holder.serviceDescription.text = service.serviceDescription
        }

        override fun getItemCount() = services.size
    }

}


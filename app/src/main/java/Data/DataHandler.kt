package com.example.rushapp

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.util.Log

class DataHandler(context: Context) {

    private val dbHelper = DatabaseHelper(context)

    // **Populate Sample Data**
    fun populateSampleData() {
        val db = dbHelper.writableDatabase

        // Insert or fetch Workshop
        val workshopId = getWorkshopIdByName(db, "Example Workshop")
            ?: dbHelper.insertWorkshop(db, "Example Workshop", "123 Main Street")

        // Insert or fetch Mechanic
        val mechanicId = getMechanicIdByEmail(db, "johndoe@example.com")
            ?: dbHelper.insertMechanic(db, workshopId, "John Doe", "johndoe@example.com", "123-456-7890", "Mechanic")

        // Insert or fetch Customer
        val customerId = getCustomerIdByName(db, "Customer")
            ?: dbHelper.insertCustomer(db, "Customer")

        // Insert or fetch User
        val userId = getUserIdByEmail(db, "alice@example.com")
            ?: dbHelper.insertUser(db, mechanicId, customerId, "Alice Smith", "alice@example.com", "987-654-3210", "Admin", "password123")

        // Insert or fetch Vehicle
        val vehicleId = getVehicleIdByVin(db, "1HGCM82633A123456")
            ?: dbHelper.insertVehicle(db, userId, "Toyota", "Corolla", 2020, "1HGCM82633A123456")

        // Insert or fetch Service
        val serviceId = getServiceIdByDetails(db, mechanicId, vehicleId, "2024-11-12")
            ?: dbHelper.insertService(db, mechanicId, vehicleId, "2024-11-12", "Oil Change", "Changed engine oil and filter")

        // Insert or fetch Invoice
        val invoiceId = getInvoiceIdByServiceId(db, serviceId)
            ?: insertInvoiceWithItems(
                db,
                serviceId = serviceId,
                totalCost = 250.75,
                invoiceItems = listOf(
                    "Oil Change" to 50.00,
                    "Brake Repair" to 150.75,
                    "Tire Replacement" to 50.00
                )
            )

        // Insert Booking if not already present
        if (!isBookingPresent(db, serviceId, mechanicId, vehicleId, customerId, "2024-11-20")) {
            val resultMessage = dbHelper.insertBooking(
                db,
                serviceId,
                mechanicId,
                vehicleId,
                customerId,
                "2024-11-20",
                "Scheduled"
            )
            println(resultMessage)
        }

        db.close()
    }

    // **Invoice Functions**
    fun insertInvoiceWithItems(
        db: SQLiteDatabase,
        serviceId: Long,
        totalCost: Double,
        invoiceItems: List<Pair<String, Double>>? = null
    ): Long {
        // Insert Invoice
        val invoiceValues = ContentValues().apply {
            put("serviceId", serviceId)
            put("totalCost", totalCost)
        }

        val invoiceId = db.insert("Invoices", null, invoiceValues)
        if (invoiceId == -1L) {
            Log.e("DataHandler", "Error inserting invoice.")
            return -1L
        }

        // Insert Invoice Items
        invoiceItems?.forEach { (itemName, cost) ->
            val itemValues = ContentValues().apply {
                put("invoiceId", invoiceId)
                put("itemName", itemName)
                put("cost", cost)
            }

            val itemId = db.insert("InvoiceItems", null, itemValues)
            if (itemId == -1L) {
                Log.e("DataHandler", "Error inserting invoice item: $itemName")
            }
        }

        return invoiceId
    }

    fun getInvoiceIdByServiceId(db: SQLiteDatabase, serviceId: Long): Long? {
        val cursor = db.query(
            "Invoices",
            arrayOf("invoiceId"),
            "serviceId = ?",
            arrayOf(serviceId.toString()),
            null,
            null,
            null
        )
        return cursor.use {
            if (it.moveToFirst()) {
                it.getLong(it.getColumnIndexOrThrow("invoiceId"))
            } else {
                null
            }
        }
    }

    // **Service Functions**
    private fun getServiceIdByDetails(db: SQLiteDatabase, mechanicId: Long, vehicleId: Long, date: String): Long? {
        val cursor = db.query(
            "Services",
            arrayOf("serviceId"),
            "mechanicId = ? AND vehicleId = ? AND date = ?",
            arrayOf(mechanicId.toString(), vehicleId.toString(), date),
            null,
            null,
            null
        )
        return cursor.use {
            if (it.moveToFirst()) {
                it.getLong(it.getColumnIndexOrThrow("serviceId"))
            } else {
                null
            }
        }
    }

    private fun isBookingPresent(
        db: SQLiteDatabase,
        serviceId: Long,
        mechanicId: Long,
        vehicleId: Long,
        customerId: Long,
        bookingDate: String
    ): Boolean {
        val cursor = db.query(
            "Bookings",
            arrayOf("bookingId"),
            "serviceId = ? AND mechanicId = ? AND vehicleId = ? AND customerId = ? AND bookingDate = ?",
            arrayOf(serviceId.toString(), mechanicId.toString(), vehicleId.toString(), customerId.toString(), bookingDate),
            null,
            null,
            null
        )
        return cursor.use { it.moveToFirst() }
    }

    // **General Get Functions**
    private fun getWorkshopIdByName(db: SQLiteDatabase, name: String): Long? {
        val cursor = db.query(
            "Workshops",
            arrayOf("workshopId"),
            "name = ?",
            arrayOf(name),
            null,
            null,
            null
        )
        return cursor.use {
            if (it.moveToFirst()) {
                it.getLong(it.getColumnIndexOrThrow("workshopId"))
            } else {
                null
            }
        }
    }

    private fun getMechanicIdByEmail(db: SQLiteDatabase, email: String): Long? {
        val cursor = db.query(
            "Mechanics",
            arrayOf("mechanicId"),
            "email = ?",
            arrayOf(email),
            null,
            null,
            null
        )
        return cursor.use {
            if (it.moveToFirst()) {
                it.getLong(it.getColumnIndexOrThrow("mechanicId"))
            } else {
                null
            }
        }
    }

    private fun getCustomerIdByName(db: SQLiteDatabase, name: String): Long? {
        val cursor = db.query(
            "Customers",
            arrayOf("customerId"),
            "name = ?",
            arrayOf(name),
            null,
            null,
            null
        )
        return cursor.use {
            if (it.moveToFirst()) {
                it.getLong(it.getColumnIndexOrThrow("customerId"))
            } else {
                null
            }
        }
    }

    private fun getUserIdByEmail(db: SQLiteDatabase, email: String): Long? {
        val cursor = db.query(
            "Users",
            arrayOf("userId"),
            "email = ?",
            arrayOf(email),
            null,
            null,
            null
        )
        return cursor.use {
            if (it.moveToFirst()) {
                it.getLong(it.getColumnIndexOrThrow("userId"))
            } else {
                null
            }
        }
    }

    private fun getVehicleIdByVin(db: SQLiteDatabase, vin: String): Long? {
        val cursor = db.query(
            "Vehicles",
            arrayOf("vehicleId"),
            "vin = ?",
            arrayOf(vin),
            null,
            null,
            null
        )
        return cursor.use {
            if (it.moveToFirst()) {
                it.getLong(it.getColumnIndexOrThrow("vehicleId"))
            } else {
                null
            }
        }
    }
}

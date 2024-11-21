package com.example.rushapp

import Data.Models.Appointment
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import com.example.rushapp.models.ServiceItem

class DataHandler(context: Context) {

    val dbHelper = DatabaseHelper(context)

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
            ?: dbHelper.insertVehicle(db, userId, "Toyota", "Corolla", "2020", "1HGCM82633A123456")

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
            )//ends here

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

    fun insertNewMechanic(
        name: String,
        email: String,
        password: String,
        phone: String,
        workshopId: Long? // Nullable workshop ID
    ): Boolean {
        val db = dbHelper.writableDatabase // Open the database once
        return try {
            db.beginTransaction() // Start a transaction

            // Check if the email already exists
            if (doesUserExist(email)) {
                Log.e("DataHandler", "Mechanic with email $email already exists.")
                return false
            }

            // Register the mechanic as a customer first
            val customerId = insertCustomer(name, db) // Use the same db instance
            if (customerId == -1L) {
                Log.e("DataHandler", "Failed to register as customer for mechanic $name")
                return false
            }

            // Insert into the Users table
            val userValues = ContentValues().apply {
                put("name", name)
                put("email", email)
                put("passwordEntry", password) // Store plain-text password
                put("phone", phone)
                put("userType", "mechanic") // Define user type as mechanic
                put("customerId", customerId) // Associate mechanic with the customerId
            }

            val userId = db.insert("Users", null, userValues)
            if (userId == -1L) {
                Log.e("DataHandler", "Failed to insert into Users table for mechanic $name")
                return false
            }

            // Insert into the Mechanics table
            val mechanicValues = ContentValues().apply {
                put("name", name)
                put("email", email)
                put("phone", phone)
                if (workshopId != null) put("workshop_id", workshopId) // Optional workshop ID
                put("user_type", "mechanic") // Mechanic type in Mechanics table
            }

            val mechanicId = db.insert("mechanics", null, mechanicValues)
            if (mechanicId == -1L) {
                Log.e("DataHandler", "Failed to insert into Mechanics table for mechanic $name")
                return false
            }

            db.setTransactionSuccessful() // Commit transaction if all succeeded
            true
        } catch (e: Exception) {
            Log.e("DataHandler", "Error inserting new mechanic: ${e.message}")
            false
        } finally {
            db.endTransaction() // End transaction
            db.close() // Close the database
        }
    }









    fun insertNewUser(
        name: String,
        email: String,
        password: String,
        phone: String,
        userType: String,
        mechanicId: Long? = null
        ): Boolean {
        val db = dbHelper.writableDatabase // Open the database once
        return try {
            // Check if the email already exists
            if (doesUserExist(email)) {
                Log.e("DataHandler", "User with email $email already exists.")
                return false
            }

            // Register the user as a customer first
            val customerId = insertCustomer(name, db) // Use the same db instance
            if (customerId == -1L) {
                Log.e("DataHandler", "Failed to register as customer for user $name")
                return false
            }

            // Insert the user with the generated customerId
            val values = ContentValues().apply {
                put("name", name)
                put("email", email)
                put("passwordEntry", password)
                put("phone", phone)
                put("userType", userType)
                put("customerId", customerId) // Associate the user with the customerId
                put("mechanicId", mechanicId) // Nullable field
            }

            val newRowId = db.insert("Users", null, values)
            newRowId != -1L // Return true if insertion was successful
        } catch (e: Exception) {
            Log.e("DataHandler", "Error inserting new user: ${e.message}")
            false
        } finally {
            db.close() // Close the database only once all operations are complete
        }
    }

    fun insertCustomer(name: String, db: SQLiteDatabase): Long {
        return try {
            val values = ContentValues().apply {
                put("name", name) // Insert customer's name
            }
            val newRowId = db.insert("Customers", null, values)
            if (newRowId == -1L) {
                Log.e("DataHandler", "Error inserting customer: $name")
            }
            newRowId // Return the generated customerId
        } catch (e: Exception) {
            Log.e("DataHandler", "Error inserting customer: ${e.message}")
            -1L // Return -1 in case of an error
        }
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

    fun getInvoicesByEmail(db: SQLiteDatabase, email: String): List<Map<String, Any>> {
        val query = """
        SELECT i.invoiceId, i.totalCost, s.serviceId, s.description, s.date, v.make, v.model, v.year 
        FROM Invoices i
        INNER JOIN Services s ON i.serviceId = s.serviceId
        INNER JOIN Vehicles v ON s.vehicleId = v.vehicleId
        INNER JOIN Users u ON s.mechanicId = u.mechanicId
        WHERE u.email = ?
    """
        val cursor = db.rawQuery(query, arrayOf(email))
        return cursor.use { it.toListOfMaps() }
    }

    // Extension function to map Cursor rows to a list of maps
    private fun Cursor.toListOfMaps(): List<Map<String, Any>> {
        val list = mutableListOf<Map<String, Any>>()
        while (moveToNext()) {
            val row = (0 until columnCount).associate { columnIndex ->
                getColumnName(columnIndex) to when (getType(columnIndex)) {
                    Cursor.FIELD_TYPE_INTEGER -> getLong(columnIndex)
                    Cursor.FIELD_TYPE_FLOAT -> getDouble(columnIndex)
                    Cursor.FIELD_TYPE_STRING -> getString(columnIndex) ?: ""
                    else -> "" // Default value for null or unsupported fields
                }
            }
            list.add(row)
        }
        return list
    }





    // **Check if User Exists**
    fun doesUserExist(email: String): Boolean {
        val db = dbHelper.readableDatabase // Open database for reading
        val cursor = db.query(
            "Users",  // Table name
            arrayOf("userId"), // Select userId column
            "email = ?",       // WHERE clause
            arrayOf(email),    // WHERE arguments
            null,
            null,
            null
        )
        return cursor.use { it.moveToFirst() } // True if a record exists
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

    fun getAllMechanics(db: SQLiteDatabase): List<Mechanic> {
        val mechanics = mutableListOf<Mechanic>()
        val cursor = db.query(
            "Mechanics",
            arrayOf("name", "email"),
            null, // No WHERE clause to get all mechanics
            null,
            null,
            null,
            null
        )

        cursor.use {
            while (it.moveToNext()) {
                val name = it.getString(it.getColumnIndexOrThrow("name"))
                val email = it.getString(it.getColumnIndexOrThrow("email"))
                mechanics.add(Mechanic(name, email))
            }
        }
        return mechanics
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

    fun getOrInsertVehicle(db: SQLiteDatabase, vin: String, customerId: Long): Long {
        val vehicleId = getVehicleIdByVin(db, vin)
        return vehicleId ?: dbHelper.insertVehicle(db, customerId, vin, "Toyota", "2021", "20201543")
    }

    fun getServicesByVehicleId(db: SQLiteDatabase, vehicleId: Long): List<ServiceItem> {
        val services = mutableListOf<ServiceItem>()
        val cursor = db.query(
            "Services",
            arrayOf(
                "serviceId",
                "mechanicId",
                "vehicleId",
                "date",
                "serviceType",
                "description"
            ),
            "vehicleId = ?",
            arrayOf(vehicleId.toString()),
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
                        serviceDescription = it.getString(it.getColumnIndexOrThrow("description"))
                    )
                )
            }
        }
        return services
    }

    fun getAllServices(db: SQLiteDatabase): List<ServiceItem> {
        val services = mutableListOf<ServiceItem>()
        val cursor = db.query(
            "Services",
            arrayOf(
                "serviceId",
                "mechanicId",
                "vehicleId",
                "date",
                "serviceType",
                "description"
            ),
            null, // No WHERE clause since we want all rows
            null, // No WHERE arguments
            null, // No GROUP BY
            null, // No HAVING
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
                        serviceDescription = it.getString(it.getColumnIndexOrThrow("description"))
                    )
                )
            }
        }
        return services
    }


    fun insertService(
        db: SQLiteDatabase,
        mechanicId: Long,
        vehicleId: Long,
        date: String,
        serviceType: String,
        serviceDescription: String
    ): Boolean {
        return try {
            val values = ContentValues().apply {
                put("mechanicId", mechanicId)
                put("vehicleId", vehicleId)
                put("date", date)
                put("serviceType", serviceType)
                put("description", serviceDescription) // Ensure this is included
            }
            val newRowId = db.insert("Services", null, values)
            newRowId != -1L // Return true if insertion was successful
        } catch (e: Exception) {
            Log.e("DataHandler", "Error inserting service: ${e.message}")
            false
        }
    }



    fun getAppointmentsByVehicleId(db: SQLiteDatabase, vehicleId: Long): List<Appointment> {
        val appointments = mutableListOf<Appointment>()
        val cursor = db.query(
            "Bookings",
            arrayOf(
                "bookingId",
                "serviceId",
                "mechanicId",
                "vehicleId",
                "customerId",
                "bookingDate",
                "bookingStatus"
            ),
            "vehicleId = ?",
            arrayOf(vehicleId.toString()),
            null,
            null,
            "bookingDate ASC" // Order by date
        )
        cursor.use {
            while (it.moveToNext()) {
                appointments.add(
                    Appointment(
                        bookingId = it.getLong(it.getColumnIndexOrThrow("bookingId")),
                        serviceId = it.getLong(it.getColumnIndexOrThrow("serviceId")),
                        mechanicId = it.getLong(it.getColumnIndexOrThrow("mechanicId")),
                        vehicleId = it.getLong(it.getColumnIndexOrThrow("vehicleId")),
                        customerId = it.getLong(it.getColumnIndexOrThrow("customerId")),
                        bookingDate = it.getString(it.getColumnIndexOrThrow("bookingDate")),
                        bookingStatus = it.getString(it.getColumnIndexOrThrow("bookingStatus"))
                    )
                )
            }
        }
        return appointments
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

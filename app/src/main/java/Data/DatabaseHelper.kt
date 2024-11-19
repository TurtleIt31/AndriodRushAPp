package com.example.rushapp

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.database.Cursor


class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "RushApp.db"
        private const val DATABASE_VERSION = 4

        // Workshops table
        private const val TABLE_WORKSHOPS = "Workshops"
        private const val COLUMN_WORKSHOP_ID = "workshopId"
        private const val COLUMN_WORKSHOP_NAME = "name"
        private const val COLUMN_WORKSHOP_ADDRESS = "address"

        // Mechanics table
        private const val TABLE_MECHANICS = "Mechanics"
        private const val COLUMN_MECHANIC_ID = "mechanicId"
        private const val COLUMN_MECHANIC_WORKSHOP_ID = "workshopId"
        private const val COLUMN_MECHANIC_NAME = "name"
        private const val COLUMN_MECHANIC_EMAIL = "email"
        private const val COLUMN_MECHANIC_PHONE = "phone"
        private const val COLUMN_MECHANIC_USER_TYPE = "userType"

        // Customers table
        private const val TABLE_CUSTOMERS = "Customers"
        private const val COLUMN_CUSTOMER_ID = "customerId"
        private const val COLUMN_CUSTOMER_USER_TYPE = "userType"
        private const val COLUMN_CUSTOMER_NAME = "name"


        // Users table
        private const val TABLE_USERS = "Users"
        private const val COLUMN_USER_ID = "userId"
        private const val COLUMN_USER_MECHANIC_ID = "mechanicId"
        private const val COLUMN_USER_CUSTOMER_ID = "customerId"
        private const val COLUMN_USER_NAME = "name"
        private const val COLUMN_USER_EMAIL = "email"
        private const val COLUMN_USER_PHONE = "phone"
        private const val COLUMN_USER_TYPE = "userType"
        private const val COLUMN_USER_PASSWORD = "passwordEntry"

        // Vehicles table
        private const val TABLE_VEHICLES = "Vehicles"
        private const val COLUMN_VEHICLE_ID = "vehicleId"
        private const val COLUMN_VEHICLE_USER_ID = "userId"
        private const val COLUMN_VEHICLE_MAKE = "make"
        private const val COLUMN_VEHICLE_MODEL = "model"
        private const val COLUMN_VEHICLE_YEAR = "year"
        private const val COLUMN_VEHICLE_VIN = "vin"

        // Notifications table
        private const val TABLE_NOTIFICATIONS = "Notifications"
        private const val COLUMN_NOTIFICATION_ID = "notificationId"
        private const val COLUMN_NOTIFICATION_USER_ID = "userId"
        private const val COLUMN_NOTIFICATION_DATE = "date"
        private const val COLUMN_NOTIFICATION_MESSAGE = "message"

        // Ratings table
        private const val TABLE_RATINGS = "Ratings"
        private const val COLUMN_RATING_ID = "ratingId"
        private const val COLUMN_RATING_USER_ID = "userId"
        private const val COLUMN_RATING_MECHANIC_ID = "mechanicId"
        private const val COLUMN_RATING_VALUE = "rating"
        private const val COLUMN_RATING_REVIEW = "review"
        private const val COLUMN_RATING_DATE = "date"

        // Services table
        private const val TABLE_SERVICES = "Services"
        private const val COLUMN_SERVICE_ID = "serviceId"
        private const val COLUMN_SERVICE_MECHANIC_ID = "mechanicId"
        private const val COLUMN_SERVICE_VEHICLE_ID = "vehicleId"
        private const val COLUMN_SERVICE_DATE = "date"
        private const val COLUMN_SERVICE_TYPE = "serviceType"
        private const val COLUMN_SERVICE_DESCRIPTION = "description"

        // Invoices table
        private const val TABLE_INVOICES = "Invoices"
        private const val COLUMN_INVOICE_ID = "invoiceId"
        private const val COLUMN_INVOICE_SERVICE_ID = "serviceId"
        private const val COLUMN_INVOICE_TOTAL_COST = "totalCost"

        // InvoiceItems table
        private const val TABLE_INVOICE_ITEMS = "InvoiceItems"
        private const val COLUMN_ITEM_ID = "itemId"
        private const val COLUMN_ITEM_INVOICE_ID = "invoiceId"
        private const val COLUMN_ITEM_NAME = "itemName"
        private const val COLUMN_ITEM_COST = "cost"

        // Bookings table
        private const val TABLE_BOOKINGS = "Bookings"
        private const val COLUMN_BOOKING_ID = "bookingId"
        private const val COLUMN_BOOKING_SERVICE_ID = "serviceId"
        private const val COLUMN_BOOKING_MECHANIC_ID = "mechanicId"
        private const val COLUMN_BOOKING_VEHICLE_ID = "vehicleId"
        private const val COLUMN_BOOKING_CUSTOMER_ID = "customerId"
        private const val COLUMN_BOOKING_DATE = "bookingDate"
        private const val COLUMN_BOOKING_STATUS = "status"
    }



    override fun onCreate(db: SQLiteDatabase) {
        // Create Workshops Table
        db.execSQL("""
        CREATE TABLE $TABLE_WORKSHOPS (
            $COLUMN_WORKSHOP_ID INTEGER PRIMARY KEY AUTOINCREMENT,
            $COLUMN_WORKSHOP_NAME TEXT NOT NULL,
            $COLUMN_WORKSHOP_ADDRESS TEXT NOT NULL
        )
    """)

        // Create Mechanics Table
        db.execSQL("""
        CREATE TABLE $TABLE_MECHANICS (
            $COLUMN_MECHANIC_ID INTEGER PRIMARY KEY AUTOINCREMENT,
            $COLUMN_MECHANIC_WORKSHOP_ID INTEGER,
            $COLUMN_MECHANIC_NAME TEXT NOT NULL,
            $COLUMN_MECHANIC_EMAIL TEXT UNIQUE,
            $COLUMN_MECHANIC_PHONE TEXT,
            $COLUMN_MECHANIC_USER_TYPE TEXT,
            FOREIGN KEY ($COLUMN_MECHANIC_WORKSHOP_ID) REFERENCES $TABLE_WORKSHOPS($COLUMN_WORKSHOP_ID)
        )
    """)

        // Create Customers Table
        db.execSQL("""
        CREATE TABLE $TABLE_CUSTOMERS (
            $COLUMN_CUSTOMER_ID INTEGER PRIMARY KEY AUTOINCREMENT,
            $COLUMN_CUSTOMER_NAME TEXT,
            $COLUMN_CUSTOMER_USER_TYPE TEXT
        )
    """)

        // Create Users Table
        db.execSQL("""
        CREATE TABLE $TABLE_USERS (
            $COLUMN_USER_ID INTEGER PRIMARY KEY AUTOINCREMENT,
            $COLUMN_USER_MECHANIC_ID INTEGER,
            $COLUMN_USER_CUSTOMER_ID INTEGER NOT NULL,
            $COLUMN_USER_NAME TEXT NOT NULL,
            $COLUMN_USER_EMAIL TEXT UNIQUE,
            $COLUMN_USER_PHONE TEXT,
            $COLUMN_USER_TYPE TEXT,
            $COLUMN_USER_PASSWORD TEXT,
            FOREIGN KEY ($COLUMN_USER_MECHANIC_ID) REFERENCES $TABLE_MECHANICS($COLUMN_MECHANIC_ID),
            FOREIGN KEY ($COLUMN_USER_CUSTOMER_ID) REFERENCES $TABLE_CUSTOMERS($COLUMN_CUSTOMER_ID)
        )
    """)

        // Create Vehicles Table
        db.execSQL("""
        CREATE TABLE $TABLE_VEHICLES (
            $COLUMN_VEHICLE_ID INTEGER PRIMARY KEY AUTOINCREMENT,
            $COLUMN_VEHICLE_USER_ID INTEGER,
            $COLUMN_VEHICLE_MAKE TEXT,
            $COLUMN_VEHICLE_MODEL TEXT,
            $COLUMN_VEHICLE_YEAR INTEGER,
            $COLUMN_VEHICLE_VIN TEXT UNIQUE,
            FOREIGN KEY ($COLUMN_VEHICLE_USER_ID) REFERENCES $TABLE_USERS($COLUMN_USER_ID)
        )
    """)

        // Create Notifications Table
        db.execSQL("""
        CREATE TABLE $TABLE_NOTIFICATIONS (
            $COLUMN_NOTIFICATION_ID INTEGER PRIMARY KEY AUTOINCREMENT,
            $COLUMN_NOTIFICATION_USER_ID INTEGER,
            $COLUMN_NOTIFICATION_DATE DATE,
            $COLUMN_NOTIFICATION_MESSAGE TEXT,
            FOREIGN KEY ($COLUMN_NOTIFICATION_USER_ID) REFERENCES $TABLE_USERS($COLUMN_USER_ID)
        )
    """)

        // Create Ratings Table
        db.execSQL("""
        CREATE TABLE $TABLE_RATINGS (
            $COLUMN_RATING_ID INTEGER PRIMARY KEY AUTOINCREMENT,
            $COLUMN_RATING_USER_ID INTEGER,
            $COLUMN_RATING_MECHANIC_ID INTEGER,
            $COLUMN_RATING_VALUE INTEGER CHECK ($COLUMN_RATING_VALUE >= 1 AND $COLUMN_RATING_VALUE <= 5),
            $COLUMN_RATING_REVIEW TEXT,
            $COLUMN_RATING_DATE DATE,
            FOREIGN KEY ($COLUMN_RATING_USER_ID) REFERENCES $TABLE_USERS($COLUMN_USER_ID),
            FOREIGN KEY ($COLUMN_RATING_MECHANIC_ID) REFERENCES $TABLE_MECHANICS($COLUMN_MECHANIC_ID)
        )
    """)

        // Create Services Table
        db.execSQL("""
        CREATE TABLE $TABLE_SERVICES (
            $COLUMN_SERVICE_ID INTEGER PRIMARY KEY AUTOINCREMENT,
            $COLUMN_SERVICE_MECHANIC_ID INTEGER,
            $COLUMN_SERVICE_VEHICLE_ID INTEGER,
            $COLUMN_SERVICE_DATE DATE,
            $COLUMN_SERVICE_TYPE TEXT,
            $COLUMN_SERVICE_DESCRIPTION TEXT,
            FOREIGN KEY ($COLUMN_SERVICE_MECHANIC_ID) REFERENCES $TABLE_MECHANICS($COLUMN_MECHANIC_ID),
            FOREIGN KEY ($COLUMN_SERVICE_VEHICLE_ID) REFERENCES $TABLE_VEHICLES($COLUMN_VEHICLE_ID)
        )
    """)

        // Create Invoices Table
        db.execSQL("""
        CREATE TABLE $TABLE_INVOICES (
            $COLUMN_INVOICE_ID INTEGER PRIMARY KEY AUTOINCREMENT,
            $COLUMN_INVOICE_SERVICE_ID INTEGER,
            $COLUMN_INVOICE_TOTAL_COST DECIMAL(10, 2),
            FOREIGN KEY ($COLUMN_INVOICE_SERVICE_ID) REFERENCES $TABLE_SERVICES($COLUMN_SERVICE_ID)
        )
    """)

        // Create Invoice Items Table
        db.execSQL("""
        CREATE TABLE $TABLE_INVOICE_ITEMS (
            $COLUMN_ITEM_ID INTEGER PRIMARY KEY AUTOINCREMENT,
            $COLUMN_ITEM_INVOICE_ID INTEGER,
            $COLUMN_ITEM_NAME TEXT,
            $COLUMN_ITEM_COST DECIMAL(10, 2),
            FOREIGN KEY ($COLUMN_ITEM_INVOICE_ID) REFERENCES $TABLE_INVOICES($COLUMN_INVOICE_ID)
        )
    """)

        // Create Bookings Table
        db.execSQL("""
        CREATE TABLE $TABLE_BOOKINGS (
            $COLUMN_BOOKING_ID INTEGER PRIMARY KEY AUTOINCREMENT,
            $COLUMN_BOOKING_SERVICE_ID INTEGER,
            $COLUMN_BOOKING_MECHANIC_ID INTEGER,
            $COLUMN_BOOKING_VEHICLE_ID INTEGER,
            $COLUMN_BOOKING_CUSTOMER_ID INTEGER,
            $COLUMN_BOOKING_DATE DATE,
            $COLUMN_BOOKING_STATUS TEXT,
            FOREIGN KEY ($COLUMN_BOOKING_SERVICE_ID) REFERENCES $TABLE_SERVICES($COLUMN_SERVICE_ID),
            FOREIGN KEY ($COLUMN_BOOKING_MECHANIC_ID) REFERENCES $TABLE_MECHANICS($COLUMN_MECHANIC_ID),
            FOREIGN KEY ($COLUMN_BOOKING_VEHICLE_ID) REFERENCES $TABLE_VEHICLES($COLUMN_VEHICLE_ID),
            FOREIGN KEY ($COLUMN_BOOKING_CUSTOMER_ID) REFERENCES $TABLE_CUSTOMERS($COLUMN_CUSTOMER_ID)
        )
    """)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_BOOKINGS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_INVOICE_ITEMS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_INVOICES")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_SERVICES")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_RATINGS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NOTIFICATIONS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_VEHICLES")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_USERS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_CUSTOMERS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_MECHANICS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_WORKSHOPS")
        onCreate(db)
    }




    // --- Workshops Table ---
    fun insertWorkshop(db: SQLiteDatabase, name: String, address: String): Long {
        val values = ContentValues().apply {
            put("name", name)
            put("address", address)
        }
        return db.insert("Workshops", null, values)
    }

    fun getWorkshop(db: SQLiteDatabase, id: Long): Cursor {
        return db.query("Workshops", null, "workshopId = ?", arrayOf(id.toString()), null, null, null)
    }

    fun updateWorkshop(db: SQLiteDatabase, id: Long, name: String, address: String): Int {
        val values = ContentValues().apply {
            put("name", name)
            put("address", address)
        }
        return db.update("Workshops", values, "workshopId = ?", arrayOf(id.toString()))
    }

    fun deleteWorkshop(db: SQLiteDatabase, id: Long): Int {
        return db.delete("Workshops", "workshopId = ?", arrayOf(id.toString()))
    }

    // --- Mechanics Table ---
    fun insertMechanic(db: SQLiteDatabase, workshopId: Long, name: String, email: String, phone: String, userType: String): Long {
        val values = ContentValues().apply {
            put("workshopId", workshopId)
            put("name", name)
            put("email", email)
            put("phone", phone)
            put("userType", userType)
        }
        return db.insert("Mechanics", null, values)
    }

    fun getMechanic(db: SQLiteDatabase, id: Long): Cursor {
        return db.query("Mechanics", null, "mechanicId = ?", arrayOf(id.toString()), null, null, null)
    }

    fun updateMechanic(db: SQLiteDatabase, id: Long, name: String, email: String, phone: String, userType: String): Int {
        val values = ContentValues().apply {
            put("name", name)
            put("email", email)
            put("phone", phone)
            put("userType", userType)
        }
        return db.update("Mechanics", values, "mechanicId = ?", arrayOf(id.toString()))
    }

    fun deleteMechanic(db: SQLiteDatabase, id: Long): Int {
        return db.delete("Mechanics", "mechanicId = ?", arrayOf(id.toString()))
    }

    // --- Customers Table ---
    fun insertCustomer(db: SQLiteDatabase, userType: String): Long {
        val values = ContentValues().apply {
            put("userType", userType)
        }
        return db.insert("Customers", null, values)
    }

    fun getCustomer(db: SQLiteDatabase, id: Long): Cursor {
        return db.query("Customers", null, "customerId = ?", arrayOf(id.toString()), null, null, null)
    }

    fun updateCustomer(db: SQLiteDatabase, id: Long, userType: String): Int {
        val values = ContentValues().apply {
            put("userType", userType)
        }
        return db.update("Customers", values, "customerId = ?", arrayOf(id.toString()))
    }

    fun deleteCustomer(db: SQLiteDatabase, id: Long): Int {
        return db.delete("Customers", "customerId = ?", arrayOf(id.toString()))
    }

    // --- Users Table ---
    fun insertUser(db: SQLiteDatabase, mechanicId: Long?, customerId: Long?, name: String, email: String, phone: String, userType: String, password: String): Long {
        val values = ContentValues().apply {
            put("mechanicId", mechanicId)
            put("customerId", customerId)
            put("name", name)
            put("email", email)
            put("phone", phone)
            put("userType", userType)
            put("passwordEntry", password)
        }
        return db.insert("Users", null, values)
    }

    fun getUser(db: SQLiteDatabase, id: Long): Cursor {
        return db.query("Users", null, "userId = ?", arrayOf(id.toString()), null, null, null)
    }

    fun updateUser(db: SQLiteDatabase, id: Long, name: String, email: String, phone: String, userType: String, password: String): Int {
        val values = ContentValues().apply {
            put("name", name)
            put("email", email)
            put("phone", phone)
            put("userType", userType)
            put("passwordEntry", password)
        }
        return db.update("Users", values, "userId = ?", arrayOf(id.toString()))
    }

    fun deleteUser(db: SQLiteDatabase, id: Long): Int {
        return db.delete("Users", "userId = ?", arrayOf(id.toString()))
    }

    // --- Vehicles Table ---
    fun insertVehicle(db: SQLiteDatabase, userId: Long, make: String, model: String, year: Int, vin: String): Long {
        val values = ContentValues().apply {
            put("userId", userId)
            put("make", make)
            put("model", model)
            put("year", year)
            put("vin", vin)
        }
        return db.insert("Vehicles", null, values)
    }

    fun getVehicle(db: SQLiteDatabase, id: Long): Cursor {
        return db.query("Vehicles", null, "vehicleId = ?", arrayOf(id.toString()), null, null, null)
    }

    fun updateVehicle(db: SQLiteDatabase, id: Long, make: String, model: String, year: Int, vin: String): Int {
        val values = ContentValues().apply {
            put("make", make)
            put("model", model)
            put("year", year)
            put("vin", vin)
        }
        return db.update("Vehicles", values, "vehicleId = ?", arrayOf(id.toString()))
    }

    fun deleteVehicle(db: SQLiteDatabase, id: Long): Int {
        return db.delete("Vehicles", "vehicleId = ?", arrayOf(id.toString()))
    }

    // --- Notifications Table ---
    fun insertNotification(db: SQLiteDatabase, userId: Long, date: String, message: String): Long {
        val values = ContentValues().apply {
            put("userId", userId)
            put("date", date)
            put("message", message)
        }
        return db.insert("Notifications", null, values)
    }

    fun getNotification(db: SQLiteDatabase, id: Long): Cursor {
        return db.query("Notifications", null, "notificationId = ?", arrayOf(id.toString()), null, null, null)
    }

    fun deleteNotification(db: SQLiteDatabase, id: Long): Int {
        return db.delete("Notifications", "notificationId = ?", arrayOf(id.toString()))
    }

    // --- Ratings Table ---
    fun insertRating(db: SQLiteDatabase, userId: Long, mechanicId: Long, rating: Int, review: String, date: String): Long {
        val values = ContentValues().apply {
            put("userId", userId)
            put("mechanicId", mechanicId)
            put("rating", rating)
            put("review", review)
            put("date", date)
        }
        return db.insert("Ratings", null, values)
    }

    fun getRating(db: SQLiteDatabase, id: Long): Cursor {
        return db.query("Ratings", null, "ratingId = ?", arrayOf(id.toString()), null, null, null)
    }

    fun deleteRating(db: SQLiteDatabase, id: Long): Int {
        return db.delete("Ratings", "ratingId = ?", arrayOf(id.toString()))
    }

    // --- Services Table ---
    fun insertService(db: SQLiteDatabase, mechanicId: Long, vehicleId: Long, date: String, serviceType: String, description: String): Long {
        val values = ContentValues().apply {
            put("mechanicId", mechanicId)
            put("vehicleId", vehicleId)
            put("date", date)
            put("serviceType", serviceType)
            put("description", description)
        }
        return db.insert("Services", null, values)
    }

    fun getService(db: SQLiteDatabase, id: Long): Cursor {
        return db.query("Services", null, "serviceId = ?", arrayOf(id.toString()), null, null, null)
    }

    fun deleteService(db: SQLiteDatabase, id: Long): Int {
        return db.delete("Services", "serviceId = ?", arrayOf(id.toString()))
    }

    // --- Invoices Table ---
    fun insertInvoice(db: SQLiteDatabase, serviceId: Long, totalCost: Double): Long {
        val values = ContentValues().apply {
            put("serviceId", serviceId)
            put("totalCost", totalCost)
        }
        return db.insert("Invoices", null, values)
    }

    fun getInvoice(db: SQLiteDatabase, id: Long): Cursor {
        return db.query("Invoices", null, "invoiceId = ?", arrayOf(id.toString()), null, null, null)
    }

    fun deleteInvoice(db: SQLiteDatabase, id: Long): Int {
        return db.delete("Invoices", "invoiceId = ?", arrayOf(id.toString()))
    }

    // --- InvoiceItems Table ---
    fun insertInvoiceItem(db: SQLiteDatabase, invoiceId: Long, itemName: String, cost: Double): Long {
        val values = ContentValues().apply {
            put("invoiceId", invoiceId)
            put("itemName", itemName)
            put("cost", cost)
        }
        return db.insert("InvoiceItems", null, values)
    }

    fun getInvoiceItem(db: SQLiteDatabase, id: Long): Cursor {
        return db.query("InvoiceItems", null, "itemId = ?", arrayOf(id.toString()), null, null, null)
    }

    fun deleteInvoiceItem(db: SQLiteDatabase, id: Long): Int {
        return db.delete("InvoiceItems", "itemId = ?", arrayOf(id.toString()))
    }

    // --- Bookings Table ---

    fun insertBooking(

        bookingDate: String
    ): String {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("bookingDate", bookingDate)
            put("status", "Not Complete")
        }
        val result = db.insert("Bookings", null, values)
        return if (result != -1L) {
            "Booking successfully created."
        } else {
            "Error: Could not create booking."
        }
    }

    fun getBooking(db: SQLiteDatabase, bookingId: Long): Cursor {
        return db.query("Bookings", null, "bookingId = ?", arrayOf(bookingId.toString()), null, null, null)
    }
    //gets bookings for certain users
    fun getBookingsForUser(db: SQLiteDatabase, userEmail: String): Cursor {
        // Adjust the join condition based on your actual schema
        val query = """
        SELECT b.*
        FROM Bookings b
        INNER JOIN Users u ON b.customerId = u.userId
        WHERE u.email = ?
        ORDER BY b.bookingDate ASC
    """
        return db.rawQuery(query, arrayOf(userEmail))
    }


    fun updateBooking(db: SQLiteDatabase, bookingId: Long, serviceId: Long, mechanicId: Long, vehicleId: Long, customerId: Long, bookingDate: String, bookingStatus: String): Int {
        val values = ContentValues().apply {
            put("serviceId", serviceId)
            put("mechanicId", mechanicId)
            put("vehicleId", vehicleId)
            put("customerId", customerId)
            put("bookingDate", bookingDate)
            put("status", bookingStatus)
        }
        return db.update("Bookings", values, "bookingId = ?", arrayOf(bookingId.toString()))
    }

    fun deleteBooking(db: SQLiteDatabase, bookingId: Long): Int {
        return db.delete("Bookings", "bookingId = ?", arrayOf(bookingId.toString()))
    }






}



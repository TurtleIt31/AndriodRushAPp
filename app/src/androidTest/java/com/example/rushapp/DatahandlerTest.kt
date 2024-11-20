package com.example.rushapp

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.Assert.assertTrue
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DataHandlerTest {

    private lateinit var dataHandler: DataHandler
    private lateinit var dbHelper: DatabaseHelper
    private lateinit var context: Context

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
        dataHandler = DataHandler(context)
        dbHelper = DatabaseHelper(context)
        dataHandler.populateSampleData() // Populate sample data
    }

    @After
    fun tearDown() {
        dbHelper.close() // Ensure the database is closed after the tests
    }

    @Test
    fun testUserDataExists() {
        val db = dbHelper.readableDatabase
        val cursor = db.query("Users", null, null, null, null, null, null)

        if (cursor.count > 0) {
            cursor.moveToFirst()

            val columnNames = cursor.columnNames
            println("Column Names in Users Table: ${columnNames.joinToString()}")

            do {
                val userIdIndex = cursor.getColumnIndex("userId")
                val userId = cursor.getLong(userIdIndex)

                assertTrue("userId should not be null or 0", userId != 0L)

                val rowValues = columnNames.map { columnName ->
                    "$columnName: ${cursor.getString(cursor.getColumnIndexOrThrow(columnName))}"
                }
                println("Row: ${rowValues.joinToString()}")
            } while (cursor.moveToNext())
        } else {
            println("No data found in the Users table")
        }

        cursor.close()
        db.close()
    }

    @Test
    fun testBookingExists() {
        val db = dbHelper.readableDatabase
        val cursor = db.query("Bookings", null, null, null, null, null, null)

        // Check if there are rows in the Bookings table
        assertTrue("No data found in the Bookings table", cursor.count > 0)

        println("Number of rows in Bookings table: ${cursor.count}")

        if (cursor.count > 0) {
            cursor.moveToFirst()

            val columnNames = cursor.columnNames
            println("Column Names in Bookings Table: ${columnNames.joinToString()}")

            println("Data in Bookings Table:")
            do {
                val bookingIdIndex = cursor.getColumnIndex("bookingId")
                val bookingId = cursor.getLong(bookingIdIndex)

                // Assert that bookingId is valid
                assertTrue("bookingId should not be null or 0", bookingId != 0L)

                val rowValues = columnNames.map { columnName ->
                    "$columnName: ${cursor.getString(cursor.getColumnIndexOrThrow(columnName))}"
                }
                println("Row: ${rowValues.joinToString()}")
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()
    }

    @Test
    fun testBookingsTableHasData() {
        val db = dbHelper.readableDatabase
        val cursor = db.query("Bookings", null, null, null, null, null, null)

        // Assert that there is data in the Bookings table
        assertTrue("No data found in the Bookings table", cursor.count > 0)

        println("Number of rows in Bookings table: ${cursor.count}")

        if (cursor.count > 0) {
            cursor.moveToFirst()

            val columnNames = cursor.columnNames
            println("Column Names in Bookings Table: ${columnNames.joinToString()}")

            println("Data in Bookings Table:")
            do {
                val bookingIdIndex = cursor.getColumnIndex("bookingId")
                if (bookingIdIndex == -1) {
                    throw IllegalStateException("Column 'bookingId' does not exist in the Bookings table.")
                }
                val bookingId = cursor.getLong(bookingIdIndex)

                // Assert that bookingId is valid
                assertTrue("bookingId should not be null or 0", bookingId != 0L)

                val rowValues = columnNames.map { columnName ->
                    "$columnName: ${cursor.getString(cursor.getColumnIndexOrThrow(columnName))}"
                }
                println("Row: ${rowValues.joinToString()}")
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()
    }

    @Test
    fun testMechanicExists() {
        val db = dbHelper.readableDatabase
        val cursor = db.query("Mechanics", null, null, null, null, null, null)

        // Check if there are rows in the Mechanics table
        assertTrue("No data found in the Mechanics table", cursor.count > 0)

        println("Number of rows in Mechanics table: ${cursor.count}")

        if (cursor.count > 0) {
            cursor.moveToFirst()

            val columnNames = cursor.columnNames
            println("Column Names in Mechanics Table: ${columnNames.joinToString()}")

            println("Data in Mechanics Table:")
            do {
                val mechanicIdIndex = cursor.getColumnIndex("mechanicId")
                val mechanicId = cursor.getLong(mechanicIdIndex)

                // Assert that mechanicId is valid
                assertTrue("mechanicId should not be null or 0", mechanicId != 0L)

                val rowValues = columnNames.map { columnName ->
                    "$columnName: ${cursor.getString(cursor.getColumnIndexOrThrow(columnName))}"
                }
                println("Row: ${rowValues.joinToString()}")
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()
    }

    @Test
    fun testCustomerExists() {
        val db = dbHelper.readableDatabase
        val cursor = db.query("Customers", null, null, null, null, null, null)

        // Check if there are rows in the Customers table
        assertTrue("No data found in the Customers table", cursor.count > 0)

        println("Number of rows in Customers table: ${cursor.count}")

        if (cursor.count > 0) {
            cursor.moveToFirst()

            val columnNames = cursor.columnNames
            println("Column Names in Customers Table: ${columnNames.joinToString()}")

            println("Data in Customers Table:")
            do {
                val customerIdIndex = cursor.getColumnIndex("customerId")
                val customerId = cursor.getLong(customerIdIndex)

                // Assert that customerId is valid
                assertTrue("customerId should not be null or 0", customerId != 0L)

                val rowValues = columnNames.map { columnName ->
                    "$columnName: ${cursor.getString(cursor.getColumnIndexOrThrow(columnName))}"
                }
                println("Row: ${rowValues.joinToString()}")
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()
    }

    @Test
    fun testVehicleExists() {
        val db = dbHelper.readableDatabase
        val cursor = db.query("Vehicles", null, null, null, null, null, null)

        // Check if there are rows in the Vehicles table
        assertTrue("No data found in the Vehicles table", cursor.count > 0)

        println("Number of rows in Vehicles table: ${cursor.count}")

        if (cursor.count > 0) {
            cursor.moveToFirst()

            val columnNames = cursor.columnNames
            println("Column Names in Vehicles Table: ${columnNames.joinToString()}")

            println("Data in Vehicles Table:")
            do {
                val vehicleIdIndex = cursor.getColumnIndex("vehicleId")
                val vehicleId = cursor.getLong(vehicleIdIndex)

                // Assert that vehicleId is valid
                assertTrue("vehicleId should not be null or 0", vehicleId != 0L)

                val rowValues = columnNames.map { columnName ->
                    "$columnName: ${cursor.getString(cursor.getColumnIndexOrThrow(columnName))}"
                }
                println("Row: ${rowValues.joinToString()}")
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()
    }

    @Test
    fun testServiceExists() {
        val db = dbHelper.readableDatabase
        val cursor = db.query("Services", null, null, null, null, null, null)

        // Check if there are rows in the Services table
        assertTrue("No data found in the Services table", cursor.count > 0)

        println("Number of rows in Services table: ${cursor.count}")

        if (cursor.count > 0) {
            cursor.moveToFirst()

            val columnNames = cursor.columnNames
            println("Column Names in Services Table: ${columnNames.joinToString()}")

            println("Data in Services Table:")
            do {
                val serviceIdIndex = cursor.getColumnIndex("serviceId")
                val serviceId = cursor.getLong(serviceIdIndex)

                // Assert that serviceId is valid
                assertTrue("serviceId should not be null or 0", serviceId != 0L)

                val rowValues = columnNames.map { columnName ->
                    "$columnName: ${cursor.getString(cursor.getColumnIndexOrThrow(columnName))}"
                }
                println("Row: ${rowValues.joinToString()}")
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()
    }








}


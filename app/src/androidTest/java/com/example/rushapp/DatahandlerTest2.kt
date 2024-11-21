package com.example.rushapp

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import androidx.test.core.app.ApplicationProvider
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class DatahandlerTest2 {

    private lateinit var dbHelper: DatabaseHelper
    private lateinit var db: SQLiteDatabase
    private lateinit var dataHandler: DataHandler

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        dbHelper = DatabaseHelper(context)
        db = dbHelper.writableDatabase
        dataHandler = DataHandler(context)

        // Clear the database before each test
        db.execSQL("DELETE FROM InvoiceItems")
        db.execSQL("DELETE FROM Invoices")
    }

    @After
    fun teardown() {
        db.close()
    }

    @Test
    fun testInsertInvoiceWithItems() {
        // Test data
        val serviceId = 1L
        val totalCost = 300.00
        val invoiceItems = listOf(
            "Oil Change" to 100.00,
            "Brake Pads" to 200.00
        )

        // Insert an invoice with items
        val invoiceId = dataHandler.insertInvoiceWithItems(db, serviceId, totalCost, invoiceItems)

        // Verify invoice insertion
        val cursor = db.query(
            "Invoices",
            arrayOf("invoiceId", "serviceId", "totalCost"),
            "invoiceId = ?",
            arrayOf(invoiceId.toString()),
            null,
            null,
            null
        )

        assertEquals(true, cursor.moveToFirst())
        assertEquals(serviceId, cursor.getLong(cursor.getColumnIndexOrThrow("serviceId")))
        assertEquals(totalCost, cursor.getDouble(cursor.getColumnIndexOrThrow("totalCost")), 0.0)
        cursor.close()

        // Verify invoice items insertion
        val itemsCursor = db.query(
            "InvoiceItems",
            arrayOf("invoiceId", "itemName", "cost"),
            "invoiceId = ?",
            arrayOf(invoiceId.toString()),
            null,
            null,
            null
        )

        val insertedItems = mutableListOf<Pair<String, Double>>()
        while (itemsCursor.moveToNext()) {
            val itemName = itemsCursor.getString(itemsCursor.getColumnIndexOrThrow("itemName"))
            val cost = itemsCursor.getDouble(itemsCursor.getColumnIndexOrThrow("cost"))
            insertedItems.add(itemName to cost)
        }
        itemsCursor.close()

        assertEquals(invoiceItems.size, insertedItems.size)
        assertEquals(invoiceItems, insertedItems)
    }

    @Test
    fun testGetInvoiceIdByServiceId() {
        // Insert a mock invoice
        val serviceId = 2L
        val totalCost = 150.00
        val invoiceId = dataHandler.insertInvoiceWithItems(db, serviceId, totalCost, null)

        // Retrieve the invoice ID by service ID
        val retrievedInvoiceId = dataHandler.getInvoiceIdByServiceId(db, serviceId)

        // Verify that the correct invoice ID is retrieved
        assertNotNull(retrievedInvoiceId)
        assertEquals(invoiceId, retrievedInvoiceId)
    }

    @Test
    fun testInsertNewUserRetrievesOrCreatesCustomerId() {
        val name = "John Doe"
        val email = "johndoe@example.com"
        val password = "password123"
        val phone = "123-456-7890"
        val userType = "Customer"

        // Ensure the Customers table is empty
        db.execSQL("DELETE FROM Customers")

        // Insert a new user
        val isUserInserted = dataHandler.insertNewUser(name, email, password, phone, userType)

        // Verify that the user was inserted
        assertEquals(true, isUserInserted)

        // Check if the user is associated with the correct customerId
        val cursor = db.query(
            "Users",
            arrayOf("customerId"),
            "email = ?",
            arrayOf(email),
            null,
            null,
            null
        )

        assertEquals(true, cursor.moveToFirst())
        val customerId = cursor.getLong(cursor.getColumnIndexOrThrow("customerId"))
        assertNotNull(customerId)

        // Verify that the customer exists in the Customers table
        val customerCursor = db.query(
            "Customers",
            arrayOf("customerId", "name"),
            "customerId = ?",
            arrayOf(customerId.toString()),
            null,
            null,
            null
        )
        assertEquals(true, customerCursor.moveToFirst())
        assertEquals(name, customerCursor.getString(customerCursor.getColumnIndexOrThrow("name")))

        // Clean up
        customerCursor.close()
        cursor.close()
    }

    @Test
    fun testGetInvoicesByEmail() {
        // Use an existing email that already has invoices in the database
        val email = "alice@example.com"

        // Retrieve invoices by email
        val invoices = dataHandler.getInvoicesByEmail(db, email)

        // Verify that invoices are retrieved
        assertNotNull(invoices)
        assert(invoices.isNotEmpty()) { "No invoices found for the provided email: $email" }

        // Example: Verify the details of the first invoice
        val firstInvoice = invoices[0]

        // Check for expected keys in the result
        assert(firstInvoice.containsKey("invoiceId")) { "Missing invoiceId in the result" }
        assert(firstInvoice.containsKey("totalCost")) { "Missing totalCost in the result" }
        assert(firstInvoice.containsKey("serviceId")) { "Missing serviceId in the result" }
        assert(firstInvoice.containsKey("description")) { "Missing description in the result" }
        assert(firstInvoice.containsKey("date")) { "Missing date in the result" }
        assert(firstInvoice.containsKey("make")) { "Missing make in the result" }
        assert(firstInvoice.containsKey("model")) { "Missing model in the result" }
        assert(firstInvoice.containsKey("year")) { "Missing year in the result" }

        // Example: Log the results (optional)
        invoices.forEachIndexed { index, invoice ->
            println("Invoice $index: $invoice")
        }


    }
    @Test
    fun testInsertNewMechanic() {
        val testName = "Unit Test Mechanic"
        val testEmail = "unittestmechanic@example.com"
        val testPassword = "unittestpassword"
        val testPhone = "123-456-7890"
        val testWorkshopId: Long? = 1 // Replace with a valid workshop ID

        val success = dataHandler.insertNewMechanic(
            name = testName,
            email = testEmail,
            password = testPassword,
            phone = testPhone,
            workshopId = testWorkshopId
        )

        assertTrue("Mechanic should be inserted successfully", success)
    }

    @Test
    fun testGetAllServices() {
        val db = dataHandler.dbHelper.writableDatabase

        // Insert mock data for testing
        val mockServices = listOf(
            ContentValues().apply {
                put("mechanicId", 1L)
                put("vehicleId", 1L)
                put("date", "2024-11-20")
                put("serviceType", "Oil Change")
                put("description", "Full synthetic oil replacement")
            },
            ContentValues().apply {
                put("mechanicId", 2L)
                put("vehicleId", 2L)
                put("date", "2024-11-21")
                put("serviceType", "Brake Inspection")
                put("description", "Brake pad and rotor check")
            },
            ContentValues().apply {
                put("mechanicId", 3L)
                put("vehicleId", 3L)
                put("date", "2024-11-22")
                put("serviceType", "Tire Rotation")
                put("description", "Rotate tires for even wear")
            }
        )

        // Insert the mock services into the database
        mockServices.forEach { service ->
            db.insert("Services", null, service)
        }

        // Fetch all services using the method under test
        val allServices = dataHandler.getAllServices(db)

        // Verify the results
        assert(allServices.isNotEmpty()) { "Services list should not be empty" }
        assert(allServices.size == mockServices.size) { "Services list size should match the number of inserted services" }

        // Check individual services
        assert(allServices[0].serviceType == "Oil Change") { "First service type should be 'Oil Change'" }
        assert(allServices[1].serviceType == "Brake Inspection") { "Second service type should be 'Brake Inspection'" }
        assert(allServices[2].serviceType == "Tire Rotation") { "Third service type should be 'Tire Rotation'" }

        assert(allServices[0].serviceDescription == "Full synthetic oil replacement") { "First service description should match" }
        assert(allServices[1].serviceDescription == "Brake pad and rotor check") { "Second service description should match" }
        assert(allServices[2].serviceDescription == "Rotate tires for even wear") { "Third service description should match" }

        // Clean up test data
        db.delete("Services", null, null)
        db.close()
    }








}

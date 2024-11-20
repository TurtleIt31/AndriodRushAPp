package com.example.rushapp

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import androidx.test.core.app.ApplicationProvider
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
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
}

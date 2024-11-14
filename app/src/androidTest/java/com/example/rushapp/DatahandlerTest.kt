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
        dataHandler.populateSampleData()
    }


    @Test
    fun testDataInsertion() {
        val db = dbHelper.readableDatabase
        val cursor = db.query("Users", null, null, null, null, null, null)

        // Check if data is present in the Users table
        if (cursor.count > 0) {
            cursor.moveToFirst() // Move to the first row

            // Print out column names
            val columnNames = cursor.columnNames
            println("Column Names in Users Table: ${columnNames.joinToString()}")

            // Iterate through all rows and print column values
            do {
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
}

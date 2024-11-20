import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.rushapp.DatabaseHelper
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DatabaseTests {

    private lateinit var dbHelper: DatabaseHelper
    private lateinit var db: SQLiteDatabase

    @Before
    fun setUp() {
        // Initialize the DatabaseHelper and the database
        val context = ApplicationProvider.getApplicationContext<Context>()
        dbHelper = DatabaseHelper(context)
        db = dbHelper.readableDatabase // Properly initialize the class-level db
    }

    @Test
    fun testBookingsDataExists() {
        // Query the Bookings table
        val cursor = db.query("Bookings", null, null, null, null, null, null)

        // Check if the Bookings table has any rows
        if (cursor.count > 0) {
            Log.d("DatabaseTests", "Found ${cursor.count} rows in the Bookings table")

            cursor.moveToFirst()

            // Retrieve and log the column names
            val columnNames = cursor.columnNames
            Log.d("DatabaseTests", "Column Names in Bookings Table: ${columnNames.joinToString()}")

            // Iterate through all rows and validate the data
            do {
                // Validate that bookingId and customerId are valid
                val bookingIdIndex = cursor.getColumnIndexOrThrow("bookingId")
                val customerIdIndex = cursor.getColumnIndexOrThrow("customerId")

                val bookingId = cursor.getLong(bookingIdIndex)
                val customerId = cursor.getLong(customerIdIndex)

                assertTrue("bookingId should not be null or 0", bookingId != 0L)
                assertTrue("customerId should not be null or 0", customerId != 0L)

                // Log row values for debugging
                val rowValues = columnNames.map { columnName ->
                    "$columnName: ${cursor.getString(cursor.getColumnIndexOrThrow(columnName))}"
                }
                Log.d("DatabaseTests", "Row: ${rowValues.joinToString()}")

            } while (cursor.moveToNext())
        } else {
            Log.d("DatabaseTests", "No data found in the Bookings table")
            assertFalse("Bookings table should not be empty for this test", true)
        }

        // Close the cursor
        cursor.close()
    }


    @Test
    fun testJoinQuery() {
        val query = """
        SELECT b.*, u.userId, u.email
        FROM Bookings b
        INNER JOIN Users u ON b.customerId = u.userId
    """
        val cursor = db.rawQuery(query, null)

        // Assert that the query returns rows
        if (cursor.moveToFirst()) {
            Log.d("DatabaseTests", "Join query returned ${cursor.count} rows")
            do {
                // Log and assert important columns
                val bookingId = cursor.getLong(cursor.getColumnIndexOrThrow("bookingId"))
                val userId = cursor.getLong(cursor.getColumnIndexOrThrow("userId"))
                val customerId = cursor.getLong(cursor.getColumnIndexOrThrow("customerId"))
                val email = cursor.getString(cursor.getColumnIndexOrThrow("email"))

                Log.d("DatabaseTests", "BookingId: $bookingId, CustomerId: $customerId, UserId: $userId, Email: $email")

                // Ensure the customerId matches the userId for the join
                assertTrue("customerId should match userId for the join", customerId == userId)

            } while (cursor.moveToNext())
        } else {
            Log.d("DatabaseTests", "Join query returned no rows")
            assertFalse("Join query should return results", true)
        }

        cursor.close()
    }

}

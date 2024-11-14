import android.content.ContentValues
import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.rushapp.DatabaseHelper
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.Assert.*

class DatabaseHelperTest {

    private lateinit var dbHelper: DatabaseHelper
    private lateinit var context: Context

    @Before
    fun setup() {
        context = ApplicationProvider.getApplicationContext()
        dbHelper = DatabaseHelper(context)
    }

    @After
    fun tearDown() {
        dbHelper.close()
        context.deleteDatabase("RushApp.db")
    }

    @Test
    fun testDatabaseCreation() {
        val db = dbHelper.readableDatabase
        assertTrue(db.isOpen)
        // Add more assertions as needed to validate table creation, etc.
    }

    @Test
    fun testInsertAndRetrieveData() {
        val db = dbHelper.writableDatabase

        // Insert sample data into Workshops table
        val values = ContentValues().apply {
            put("name", "Test Workshop")
            put("address", "123 Test Street")
        }
        val id = db.insert("Workshops", null, values)
        assertTrue(id != -1L)

        // Retrieve data and validate
        val cursor = db.query("Workshops", null, "workshopId = ?", arrayOf(id.toString()), null, null, null)
        assertTrue(cursor.moveToFirst())
        assertEquals("Test Workshop", cursor.getString(cursor.getColumnIndexOrThrow("name")))
        cursor.close()
    }
}

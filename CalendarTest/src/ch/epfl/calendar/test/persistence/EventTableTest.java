package ch.epfl.calendar.test.persistence;

import junit.framework.TestCase;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;
import ch.epfl.calendar.App;
import ch.epfl.calendar.persistence.EventTable;

/**
 *
 * @author lweingart
 *
 */
public class EventTableTest extends TestCase {

    private SQLiteDatabase mDb;

    @Override
    public void setUp() throws Exception {
        super.setUp();

        App.setDBHelper("calendar_test.db");
        mDb = App.getDBHelper().getReadableDatabase();
    }

    @Override
    public void tearDown() throws Exception {
        super.tearDown();

        mDb.close();
    }

    // For deeper tests,
    // @see ch.epfl.calendar.persistence.tests.DBHelperTest#testTableCreation()
    public void testOnCreate() {
        try {
            EventTable.onCreate(mDb);
            String tableName = SQLiteDatabase
                    .findEditTable(EventTable.TABLE_NAME_EVENT);
            Log.i("Database contains table : ", tableName);
            fail("Should throw an SQLiteException");
        } catch (SQLiteException e) {
            // success
        	assertNotNull(e);
        }
    }

    public void testOnUpgrade() {
    	try {
    		EventTable.onUpgrade(mDb, 1, 2);
    		String tableName = SQLiteDatabase
                    .findEditTable(EventTable.TABLE_NAME_EVENT);
            Log.i("Database contains table : ", tableName);
            assertTrue(tableName.equals(EventTable.TABLE_NAME_EVENT));
        } catch (SQLiteException e) {
            // fail
        	fail("Shouldn't have thrown an SQLiteException");
    	}
    }
}

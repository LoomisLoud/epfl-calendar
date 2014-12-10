package ch.epfl.calendar.persistence.tests;

import junit.framework.TestCase;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;
import ch.epfl.calendar.App;
import ch.epfl.calendar.persistence.PeriodTable;

/**
 *
 * @author lweingart
 *
 */
public class PeriodTableTest extends TestCase {

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
            PeriodTable.onCreate(mDb);
            String tableName = SQLiteDatabase
                    .findEditTable(PeriodTable.TABLE_NAME_PERIOD);
            Log.i("Database contains table : ", tableName);
            fail("Should throw an SQLiteException");
        } catch (SQLiteException e) {
            // success
        	assertNotNull(e);
        }
    }

    public void testOnUpgrade() {
    	try {
    		PeriodTable.onUpgrade(mDb, 1, 2);
    		String tableName = SQLiteDatabase
                    .findEditTable(PeriodTable.TABLE_NAME_PERIOD);
            Log.i("Database contains table : ", tableName);
            assertTrue(tableName.equals(PeriodTable.TABLE_NAME_PERIOD));
        } catch (SQLiteException e) {
            // fail
        	fail("Shouldn't have thrown an SQLiteException");
    	}
    }
}

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
        }
    }
}

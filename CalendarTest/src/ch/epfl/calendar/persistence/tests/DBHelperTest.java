package ch.epfl.calendar.persistence.tests;

import junit.framework.TestCase;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import ch.epfl.calendar.App;
import ch.epfl.calendar.persistence.CourseTable;
import ch.epfl.calendar.persistence.EventTable;
import ch.epfl.calendar.persistence.PeriodTable;

/**
 * 
 * @author lweingart
 * 
 */
public class DBHelperTest extends TestCase {

    private SQLiteDatabase mDb;

    // @Override
    // public void setUp() {
    // RenamingDelegatingContext context = new
    // RenamingDelegatingContext(App.getAppContext(), "test_db.db");
    // mDBHelper = new DBHelper(context, "calendar_test.db");
    // }

    @Override
    public void setUp() {
        try {
            super.setUp();
        } catch (Exception e) {
            e.printStackTrace();
        }

        App.setDBHelper("calendar_test.db");
        mDb = App.getDBHelper().getReadableDatabase();
    }

    public void testTablesCreation() {
        String tableName = SQLiteDatabase
                .findEditTable(CourseTable.TABLE_NAME_COURSE);
        assertEquals(tableName, CourseTable.TABLE_NAME_COURSE);
        Log.i("DB contains table : ", tableName);
        tableName = SQLiteDatabase.findEditTable(PeriodTable.TABLE_NAME_PERIOD);
        assertEquals(tableName, PeriodTable.TABLE_NAME_PERIOD);
        Log.i("DB contains table : ", tableName);
        tableName = SQLiteDatabase.findEditTable(EventTable.TABLE_NAME_EVENT);
        assertEquals(tableName, EventTable.TABLE_NAME_EVENT);
        Log.i("DB contains table : ", tableName);
    }
}

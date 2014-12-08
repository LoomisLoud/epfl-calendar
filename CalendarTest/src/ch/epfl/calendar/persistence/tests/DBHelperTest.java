package ch.epfl.calendar.persistence.tests;

import java.util.ArrayList;
import java.util.Arrays;

import junit.framework.TestCase;
import android.database.Cursor;
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
    private static final int NB_COL_COURSE_TABLE = 5;
    private static final int NB_COL_PERIOD_TABLE = 6;
    private static final int NB_COL_EVENT_TABLE = 8;

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

    public void testTablesCreation() {
        String tableName = SQLiteDatabase
                .findEditTable(CourseTable.TABLE_NAME_COURSE);

        Log.i("DB contains table : ", tableName);
        assertEquals(tableName, CourseTable.TABLE_NAME_COURSE);
        tableName = SQLiteDatabase.findEditTable(PeriodTable.TABLE_NAME_PERIOD);
        Log.i("DB contains table : ", tableName);
        assertEquals(tableName, PeriodTable.TABLE_NAME_PERIOD);
        tableName = SQLiteDatabase.findEditTable(EventTable.TABLE_NAME_EVENT);
        Log.i("DB contains table : ", tableName);
        assertEquals(tableName, EventTable.TABLE_NAME_EVENT);

        String courses = CourseTable.TABLE_NAME_COURSE;
        Cursor cursor = mDb.rawQuery("SELECT * FROM " + courses + " LIMIT 1", null);
        String[] coursesCols = cursor.getColumnNames();
        ArrayList<String> columns = new ArrayList<String>(Arrays.asList(coursesCols));
        Log.i("Courses table columns : ", columns.toString());

        assertTrue(columns.contains(CourseTable.COLUMN_NAME_CODE));
        assertTrue(columns.contains(CourseTable.COLUMN_NAME_CREDITS));
        assertTrue(columns.contains(CourseTable.COLUMN_NAME_DESCRIPTION));
        assertTrue(columns.contains(CourseTable.COLUMN_NAME_NAME));
        assertTrue(columns.contains(CourseTable.COLUMN_NAME_TEACHER));

        assertTrue(columns.size() == NB_COL_COURSE_TABLE);

        String periods = PeriodTable.TABLE_NAME_PERIOD;
        cursor = mDb.rawQuery("SELECT * FROM " + periods + " LIMIT 1", null);
        String[] periodsCols = cursor.getColumnNames();
        columns = new ArrayList<String>(Arrays.asList(periodsCols));
        Log.i("Periods table columns : ", columns.toString());

        assertTrue(columns.contains(PeriodTable.COLUMN_NAME_ID));
        assertTrue(columns.contains(PeriodTable.COLUMN_NAME_COURSE));
        assertTrue(columns.contains(PeriodTable.COLUMN_NAME_STARTDATE));
        assertTrue(columns.contains(PeriodTable.COLUMN_NAME_ENDDATE));
        assertTrue(columns.contains(PeriodTable.COLUMN_NAME_ROOMS));
        assertTrue(columns.contains(PeriodTable.COLUMN_NAME_TYPE));

        assertTrue(columns.size() == NB_COL_PERIOD_TABLE);

        String events = EventTable.TABLE_NAME_EVENT;
        cursor = mDb.rawQuery("SELECT * FROM " + events + " LIMIT 1", null);
        String[] eventsCols = cursor.getColumnNames();
        columns = new ArrayList<String>(Arrays.asList(eventsCols));
        Log.i("Events table columns : ", columns.toString());

        assertTrue(columns.contains(EventTable.COLUMN_NAME_ID));
        assertTrue(columns.contains(EventTable.COLUMN_NAME_COURSE));
        assertTrue(columns.contains(EventTable.COLUMN_NAME_DESCRIPTION));
        assertTrue(columns.contains(EventTable.COLUMN_NAME_STARTDATE));
        assertTrue(columns.contains(EventTable.COLUMN_NAME_ENDDATE));
        assertTrue(columns.contains(EventTable.COLUMN_NAME_IS_BLOCK));
        assertTrue(columns.contains(EventTable.COLUMN_NAME_NAME));
        assertTrue(columns.contains(EventTable.COLUMN_NAME_TYPE));

        assertTrue(columns.size() == NB_COL_EVENT_TABLE);
    }
}

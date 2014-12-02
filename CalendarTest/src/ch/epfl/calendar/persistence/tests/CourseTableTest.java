package ch.epfl.calendar.persistence.tests;

import junit.framework.TestCase;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;
import ch.epfl.calendar.App;
import ch.epfl.calendar.persistence.CourseTable;

/**
 * @author lweingart
 *
 */
public class CourseTableTest extends TestCase {

	private SQLiteDatabase mDb;
//	private DBHelper mDBHelper;

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

	public void testOnCreate() {
		try {
			CourseTable.onCreate(mDb);
			String tableName = SQLiteDatabase.findEditTable(CourseTable.TABLE_NAME_COURSE);
			Log.i("Database contains table : ", tableName);
			fail("Should throw an sqlException");
		} catch (SQLiteException e) {
			//success
		}
	}

}

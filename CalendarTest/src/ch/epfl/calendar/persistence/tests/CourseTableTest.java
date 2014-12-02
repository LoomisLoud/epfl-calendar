package ch.epfl.calendar.persistence.tests;

import junit.framework.TestCase;
import android.database.sqlite.SQLiteDatabase;
import ch.epfl.calendar.App;
import ch.epfl.calendar.persistence.CourseTable;

/**
 * @author lweingart
 *
 */
public class CourseTableTest extends TestCase {

	private SQLiteDatabase mDb;

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
		CourseTable.onCreate(mDb);
	}

}

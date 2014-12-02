package ch.epfl.calendar.persistence.tests;

import android.database.sqlite.SQLiteDatabase;
import junit.framework.TestCase;
import ch.epfl.calendar.App;

/**
 *
 * @author lweingart
 *
 */
public class CourseTableTest extends TestCase {

	@Override
	public void setUp() throws Exception {
        super.setUp();

        App.setDBHelper("calendar_test.db");
        SQLiteDatabase db = App.getDBHelper().getReadableDatabase();
	}
}

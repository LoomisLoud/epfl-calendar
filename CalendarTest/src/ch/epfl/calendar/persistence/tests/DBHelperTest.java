package ch.epfl.calendar.persistence.tests;

import junit.framework.TestCase;
import android.database.sqlite.SQLiteDatabase;
import ch.epfl.calendar.App;
import ch.epfl.calendar.persistence.DBHelper;

/**
 *
 * @author lweingart
 *
 */
public class DBHelperTest extends TestCase {

	private SQLiteDatabase mDb;
	private DBHelper mDBHelper;

//	@Override
//	public void setUp() {
//		RenamingDelegatingContext context = new RenamingDelegatingContext(App.getAppContext(), "test_db.db");
//		mDBHelper = new DBHelper(context, "calendar_test.db");
//	}

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

	public void testAddEntry() {
		// TODO some tests
	}

	@Override
	public void tearDown() throws Exception {
		mDBHelper.close();
		super.tearDown();
	}
}

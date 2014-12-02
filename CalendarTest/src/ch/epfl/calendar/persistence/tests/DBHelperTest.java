package ch.epfl.calendar.persistence.tests;

import junit.framework.TestCase;
import android.test.RenamingDelegatingContext;
import ch.epfl.calendar.App;
import ch.epfl.calendar.persistence.DBHelper;

/**
 *
 * @author lweingart
 *
 */
public class DBHelperTest extends TestCase {

	private DBHelper mDBHelper;

	@Override
	public void setUp() {
		RenamingDelegatingContext context = new RenamingDelegatingContext(App.getAppContext(), "test_db.db");
		mDBHelper = new DBHelper(context, "test_db.db");
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

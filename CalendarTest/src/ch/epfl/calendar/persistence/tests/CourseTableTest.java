package ch.epfl.calendar.persistence.tests;

import junit.framework.TestCase;
import ch.epfl.calendar.App;

/**
 *
 * @author lweingart
 *
 */
public class CourseTableTest extends TestCase {

	@Override
	public void setUp() {
        super.setUp();

        App.setDBHelper("calendar_test.db");
        getInstrumentation().getTargetContext().deleteDatabase(
                App.getDBHelper().getDatabaseName());
	}
}

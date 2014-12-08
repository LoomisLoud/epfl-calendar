/**
 * 
 */
package ch.epfl.calendar.display.tests;

import java.util.ArrayList;
import java.util.List;

import android.test.ActivityInstrumentationTestCase2;
import ch.epfl.calendar.App;
import ch.epfl.calendar.data.Course;
import ch.epfl.calendar.data.Period;
import ch.epfl.calendar.display.EventListActivity;
import ch.epfl.calendar.persistence.DBQuester;
import ch.epfl.calendar.testing.utils.MockActivity;

/**
 * @author AblionGE
 * 
 */
public class EventListActivityTest extends
        ActivityInstrumentationTestCase2<EventListActivity> {

    private DBQuester mDB;
    private EventListActivity mActivity;
    private MockActivity mMockActivity;
    private List<Course> mCourses;

    private static final int SLEEP_TIME = 250;

    public EventListActivityTest() {
        super(EventListActivity.class);
    }

    /*
     * (non-Javadoc)
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();

        /*
         * SUPER DUPER IMPORTANT : class this next line in setUp in every test
         * class that need access to the database !
         */
        App.setDBHelper("calendar_test.db");
        getInstrumentation().getTargetContext().deleteDatabase(
                App.getDBHelper().getDatabaseName());

        mDB = new DBQuester();

        mDB.deleteAllTables();
        mDB.createTables();

        createCourses();
        mMockActivity = new MockActivity();
        App.setActionBar(mMockActivity);
        mMockActivity.setUdpateData(mMockActivity);
        mDB.storeCourses(mCourses);
        waitOnInsertionInDB();

        mActivity = getActivity();

        // We need to set up which activity is the current one (needed by
        // AsyncTask to be able to use callback functions
        App.setActionBar(mActivity);
        mActivity.setUdpateData(mActivity);
//        mDB.storeCourses(mCourses);
//        waitOnInsertionInDB();
    }

    /*
     * (non-Javadoc)
     * @see junit.framework.TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test method for
     * {@link ch.epfl.calendar.display.EventListActivity#switchToAddBlockActivity()}
     * .
     */
    public final void testSwitchToAddBlockActivity() {
        // fail("Not yet implemented"); // TODO
    }

    /**
     * Test method for
     * {@link ch.epfl.calendar.display.EventListActivity#switchToAddEventsActivity()}
     * .
     */
    public final void testSwitchToAddEventsActivity() {
        // fail("Not yet implemented"); // TODO
    }

    /**
     * Test method for
     * {@link ch.epfl.calendar.display.EventListActivity#updateData()}. (Test
     * onResume())
     */
    public final void testUpdateData() {
        // fail("Not yet implemented"); // TODO
    }

    public final void testSort() {

    }

    public final void testStringToPeriodType() {

    }

    public final void testRemovePastEvents() {

    }

    public final void testEventToEventForList() {

    }

    public final void testCreateAdapter() {

    }

    private void createCourses() throws Exception {
        List<String> period1Course1Rooms = new ArrayList<String>();
        List<String> period2Course1Rooms = new ArrayList<String>();
        period1Course1Rooms.add("GCA 331");
        period1Course1Rooms.add("CO2");
        period2Course1Rooms.add("INF1");
        period2Course1Rooms.add("INF2");
        Period period1Course1 = new Period("Lecture", "27.11.2034 08:00",
                "27.11.2034 10:00", period1Course1Rooms, "1");
        Period period2Course1 = new Period("Exercise", "28.11.2034 08:00",
                "28.11.2034 10:00", period2Course1Rooms, "2");
        ArrayList<Period> periodsCourse1 = new ArrayList<Period>();
        periodsCourse1.add(period1Course1);
        periodsCourse1.add(period2Course1);
        Course course1 = new Course("TestCourse1", periodsCourse1,
                "Pr. Testpr1", 200, "CS-321", "awesome course", null);

        List<String> period1Course2Rooms = new ArrayList<String>();
        List<String> period2Course2Rooms = new ArrayList<String>();
        period1Course2Rooms.add("GCB 332");
        period1Course2Rooms.add("INF119");
        period2Course2Rooms.add("INM202");
        period2Course2Rooms.add("INM203");
        Period period1Course2 = new Period("Lecture", "30.11.2034 08:00",
                "30.11.2034 10:00", period1Course2Rooms, "3");
        Period period2Course2 = new Period("Exercise", "02.11.2034 08:00",
                "02.11.2034 10:00", period2Course2Rooms, "4");
        ArrayList<Period> periodsCourse2 = new ArrayList<Period>();
        periodsCourse2.add(period1Course2);
        periodsCourse2.add(period2Course2);
        Course course2 = new Course("TestCourse2", periodsCourse2,
                "Pr. Testpr2", 5, "CS-000", "cool course", null);

        mCourses = new ArrayList<Course>();
        mCourses.add(course1);
        mCourses.add(course2);
    }

    private void waitOnInsertionInDB() {
        while (mMockActivity.getNbOfAsyncTaskDB() > 0) {
            try {
                Thread.sleep(SLEEP_TIME);
            } catch (InterruptedException e) {
                e.printStackTrace();
                fail();
            }
        }
    }

}

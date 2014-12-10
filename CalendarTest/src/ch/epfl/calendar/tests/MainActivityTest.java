/**
 * 
 */
package ch.epfl.calendar.tests;

import java.util.ArrayList;
import java.util.List;

import android.test.ActivityInstrumentationTestCase2;
import ch.epfl.calendar.App;
import ch.epfl.calendar.MainActivity;
import ch.epfl.calendar.authentication.TequilaAuthenticationAPI;
import ch.epfl.calendar.data.Course;
import ch.epfl.calendar.data.Period;
import ch.epfl.calendar.persistence.DBQuester;

/**
 * @author AblionGE
 * 
 */
public class MainActivityTest extends
        ActivityInstrumentationTestCase2<MainActivity> {

    private MainActivity mActivity;
    private DBQuester mDB;

    private static final int SLEEP_TIME = 250;

    public MainActivityTest() {
        super(MainActivity.class);
    }

    /*
     * (non-Javadoc)
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();

        mActivity = getActivity();

        App.setDBHelper("calendar_test.db");
        // We need to set up which activity is the current one (needed by
        // AsyncTask to be able to use callback functions
        App.setActionBar(mActivity);
        mActivity.setUdpateData(mActivity);

        mDB = new DBQuester();

        mDB.deleteAllTables();
        mDB.createTables();

        populateTestDB();
    }

    /*
     * (non-Javadoc)
     * @see junit.framework.TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test method for {@link ch.epfl.calendar.MainActivity#onMonthChange()}.
     */
    public final void testOnMonthChange() {
        setUser();
    }

    /**
     * Test method for
     * {@link ch.epfl.calendar.MainActivity#onEventClick(ch.epfl.calendar.thirdParty.calendarViews.WeekViewEvent, android.graphics.RectF)}
     * .
     */
    public final void testOnEventClick() {
        setUser();
    }

    /**
     * Test method for
     * {@link ch.epfl.calendar.MainActivity#onEventLongPress(ch.epfl.calendar.thirdParty.calendarViews.WeekViewEvent, android.graphics.RectF)}
     * .
     */
    public final void testOnEventLongPress() {
        setUser();
    }

    /**
     * Test method for {@link ch.epfl.calendar.MainActivity#updateData()}.
     */
    public final void testUpdateData() {
        setUser();
    }

    private void populateTestDB() throws Exception {
        List<String> period1Course1Rooms = new ArrayList<String>();
        List<String> period2Course1Rooms = new ArrayList<String>();
        period1Course1Rooms.add("GCA 331");
        period1Course1Rooms.add("CO2");
        period2Course1Rooms.add("INF1");
        period2Course1Rooms.add("INF2");
        Period period1Course1 = new Period("Lecture", "27.11.2014 08:00",
                "27.11.2014 10:00", period1Course1Rooms, "1");
        Period period2Course1 = new Period("Exercise", "28.11.2014 08:00",
                "28.11.2014 10:00", period2Course1Rooms, "2");
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
        Period period1Course2 = new Period("Lecture", "30.11.2014 08:00",
                "30.11.2014 10:00", period1Course2Rooms, "3");
        Period period2Course2 = new Period("Exercise", "02.11.2014 08:00",
                "02.11.2014 10:00", period2Course2Rooms, "4");
        ArrayList<Period> periodsCourse2 = new ArrayList<Period>();
        periodsCourse2.add(period1Course2);
        periodsCourse2.add(period2Course2);
        Course course2 = new Course("TestCourse2", periodsCourse2,
                "Pr. Testpr2", 5, "CS-000", "cool course", null);

        mDB.storeCourse(course1);
        mDB.storeCourse(course2);
        waitOnInsertionInDB();
    }

    private void waitOnInsertionInDB() {
        while (mActivity.getNbOfAsyncTaskDB() > 0) {
            try {
                Thread.sleep(SLEEP_TIME);
            } catch (InterruptedException e) {
                e.printStackTrace();
                fail();
            }
        }
    }

    private void setUser() {
        App.setCurrentUsername("testUsername");
        // store the sessionID in the preferences
        TequilaAuthenticationAPI.getInstance().setSessionID(
                mActivity.getApplicationContext(), "sessionID");
        TequilaAuthenticationAPI.getInstance().setUsername(
                mActivity.getApplicationContext(), "testUsername");

        mActivity = getActivity();
    }

}

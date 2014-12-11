/**
 * 
 */
package ch.epfl.calendar.display.tests;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import ch.epfl.calendar.App;
import ch.epfl.calendar.data.Course;
import ch.epfl.calendar.data.Event;
import ch.epfl.calendar.data.Period;
import ch.epfl.calendar.display.AddEventBlockActivity;
import ch.epfl.calendar.persistence.DBQuester;
import ch.epfl.calendar.testing.utils.MockActivity;
import ch.epfl.calendar.testing.utils.Utils;

import com.google.android.apps.common.testing.testrunner.ActivityLifecycleMonitorRegistry;
import com.google.android.apps.common.testing.testrunner.Stage;
import com.google.common.collect.Iterables;

/**
 * @author AblionGE
 * 
 */
public class AddEventBlockActivityTest extends
        ActivityInstrumentationTestCase2<AddEventBlockActivity> {

    private DBQuester mDB;
    private AddEventBlockActivity mActivity;
    private MockActivity mMockActivity;
    private List<Course> mCourses;
    private List<Event> mEvents;

    private static final int SLEEP_TIME = 250;

    public AddEventBlockActivityTest() {
        super(AddEventBlockActivity.class);
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
        for (Event event : mEvents) {
            mDB.storeEvent(event);
        }
        waitOnInsertionInDB();
        DBQuester.close();

        mActivity = new AddEventBlockActivity();
    }

    /*
     * (non-Javadoc)
     * @see junit.framework.TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        try {
            Utils.pressBack(getCurrentActivity());
        } catch (Throwable e) {
            e.printStackTrace();
        }
        super.tearDown();
        getInstrumentation().getTargetContext().deleteDatabase(
                App.getDBHelper().getDatabaseName());
    }

    /**
     * Test method for
     * {@link ch.epfl.calendar.display.AddEventBlockActivity#finishActivity(android.view.View)}
     * .
     */
    public final void testFinishActivityView() {
        setActivity();
    }

    /**
     * Test method for
     * {@link ch.epfl.calendar.display.AddEventBlockActivity#updateData()}.
     */
    public final void testUpdateData() {
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
        Period period3Course1 = new Period("Project", "29.11.2010 08:00",
                "29.11.2010 10:00", period2Course1Rooms, "3");
        ArrayList<Period> periodsCourse1 = new ArrayList<Period>();
        periodsCourse1.add(period1Course1);
        periodsCourse1.add(period2Course1);
        periodsCourse1.add(period3Course1);
        Course course1 = new Course("TestCourse1", periodsCourse1,
                "Pr. Testpr1", 200, "CS-321", "awesome course", null);

        List<String> period1Course2Rooms = new ArrayList<String>();
        List<String> period2Course2Rooms = new ArrayList<String>();
        period1Course2Rooms.add("GCB 332");
        period1Course2Rooms.add("INF119");
        period2Course2Rooms.add("INM202");
        period2Course2Rooms.add("INM203");
        Period period1Course2 = new Period("Lecture", "27.11.2034 08:00",
                "27.11.2034 17:00", period1Course2Rooms, "4");
        Period period2Course2 = new Period("Exercise", "02.11.2034 08:00",
                "02.11.2034 10:00", period2Course2Rooms, "5");
        ArrayList<Period> periodsCourse2 = new ArrayList<Period>();
        periodsCourse2.add(period1Course2);
        periodsCourse2.add(period2Course2);
        Course course2 = new Course("TestCourse2", periodsCourse2,
                "Pr. Testpr2", 5, "CS-000", "cool course", null);

        mCourses = new ArrayList<Course>();
        mCourses.add(course1);
        mCourses.add(course2);

        // Add events
        Event event1 = new Event("event1", "27.11.2034 08:00",
                "27.11.2034 18:00", "exercises", App.NO_COURSE, "Event 1",
                false, DBQuester.NO_ID);
        Event event2 = new Event("event2", "28.11.2014 08:00",
                "28.11.2014 18:00", "project", App.NO_COURSE, "Event 2", false,
                DBQuester.NO_ID);

        mEvents = new ArrayList<Event>();
        mEvents.add(event1);
        mEvents.add(event2);
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

    private void setActivity() {
        // Here is creation of the intent used in onCreate.
        // If the Id's value needs to be changed, just move the 3 following
        // lines directly in the tests method BEFORE calling setActivity()
        Intent intent = new Intent();
        // TODO : Change values
        intent.putExtra("courseName", mCourses.get(0));
        intent.putExtra("position", 1);
        setActivityIntent(intent);

        mActivity = getActivity();

        // We need to set up which activity is the current one (needed by
        // AsyncTask to be able to use callback functions
        App.setActionBar(mActivity);
        mActivity.setUdpateData(mActivity);
    }

    public Activity getCurrentActivity() throws Throwable {
        getInstrumentation().waitForIdleSync();
        final Activity[] activity = new Activity[1];
        runTestOnUiThread(new Runnable() {
            @Override
            public void run() {
                java.util.Collection<Activity> activites = ActivityLifecycleMonitorRegistry
                        .getInstance().getActivitiesInStage(Stage.RESUMED);
                activity[0] = Iterables.getOnlyElement(activites);
            }
        });
        return activity[0];
    }

}

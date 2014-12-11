package ch.epfl.calendar.display.tests;

import static com.google.android.apps.common.testing.ui.espresso.Espresso.onView;
import static com.google.android.apps.common.testing.ui.espresso.action.ViewActions.click;
import static com.google.android.apps.common.testing.ui.espresso.assertion.ViewAssertions.matches;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.isDisplayed;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.withId;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import ch.epfl.calendar.App;
import ch.epfl.calendar.R;
import ch.epfl.calendar.data.Course;
import ch.epfl.calendar.data.Event;
import ch.epfl.calendar.data.Period;
import ch.epfl.calendar.display.CourseDetailsActivity;
import ch.epfl.calendar.persistence.DBQuester;
import ch.epfl.calendar.persistence.LocalDatabaseInterface;
import ch.epfl.calendar.testing.utils.MockActivity;
import ch.epfl.calendar.testing.utils.Utils;

import com.google.android.apps.common.testing.testrunner.ActivityLifecycleMonitorRegistry;
import com.google.android.apps.common.testing.testrunner.Stage;
import com.google.common.collect.Iterables;

/**
 * 
 * @author Enea Bell
 * 
 */
public class CourseDetailsActivityInstrumentationTest extends
        ActivityInstrumentationTestCase2<CourseDetailsActivity> {

    private CourseDetailsActivity mActivity;
    private MockActivity mMockActivity;
    private LocalDatabaseInterface mDB;
    private List<Course> mCourses;
    private List<Event> mEvents;
    private static final int SLEEP_TIME = 250;
    public static final String TEST = "test";

    public CourseDetailsActivityInstrumentationTest() {
        super(CourseDetailsActivity.class);
    }

    /*
     * (non-Javadoc)
     * 
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
        DBQuester.close();

        // mActivity = new CourseDetailsActivity();
    }

    /*
     * (non-Javadoc)
     * 
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

    public void testLaunchActivityNothingInIntent() {
        setActivity();
        assertNotNull(mActivity);
    }

    public void testLaunchActivityWithIntentCourseNotInDB() {
        setIntentActivityWithExtra("DAWG");
        onView(withId(R.id.courseName)).check(
                matches(withText("DAWG" + " not found in data base.")));
    }

    public void testLaunchActivityWithIntentCorrectCourseRetrievenFromDBAndShown() {
        setIntentActivityWithExtra(mCourses.get(0).getName());
        // test the course shown is the same as asked
        onView(withId(R.id.courseName)).check(
                matches(withText(mCourses.get(0).getName())));
    }

    public void testAllMainTextViewVisible() {
        setIntentActivityWithExtra(mCourses.get(0).getName());
        onView(withId(R.id.courseName)).check(matches(isDisplayed()));
        onView(withId(R.id.courseProfessor)).check(matches(isDisplayed()));
        onView(withId(R.id.courseCredits)).check(matches(isDisplayed()));
        onView(withId(R.id.coursePeriod)).check(matches(isDisplayed()));
        onView(withId(R.id.courseDescription)).check(matches(isDisplayed()));
    }

    public void testCourseWithNoDescription() {
        setIntentActivityWithExtra(mCourses.get(2).getName());
        // no text should be shown
        onView(withId(R.id.courseDescription)).check(matches(withText("")));
    }

    public void testSwitchActivityEvent() throws Throwable {
        setIntentActivityWithExtra(mCourses.get(0).getName());
        // current activity name
        Class<? extends Activity> oldActivity = getCurrentActivity().getClass();
        // click on event and see if we switched activity
        onView(withText(containsString(mEvents.get(0).getName()))).perform(
                click());
        // we should end on AddEventActivity so we check they're not the same if
        // it was successful
        assertNotSame(oldActivity, getCurrentActivity().getClass());
    }

    public void testCourseWithNoEventRelated() {
        // setActivity with extra of courseName to be retrieved
        setIntentActivityWithExtra(mCourses.get(1).getName());
        // check if eventRelated is displayed (shouldn't since no event are
        // inside)
        onView(withId(R.id.linkedEvents)).check(matches(not(isDisplayed())));
    }

    /**
     * SetActivity with extra of courseName to be retrieved
     * 
     * @param courseName
     */
    private void setIntentActivityWithExtra(String courseName) {
        // set intent to start activity
        Intent intent = new Intent();
        intent.putExtra("course", courseName);
        setActivityIntent(intent);
        // setActivity
        setActivity();
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

        Course course3 = new Course("TestCourse3", periodsCourse2,
                "Pr. Testpr3", 5, "CS-000", "", null);
        // Add events
        Event event1 = new Event("event1", "27.11.2034 08:00",
                "27.11.2034 18:00", "exercises", course1.getName(),
                "Description of event", false, 1082838);

        mEvents = new ArrayList<Event>();
        mEvents.add(event1);
        course1.setEvents(new ArrayList<Event>(mEvents));

        mCourses = new ArrayList<Course>();

        mCourses.add(course1);
        mCourses.add(course2);
        mCourses.add(course3);
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
        mActivity = getActivity();

        // We need to set up which activity is the current one (needed by
        // AsyncTask to be able to use callback functions
        App.setActionBar(mActivity);
        mActivity.setUdpateData(mActivity);
    }

    private Activity getCurrentActivity() throws Throwable {
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
        System.out.println("ACTIVITY " + activity[0].toString());
        return activity[0];
    }
}

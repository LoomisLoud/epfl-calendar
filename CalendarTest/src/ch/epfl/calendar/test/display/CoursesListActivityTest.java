/**
 *
 */
package ch.epfl.calendar.test.display;

import static com.google.android.apps.common.testing.ui.espresso.Espresso.onData;
import static com.google.android.apps.common.testing.ui.espresso.action.ViewActions.click;
import static com.google.android.apps.common.testing.ui.espresso.assertion.ViewAssertions.doesNotExist;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.test.ActivityInstrumentationTestCase2;
import ch.epfl.calendar.App;
import ch.epfl.calendar.R;
import ch.epfl.calendar.data.Course;
import ch.epfl.calendar.data.Period;
import ch.epfl.calendar.display.CoursesListActivity;
import ch.epfl.calendar.persistence.DBQuester;
import ch.epfl.calendar.persistence.LocalDatabaseInterface;
import ch.epfl.calendar.test.utils.MockActivity;
import ch.epfl.calendar.test.utils.Utils;

import com.google.android.apps.common.testing.testrunner.ActivityLifecycleMonitorRegistry;
import com.google.android.apps.common.testing.testrunner.Stage;
import com.google.common.collect.Iterables;

/**
 * @author gilbrechbuhler
 *
 */
public class CoursesListActivityTest extends
        ActivityInstrumentationTestCase2<CoursesListActivity> {

    private static final int N_LIST_VIEW_ELEMENTS = 2;

    private static final int SLEEP_TIME = 250;

    private List<Course> mCourses = new ArrayList<Course>();
    private CoursesListActivity mActivity;
    private MockActivity mMockActivity;
    private LocalDatabaseInterface mDB;
    public static final String TEST = "test";

    public CoursesListActivityTest() {
        super(CoursesListActivity.class);
    }

    @Override
	public void setUp() throws Exception {
        super.setUp();

        /*
         * SUPER DUPER IMPORTANT : class this next line in setUp in every test
         * class that need access to the database !
         */
        App.setDBHelper("calendar_test.db");
        getInstrumentation().getTargetContext().deleteDatabase(
                App.getDBHelper().getDatabaseName());

        mMockActivity = new MockActivity();
        App.setActionBar(mMockActivity);
        mMockActivity.setUdpateData(mMockActivity);

        mDB = new DBQuester();

        mDB.deleteAllTables();
        mDB.createTables();

        populateTestDB();
    }

    @Override
	public void tearDown() throws Exception {
        try {
            Utils.pressBack(getCurrentActivity());
        } catch (Throwable e) {
            e.printStackTrace();
        }
        super.tearDown();
    }

    public void testSizeListView() throws InterruptedException {
        getActivityOnTest();
        onData(is(instanceOf(HashMap.class)))
                // Every entry in the ListView is a HashMap
                .inAdapterView(withId(getIdByName("coursesListView")))
                .atPosition(N_LIST_VIEW_ELEMENTS - 1).perform(); // Empty
                                                                 // perform will
                                                                 // fail if no
                                                                 // element at
                                                                 // this
                                                                 // position
                                                                 // (index
                                                                 // starts at
                                                                 // 0)*/
    }

    public void testClickOnListView() {
        getActivityOnTest();
        onData(is(instanceOf(HashMap.class)))
                // Every entry in the ListView is a HashMap
                .inAdapterView(withId(getIdByName("coursesListView")))
                .atPosition(0).perform(click())
                // Check that activity has changed
                .check(doesNotExist());
    }

    public final void testGetCreditImage() throws NoSuchMethodException,
            IllegalAccessException, IllegalArgumentException,
            InvocationTargetException {
        Method getCreditImage;
        getCreditImage = (CoursesListActivity.class).getDeclaredMethod(
                "getCreditImage", new Class[] {
                    Course.class
                });
        getCreditImage.setAccessible(true);
        CoursesListActivity instance = new CoursesListActivity();

        List<Integer> drawables = new ArrayList<Integer>();
        drawables.add(R.drawable.zero);
        drawables.add(R.drawable.un);
        drawables.add(R.drawable.deux);
        drawables.add(R.drawable.trois);
        drawables.add(R.drawable.quatre);
        drawables.add(R.drawable.cinq);
        drawables.add(R.drawable.six);
        drawables.add(R.drawable.sept);
        drawables.add(R.drawable.huit);
        drawables.add(R.drawable.neuf);
        drawables.add(R.drawable.dix);
        drawables.add(R.drawable.onze);
        drawables.add(R.drawable.douze);
        drawables.add(R.drawable.zero);
        for (int i = 0; i < 14; i++) {
            Course course = new Course("TestCourse1", null, "Pr. Testpr1", i,
                    "CS-321", "awesome course", null);
            assertEquals(drawables.get(i),
                    getCreditImage.invoke(instance, course));
        }
    }

    private int getIdByName(String name) {
        Context context = getInstrumentation().getTargetContext();
        int result = context.getResources().getIdentifier(name, "id",
                context.getPackageName());
        assertTrue("id for name not found: " + name, result != 0);
        return result;
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
                "Pr. Testpr1", 12, "CS-321", "awesome course", null);

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

        mCourses.add(course1);
        mCourses.add(course2);

        mDB.storeCourse(course1);
        mDB.storeCourse(course2);

        waitOnInsertionInDB();
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

    public void getActivityOnTest() {
        mActivity = getActivity();

        // We need to set up which activity is the current one (needed by
        // AsyncTask to be able to use callback functions
        App.setActionBar(mActivity);
        mActivity.setUdpateData(mActivity);
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

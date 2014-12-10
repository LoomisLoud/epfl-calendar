/**
 * 
 */
package ch.epfl.calendar.display.tests;

import static com.google.android.apps.common.testing.ui.espresso.Espresso.onData;
import static com.google.android.apps.common.testing.ui.espresso.action.ViewActions.click;
import static com.google.android.apps.common.testing.ui.espresso.assertion.ViewAssertions.doesNotExist;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.ListView;
import ch.epfl.calendar.App;
import ch.epfl.calendar.R;
import ch.epfl.calendar.data.Course;
import ch.epfl.calendar.data.Period;
import ch.epfl.calendar.display.CoursesListActivity;
import ch.epfl.calendar.persistence.DBQuester;
import ch.epfl.calendar.persistence.LocalDatabaseInterface;

/**
 * @author gilbrechbuhler
 * 
 */
public class CoursesListActivityTest extends
        ActivityInstrumentationTestCase2<CoursesListActivity> {

    private static final int N_LIST_VIEW_ELEMENTS = 2;
    
    private List<Course> mCourses = new ArrayList<Course>();
    private CoursesListActivity activity;
    private LocalDatabaseInterface mDB;
    public static final String TEST = "test";

    public CoursesListActivityTest() {
        super(CoursesListActivity.class);
    }

    public void setUp() throws Exception {
        super.setUp();

        /*
         * SUPER DUPER IMPORTANT : class this next line in setUp in every test
         * class that need access to the database !
         */
        App.setDBHelper("calendar_test.db");
        getInstrumentation().getTargetContext().deleteDatabase(
                App.getDBHelper().getDatabaseName());

        activity = getActivity();

        // We need to set up which activity is the current one (needed by
        // AsyncTask to be able to use callback functions
        App.setActionBar(activity);
        activity.setUdpateData(activity);

        mDB = new DBQuester();

        mDB.deleteAllTables();
        mDB.createTables();

        populateTestDB();
    }

    public void tearDown() throws Exception {
        super.tearDown();
    }

    public void testSizeListView() throws InterruptedException {
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
        onData(is(instanceOf(HashMap.class)))
                // Every entry in the ListView is a HashMap
                .inAdapterView(withId(getIdByName("coursesListView")))
                .atPosition(0).perform(click())
                // Check that activity has changed
                .check(doesNotExist());

        // final ListView listView = (ListView) activity
        // .findViewById(R.id.coursesListView);
        // runTestOnUiThread(new Runnable() {
        //
        // @Override
        // public void run() {
        // listView.performItemClick(listView, 0, listView.getAdapter()
        // .getItemId(0));
        //
        // }
        // });
    }

    public void testCreditImage() throws Throwable {
        
        final ListView listView = (ListView) activity
                .findViewById(R.id.coursesListView);
        runTestOnUiThread(new Runnable() {
            
            @SuppressWarnings("unchecked")
            @Override
            public void run() {
                // List are not in the same size so index are switched
                assertEquals(((Map<String, String>) listView.getAdapter()
                        .getItem(0))
                        .get("credit_image"), Integer.toString(getCreditImage(mCourses.get(1))));
                
                assertEquals(((Map<String, String>) listView.getAdapter()
                        .getItem(1))
                        .get("credit_image"), Integer.toString(getCreditImage(mCourses.get(0))));
                
            }
        });
       

    }

    private int getIdByName(String name) {
        Context context = getInstrumentation().getTargetContext();
        int result = context.getResources().getIdentifier(name, "id",
                context.getPackageName());
        assertTrue("id for name not found: " + name, result != 0);
        return result;
    }
    
    private int getCreditImage(Course cours) {
        switch (cours.getCredits()) {
            case 1:
                return R.drawable.un;
            case 2:
                return R.drawable.deux;
            case 3:
                return R.drawable.trois;
            case 4:
                return R.drawable.quatre;
            case 5:
                return R.drawable.cinq;
            case 6:
                return R.drawable.six;
            case 7:
                return R.drawable.sept;
            case 8:
                return R.drawable.huit;
            case 9:
                return R.drawable.neuf;
            case 10:
                return R.drawable.dix;
            case 11:
                return R.drawable.onze;
            case 12:
                return R.drawable.douze;

            default:
                return R.drawable.zero;

        }
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
    }
}

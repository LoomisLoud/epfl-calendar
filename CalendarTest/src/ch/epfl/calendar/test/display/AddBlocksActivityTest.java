package ch.epfl.calendar.test.display;

import static com.google.android.apps.common.testing.ui.espresso.Espresso.onData;
import static com.google.android.apps.common.testing.ui.espresso.action.ViewActions.click;
import static com.google.android.apps.common.testing.ui.espresso.assertion.ViewAssertions.doesNotExist;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.ListView;
import android.widget.TextView;
import ch.epfl.calendar.App;
import ch.epfl.calendar.R;
import ch.epfl.calendar.data.Course;
import ch.epfl.calendar.data.Event;
import ch.epfl.calendar.data.Period;
import ch.epfl.calendar.data.PeriodType;
import ch.epfl.calendar.display.AddBlocksActivity;
import ch.epfl.calendar.display.AddEventBlockActivity;
import ch.epfl.calendar.persistence.DBQuester;
import ch.epfl.calendar.persistence.LocalDatabaseInterface;
import ch.epfl.calendar.test.utils.MockActivity;
import ch.epfl.calendar.test.utils.Utils;

import com.google.android.apps.common.testing.testrunner.ActivityLifecycleMonitorRegistry;
import com.google.android.apps.common.testing.testrunner.Stage;
import com.google.common.collect.Iterables;

/**
 * @author Romain
 *
 */
public class AddBlocksActivityTest extends ActivityInstrumentationTestCase2<AddBlocksActivity> {


    private static final int N_LIST_VIEW_ELEMENTS = 2;

    private static final int SLEEP_TIME = 250;

    private List<Course> mCourses = new ArrayList<Course>();
    private TextView mText;
    private ListView mList;
    private AddBlocksActivity mActivity;
    private MockActivity mMockActivity;
    private LocalDatabaseInterface mDB;

    public AddBlocksActivityTest() {
		super(AddBlocksActivity.class);
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
                .inAdapterView(withId(getIdByName("credits_blocks_list")))
                .atPosition(N_LIST_VIEW_ELEMENTS - 1).perform();
    }

    public void testClickOnListView() {
        getActivityOnTest();
        onData(is(instanceOf(HashMap.class)))
                // Every entry in the ListView is a HashMap
                .inAdapterView(withId(getIdByName("credits_blocks_list")))
                .atPosition(0).perform(click())
                .check(doesNotExist());
    }

    public void testUpdateCredits() {

    	mDB.deleteCourse("TestCourse1");
        mDB.deleteCourse("TestCourse2");

        waitOnInsertionInDB();

    	getActivityOnTest();

    	assertEquals("There are no more blocks to be placed "
      		  + "into the calendar, your week is full, good work !", mText.getText().toString());
    }

    public void testGreeter() {
    	getActivityOnTest();

        assertEquals("It seems like you did not fill "
        		  + "all your week up with your timetable"
        		  + " according to your credits. To add an"
        		  + " element to the timetable, click on the course:", mText.getText().toString());
    }

    public void testChangeCredits() {
    	Calendar startEvent = Calendar.getInstance();
    	Calendar endEvent = Calendar.getInstance();
    	endEvent.add(Calendar.HOUR_OF_DAY, 2);

    	Event e = new Event("Do " + mCourses.get(0).getName() + " homework",
                App.calendarToBasicFormatString(startEvent),
                App.calendarToBasicFormatString(endEvent),
                PeriodType.DEFAULT.toString(), mCourses.get(0).getName(),
                "You have to work on " + mCourses.get(0).getName() + " now", true,
                DBQuester.NO_ID);

    	Event eSecond = new Event("Do " + mCourses.get(1).getName() + " homework",
                App.calendarToBasicFormatString(startEvent),
                App.calendarToBasicFormatString(endEvent),
                PeriodType.DEFAULT.toString(), mCourses.get(1).getName(),
                "You have to work on " + mCourses.get(1).getName() + " now", true,
                DBQuester.NO_ID);

    	mDB.storeEvent(e);
    	mDB.storeEvent(eSecond);
        waitOnInsertionInDB();

        getActivityOnTest();

        @SuppressWarnings("unchecked")
		HashMap<String, String> adapter =  (HashMap<String, String>) mList.getAdapter().getItem(0);
        String credits = adapter.get("Remaining credits");

        assertEquals("Remaining credits: 2", credits);
        onData(is(instanceOf(HashMap.class)))
        	.inAdapterView(withId(getIdByName("credits_blocks_list")))
        	.atPosition(0).perform();
    }

    @SuppressWarnings("unchecked")
	public void testOnResult() {
    	getActivityOnTest();

    	//Initial conditions
		HashMap<String, String> adapter =  (HashMap<String, String>) mList.getAdapter().getItem(0);
        String credits = adapter.get("Remaining credits");
        assertEquals("Remaining credits: 2", credits);

        //Mocking up an activityResult
        Intent intent = new Intent();
        intent.putExtra("courseName", "TestCourse1");
        intent.putExtra("position", 0);

        Calendar startEvent = Calendar.getInstance();
    	Calendar endEvent = Calendar.getInstance();
    	endEvent.add(Calendar.HOUR_OF_DAY, 1);

    	Event e = new Event("Do " + mCourses.get(0).getName() + " homework",
                App.calendarToBasicFormatString(startEvent),
                App.calendarToBasicFormatString(endEvent),
                PeriodType.DEFAULT.toString(), mCourses.get(0).getName(),
                "You have to work on " + mCourses.get(0).getName() + " now", true,
                DBQuester.NO_ID);

    	mDB.storeEvent(e);
        waitOnInsertionInDB();
        getActivityOnTest();

        Instrumentation.ActivityResult activityResult = new Instrumentation.ActivityResult(Activity.RESULT_OK, intent);

        Instrumentation.ActivityMonitor activityMonitor = getInstrumentation()
        		  .addMonitor(AddEventBlockActivity.class.getName(), activityResult , true);

        onData(is(instanceOf(HashMap.class))).inAdapterView(withId(getIdByName("credits_blocks_list")))
        .atPosition(0).perform(click());

        // Wait for the ActivityMonitor to be hit, Instrumentation will then return the mock ActivityResult:
        AddEventBlockActivity childActivity = (AddEventBlockActivity) getInstrumentation()
        		  .waitForMonitorWithTimeout(activityMonitor, 5);

        // How do I check that StartActivityForResult correctly handles the returned result?
		adapter =  (HashMap<String, String>) mList.getAdapter().getItem(0);
        credits = adapter.get("Remaining credits");
        assertEquals("Remaining credits: 1", credits);
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
                "Pr. Testpr1", 4, "CS-321", "awesome course", null);

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
                "Pr. Testpr2", 2, "CS-000", "cool course", null);

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
        mText = (TextView) mActivity.findViewById(R.id.greeter);
        mList = (ListView) mActivity.findViewById(R.id.credits_blocks_list);

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

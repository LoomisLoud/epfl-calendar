/**
 *
 */
package ch.epfl.calendar.test.display;

import static com.google.android.apps.common.testing.ui.espresso.Espresso.onView;
import static com.google.android.apps.common.testing.ui.espresso.action.ViewActions.click;
import static com.google.android.apps.common.testing.ui.espresso.assertion.ViewAssertions.matches;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.isDisplayed;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.isEnabled;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.withId;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.withText;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import ch.epfl.calendar.App;
import ch.epfl.calendar.R;
import ch.epfl.calendar.data.Course;
import ch.epfl.calendar.data.Event;
import ch.epfl.calendar.data.Period;
import ch.epfl.calendar.display.AddEventActivity;
import ch.epfl.calendar.persistence.DBQuester;
import ch.epfl.calendar.test.utils.MockActivity;
import ch.epfl.calendar.test.utils.Utils;

import com.google.android.apps.common.testing.testrunner.ActivityLifecycleMonitorRegistry;
import com.google.android.apps.common.testing.testrunner.Stage;
import com.google.common.collect.Iterables;

/**
 * @author AblionGE
 * 
 */
public class AddEventActivityTest extends
        ActivityInstrumentationTestCase2<AddEventActivity> {

    private DBQuester mDB;
    private AddEventActivity mActivity;
    private MockActivity mMockActivity;
    private List<Course> mCourses;
    private List<Event> mEvents;

    private EditText mNameEvent;
    private EditText mDescriptionEvent;
    private Spinner mSpinnerCourses;
    private TextView mFrom;
    private TextView mTo;

    private Button mButtonStartDate;
    private Button mButtonStartHour;
    private Button mButtonEndDate;
    private Button mButtonEndHour;
    private Button mSaveButton;

    private static final int SLEEP_TIME = 250;
    private static final int NB_CREDITS_200 = 200;
    private static final int NB_CREDITS_5 = 5;

    public AddEventActivityTest() {
        super(AddEventActivity.class);
    }

    /*
     * (non-Javadoc)
     * 
     * @see junit.framework.TestCase#setUp()
     */
    @Override
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

        mActivity = new AddEventActivity();
    }

    /*
     * (non-Javadoc)
     * 
     * @see junit.framework.TestCase#tearDown()
     */
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
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
     * {@link ch.epfl.calendar.display.AddEventActivity#finishActivity(android.view.View)}
     * .
     */
    public final void testEditEvent() {

        // Here is creation of the intent used in onCreate.
        // If the Id's value needs to be changed, just move the 3 following
        // lines directly in the tests method BEFORE calling setActivity()
        Intent intent = new Intent();
        intent.putExtra("Id", 1);
        setActivityIntent(intent);

        setActivity();

        int eventId = intent.getIntExtra("Id", DBQuester.NO_ID);
        Event event = new DBQuester().getEvent(eventId);

        onView(withId(mNameEvent.getId())).check(
                matches(withText(event.getName())));
        onView(withId(mDescriptionEvent.getId())).check(
                matches(withText(event.getDescription())));
        int currentPosition = mSpinnerCourses.getSelectedItemPosition();
        String spinnerItem = mSpinnerCourses.getItemAtPosition(currentPosition).toString();
        if (currentPosition == 0) {
            spinnerItem = "NoCourse";
        }
        assertEquals(spinnerItem, event.getLinkedCourse());

        SimpleDateFormat sdfDate = new SimpleDateFormat("MM/dd/yy", Locale.US);
        SimpleDateFormat sdfHour = new SimpleDateFormat("hh:mm aa", Locale.US);

        assertEquals(mButtonStartDate.getText().toString(), sdfDate.format(event.getStartDate()
                        .getTime()));
        assertEquals(mButtonStartHour.getText().toString(), sdfHour.format(event.getStartDate()
                .getTime()));
        assertEquals(mButtonEndDate.getText().toString(), sdfDate.format(event.getEndDate()
                .getTime()));
        assertEquals(mButtonEndHour.getText().toString(), sdfHour.format(event.getEndDate()
                .getTime()));
    }

    public final void testButtons() {
        setActivity();

        Calendar start = Calendar.getInstance();
        Calendar end = Calendar.getInstance();
        end.add(Calendar.HOUR_OF_DAY, 1);
        SimpleDateFormat sdfDate = new SimpleDateFormat("MM/dd/yy", Locale.US);
        SimpleDateFormat sdfHour = new SimpleDateFormat("hh:mm aa", Locale.US);

        assertEquals(sdfDate.format(start.getTime()), mButtonStartDate
                .getText().toString());
        assertEquals(sdfHour.format(start.getTime()), mButtonStartHour
                .getText().toString());
        assertEquals(sdfDate.format(end.getTime()), mButtonEndDate.getText()
                .toString());
        assertEquals(sdfHour.format(end.getTime()), mButtonEndHour.getText()
                .toString());
        assertEquals("Save the event", mSaveButton.getText().toString());

        onView(withId(mButtonStartDate.getId())).check(matches(isDisplayed()));
        onView(withId(mButtonStartDate.getId())).check(matches(isEnabled()));
        onView(withId(mButtonStartDate.getId())).perform(click());
        onView(withText("Done")).perform(click());

        onView(withId(mButtonStartHour.getId())).check(matches(isDisplayed()));
        onView(withId(mButtonStartHour.getId())).check(matches(isEnabled()));
        onView(withId(mButtonStartHour.getId())).perform(click());
        onView(withText("Done")).perform(click());

        onView(withId(mButtonEndDate.getId())).check(matches(isDisplayed()));
        onView(withId(mButtonEndDate.getId())).check(matches(isEnabled()));
        onView(withId(mButtonEndDate.getId())).perform(click());
        onView(withText("Done")).perform(click());

        onView(withId(mButtonEndHour.getId())).check(matches(isDisplayed()));
        onView(withId(mButtonEndHour.getId())).check(matches(isEnabled()));
        onView(withId(mButtonEndHour.getId())).perform(click());
        onView(withText("Done")).perform(click());

        onView(withId(mSaveButton.getId())).check(matches(isDisplayed()));
        onView(withId(mSaveButton.getId())).check(matches(isEnabled()));
        try {
            onView(withId(mSaveButton.getId())).perform(click());
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertNotNull(mDB.getAllEventsFromCourseBlock("TestCourse1"));
    }

    public final void testSpinner() {
        setActivity();
        ArrayAdapter<String> adapter = (ArrayAdapter<String>) mSpinnerCourses
                .getAdapter();

        assertEquals(3, mSpinnerCourses.getCount());
        assertEquals("No connection with courses", adapter.getItem(0)
                .toString());
        assertEquals("TestCourse2", adapter.getItem(1).toString());
        assertEquals("TestCourse1", adapter.getItem(2).toString());
    }

    public final void testTextView() {
        setActivity();
        onView(withId(mFrom.getId())).check(matches(isDisplayed()));
        onView(withId(mTo.getId())).check(matches(isDisplayed()));
    }

    public final void testEditText() {
        setActivity();
        onView(withId(mNameEvent.getId())).check(matches(isDisplayed()));
        onView(withId(mDescriptionEvent.getId())).check(matches(isDisplayed()));
    }

    /**
     * Test method for
     * {@link ch.epfl.calendar.display.AddEventActivity#updateData()}.
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
                "Pr. Testpr1", NB_CREDITS_200, "CS-321", "awesome course", null);

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
                "Pr. Testpr2", NB_CREDITS_5, "CS-000", "cool course", null);

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
        mActivity = getActivity();

        mNameEvent = (EditText) mActivity.findViewById(R.id.name_event_text);
        mDescriptionEvent = (EditText) mActivity
                .findViewById(R.id.description_event_text);
        mSpinnerCourses = (Spinner) mActivity
                .findViewById(R.id.spinner_courses);
        mFrom = (TextView) mActivity.findViewById(R.id.start_event_text_date);
        mTo = (TextView) mActivity.findViewById(R.id.end_event_text_date);

        mButtonStartDate = (Button) mActivity
                .findViewById(R.id.start_event_dialog_date);
        mButtonStartHour = (Button) mActivity
                .findViewById(R.id.start_event_dialog_hour);
        mButtonEndDate = (Button) mActivity
                .findViewById(R.id.end_event_dialog_date);
        mButtonEndHour = (Button) mActivity
                .findViewById(R.id.end_event_dialog_hour);
        mSaveButton = (Button) mActivity.findViewById(R.id.valid_event);

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

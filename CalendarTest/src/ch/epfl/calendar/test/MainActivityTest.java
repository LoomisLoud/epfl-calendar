/**
 *
 */
package ch.epfl.calendar.test;

import static com.google.android.apps.common.testing.ui.espresso.action.ViewActions.longClick;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;
import android.test.UiThreadTest;
import ch.epfl.calendar.App;
import ch.epfl.calendar.MainActivity;
import ch.epfl.calendar.R;
import ch.epfl.calendar.authentication.TequilaAuthenticationAPI;
import ch.epfl.calendar.data.Course;
import ch.epfl.calendar.data.Event;
import ch.epfl.calendar.data.Period;
import ch.epfl.calendar.persistence.DBQuester;
import ch.epfl.calendar.test.utils.MockActivity;
import ch.epfl.calendar.test.utils.Utils;
import ch.epfl.calendar.thirdParty.calendarViews.WeekViewEvent;

import static com.google.android.apps.common.testing.ui.espresso.Espresso.onData;
import static com.google.android.apps.common.testing.ui.espresso.Espresso.onView;
import static com.google.android.apps.common.testing.ui.espresso.action.ViewActions.click;
import static com.google.android.apps.common.testing.ui.espresso.action.ViewActions.longClick;
import static com.google.android.apps.common.testing.ui.espresso.assertion.ViewAssertions.doesNotExist;
import static com.google.android.apps.common.testing.ui.espresso.assertion.ViewAssertions.matches;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.isDisplayed;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.isEnabled;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.withId;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.withText;

import com.google.android.apps.common.testing.testrunner.ActivityLifecycleMonitorRegistry;
import com.google.android.apps.common.testing.testrunner.Stage;
import com.google.common.collect.Iterables;

/**
 * @author AblionGE
 * 
 */
public class MainActivityTest extends
        ActivityInstrumentationTestCase2<MainActivity> {

    private MainActivity mActivity;
    private MockActivity mMockActivity;
    private DBQuester mDB;
    private List<Course> mCourses;
    private List<Event> mEvents;

    private static final int SLEEP_TIME = 250;
    private static final int NB_CREDITS_200 = 200;
    private static final int NB_CREDITS_5 = 5;

    public MainActivityTest() {
        super(MainActivity.class);
    }

    /*
     * (non-Javadoc)
     * @see junit.framework.TestCase#setUp()
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();

        mMockActivity = new MockActivity();

        App.setDBHelper("calendar_test.db");
        // We need to set up which activity is the current one (needed by
        // AsyncTask to be able to use callback functions
        App.setActionBar(mMockActivity);
        mMockActivity.setUdpateData(mMockActivity);

        mDB = new DBQuester();

        mDB.deleteAllTables();
        mDB.createTables();

        populateTestDB();
    }

    /*
     * (non-Javadoc)
     * @see junit.framework.TestCase#tearDown()
     */
    @Override
    protected void tearDown() throws Exception {
        try {
            Utils.pressBack(getCurrentActivity());
        } catch (Throwable e) {
            e.printStackTrace();
        }
        super.tearDown();
    }

    public final void testWhenNoConnected() {
        logout();
        mActivity = getActivity();

        App.setDBHelper("calendar_test.db");

        App.setActionBar(mActivity);
        mActivity.setUdpateData(mActivity);

        try {
            assertNotSame(mActivity, getCurrentActivity());
        } catch (Throwable e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * Test method for
     * {@link ch.epfl.calendar.MainActivity#onEventClick(ch.epfl.calendar.thirdParty.calendarViews.WeekViewEvent, android.graphics.RectF)}
     * .
     * 
     * @throws Throwable
     * 
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    public final void testOnEventClickCourse() throws Throwable {
        setUser();
        // It is very complicated to select a block and click on it (blocks are
        // managed by
        // the imported code, so it seems impossible to find them with
        // espresso).
        // Instead of it, I just test directly the method.
        List<WeekViewEvent> weekEvents = mActivity.onMonthChange();

        mActivity.onEventClick(weekEvents.get(0), null);
        assertFalse(getCurrentActivity().getClass()
                .equals(mActivity.getClass()));
    }

    public final void testOnEventClickEvent() throws Throwable {
        setUser();

        List<WeekViewEvent> weekEvents = mActivity.onMonthChange();
        mActivity.onEventClick(weekEvents.get(1), null);
        assertFalse(getCurrentActivity().getClass()
                .equals(mActivity.getClass()));
    }

    /**
     * Test method for
     * {@link ch.epfl.calendar.MainActivity#onEventLongPress(ch.epfl.calendar.thirdParty.calendarViews.WeekViewEvent, android.graphics.RectF)}
     * .
     * 
     * @throws Throwable
     */
    public final void testOnEventLongPressCourse() throws Throwable {
        setUser();
        List<WeekViewEvent> weekEvents = mActivity.onMonthChange();

        mActivity.onEventLongPress(weekEvents.get(0), null);
        assertFalse(getCurrentActivity().getClass()
                .equals(mActivity.getClass()));
    }

    public final void testOnEventLongPressEventEdit() throws Throwable {
        setUser();

        // Clicking on the WeekView, we are clicking on an event !
        onView(withId(R.id.weekView)).perform(longClick());

        onView(withText("Edit")).perform(click()).check(doesNotExist());

    }

    public final void testOnEventLongPressEventDelete() throws Throwable {
        setUser();
        // Clicking on the WeekView, we are clicking on an event !
        onView(withId(R.id.weekView)).perform(longClick());

        onView(withText("Delete")).perform(click());
    }

    public final void testOnEventLongPressEventViewDetails() throws Throwable {
        setUser();
        // Clicking on the WeekView, we are clicking on an event !
        onView(withId(R.id.weekView)).perform(longClick());
        onView(withText("View Details")).perform(click()).check(doesNotExist());

    }

    public final void testonMonthChange() {
        setUser();
        List<WeekViewEvent> weekEvents = mActivity.onMonthChange();

        assertEquals(3, weekEvents.size());
    }

    private void populateTestDB() throws Exception {
        List<String> period1Course1Rooms = new ArrayList<String>();
        List<String> period2Course1Rooms = new ArrayList<String>();
        period1Course1Rooms.add("GCA 331");
        period1Course1Rooms.add("CO2");
        period2Course1Rooms.add("INF1");
        period2Course1Rooms.add("INF2");
        Calendar calendar1 = new GregorianCalendar();
        calendar1.set(Calendar.MINUTE, 0);
        calendar1.set(Calendar.HOUR_OF_DAY, 8);
        Calendar calendar2 = new GregorianCalendar();
        calendar2.set(Calendar.MINUTE, 0);
        calendar2.set(Calendar.HOUR_OF_DAY, 9);
        Calendar calendar3 = new GregorianCalendar();
        calendar3.set(Calendar.MINUTE, 0);
        calendar3.set(Calendar.HOUR_OF_DAY, 11);
        Calendar calendar4 = new GregorianCalendar();
        calendar4.set(Calendar.MINUTE, 0);
        calendar4.set(Calendar.HOUR_OF_DAY, 12);
        Calendar calendar5 = new GregorianCalendar();
        calendar5.set(Calendar.MINUTE, 0);
        calendar5.set(Calendar.HOUR_OF_DAY, 14);
        Calendar calendar6 = new GregorianCalendar();
        calendar6.set(Calendar.MINUTE, 0);
        calendar6.set(Calendar.HOUR_OF_DAY, 15);
        Period period1Course1 = new Period("Lecture",
                App.calendarToBasicFormatString(calendar1),
                App.calendarToBasicFormatString(calendar2),
                period1Course1Rooms, "1");
        // Period period2Course1 = new Period("Exercise",
        // App.calendarToBasicFormatString(calendar3),
        // App.calendarToBasicFormatString(calendar4), period2Course1Rooms,
        // "2");
        ArrayList<Period> periodsCourse1 = new ArrayList<Period>();
        periodsCourse1.add(period1Course1);
        // periodsCourse1.add(period2Course1);
        Course course1 = new Course("TestCourse1", periodsCourse1,
                "Pr. Testpr1", 200, "CS-321", "awesome course", null);

        // Add events
        Event event1 = new Event("event1",
                App.calendarToBasicFormatString(calendar3),
                App.calendarToBasicFormatString(calendar4), "exercises",
                course1.getName(), "Event 1", false, DBQuester.NO_ID);
        Event event2 = new Event("event2",
                App.calendarToBasicFormatString(calendar5),
                App.calendarToBasicFormatString(calendar6), "exercises",
                App.NO_COURSE, "Event 1", false, DBQuester.NO_ID);

        mEvents = new ArrayList<Event>();
        mEvents.add(event1);
        course1.setEvents(mEvents);
        mEvents.add(event2);

        mCourses = new ArrayList<Course>();
        mCourses.add(course1);

        mDB.storeCourse(course1);
        mDB.storeEvent(event2);
        waitOnInsertionInDB();
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

    private void setUser() {
        App.setCurrentUsername("testUsername");
        // store the sessionID in the preferences
        TequilaAuthenticationAPI.getInstance().setSessionID(
                getInstrumentation().getTargetContext(), "sessionID");
        TequilaAuthenticationAPI.getInstance().setUsername(
                getInstrumentation().getTargetContext(), "testUsername");

        mActivity = getActivity();

        App.setDBHelper("calendar_test.db");

        App.setActionBar(mActivity);
        mActivity.setUdpateData(mActivity);

        mActivity.updateData();
    }

    private void logout() {
        App.setCurrentUsername("");
        // store the sessionID in the preferences
        TequilaAuthenticationAPI.getInstance().clearSessionID(
                getInstrumentation().getTargetContext());
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
        return activity[0];
    }

}

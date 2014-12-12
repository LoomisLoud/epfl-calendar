/**
 *
 */
package ch.epfl.calendar.test;

import static com.google.android.apps.common.testing.ui.espresso.Espresso.onView;
import static com.google.android.apps.common.testing.ui.espresso.Espresso.openContextualActionModeOverflowMenu;
import static com.google.android.apps.common.testing.ui.espresso.action.ViewActions.click;
import static com.google.android.apps.common.testing.ui.espresso.assertion.ViewAssertions.doesNotExist;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.withId;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.withText;
import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;
import ch.epfl.calendar.App;
import ch.epfl.calendar.MainActivity;
import ch.epfl.calendar.R;
import ch.epfl.calendar.authentication.TequilaAuthenticationAPI;
import ch.epfl.calendar.data.Event;
import ch.epfl.calendar.display.CourseDetailsActivity;
import ch.epfl.calendar.persistence.DBQuester;
import ch.epfl.calendar.test.utils.Utils;

import com.google.android.apps.common.testing.testrunner.ActivityLifecycleMonitorRegistry;
import com.google.android.apps.common.testing.testrunner.Stage;
import com.google.common.collect.Iterables;

/**
 * @author fouchepi
 * 
 */
public class DefaultActionBarActivityTest extends
        ActivityInstrumentationTestCase2<CourseDetailsActivity> {

    private CourseDetailsActivity mActivity;
    private DBQuester mDB;
    private static final int SLEEP_TIME = 250;

    public DefaultActionBarActivityTest() {
        super(CourseDetailsActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();

        App.setDBHelper("calendar_test.db");
        getInstrumentation().getTargetContext().deleteDatabase(
                App.getDBHelper().getDatabaseName());
        
        mDB = new DBQuester();

        mDB.deleteAllTables();
        mDB.createTables();

        mActivity = getActivity();
        
        App.setActionBar(mActivity);
        mActivity.setUdpateData(mActivity);
    }

    public void testSwitchToCoursesList() {
        openContextualActionModeOverflowMenu();
        onView(withText("Courses")).perform(click()).check(doesNotExist());
    }

    public void testSwitchToCreditsActivity() {
        openContextualActionModeOverflowMenu();
        onView(withText("Credits")).perform(click()).check(doesNotExist());
    }

    public void testSwitchToAddEventsActivity() {
        onView(withId(R.id.add_event)).perform(click()).check(doesNotExist());
    }

    public void testSwitchToAddBlockActivity() {
        openContextualActionModeOverflowMenu();
        onView(withText("Add blocks of credits")).perform(click()).check(
                doesNotExist());
    }

    public void testSwitchToListEvent() throws Throwable {
        openContextualActionModeOverflowMenu();
        Class<? extends CourseDetailsActivity> oldActivity = getActivity()
                .getClass();
        onView(withText("Planning")).perform(click());
        assertNotSame(oldActivity, getCurrentActivity().getClass());
    }

    public void testSwitchToEditActivity() throws Throwable {
        Event event = new Event("event", "27.12.2014 08:00",
                "27.12.2014 18:00", "lecture", App.NO_COURSE, "Event", false,
                DBQuester.NO_ID);

        mDB.storeEvent(event);
        waitOnInsertionInDB();

        Class<? extends CourseDetailsActivity> oldActivity = mActivity
                .getClass();
        System.out.println(mDB.getAllEvents().size());
        mActivity.switchToEditActivity(mDB.getAllEvents().get(0));
        assertNotSame(oldActivity, getCurrentActivity().getClass());
    }

    public void testLogout() {
        openContextualActionModeOverflowMenu();
        onView(withText("Logout")).perform(click()).check(doesNotExist());
    }

    public void testSwitchToCalendar() {
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
        onView(withId(R.id.action_calendar)).perform(click()).check(
                doesNotExist());
    }

    @Override
    protected void tearDown() throws Exception {
        try {
            Utils.pressBack(getCurrentActivity());
        } catch (Throwable e) {
            e.printStackTrace();
        }
        super.tearDown();
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

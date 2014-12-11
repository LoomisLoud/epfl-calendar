/**
 * 
 */
package ch.epfl.calendar.test.display;

import static com.google.android.apps.common.testing.ui.espresso.Espresso.onView;
import static com.google.android.apps.common.testing.ui.espresso.assertion.ViewAssertions.matches;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.withId;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.withText;
import android.app.Activity;
import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import ch.epfl.calendar.R;
import ch.epfl.calendar.display.EventDetailActivity;
import ch.epfl.calendar.test.utils.Utils;

import com.google.android.apps.common.testing.testrunner.ActivityLifecycleMonitorRegistry;
import com.google.android.apps.common.testing.testrunner.Stage;
import com.google.common.collect.Iterables;

/**
 * @author AblionGE
 * 
 */
public class EventDetailActivityTest extends
        ActivityInstrumentationTestCase2<EventDetailActivity> {

    private EventDetailActivity mActivity;
    private final static String NAME = "name";
    private final static String DESCRIPTION = "description";

    public EventDetailActivityTest() {
        super(EventDetailActivity.class);
    }

    /*
     * (non-Javadoc)
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();

        Intent intent = new Intent();
        intent.putExtra("description", new String[] {NAME, DESCRIPTION});
        setActivityIntent(intent);
        mActivity = getActivity();
    }

    /*
     * (non-Javadoc)
     * @see junit.framework.TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        try {
            Utils.pressBack(getCurrentActivity());
        } catch (Throwable e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        super.tearDown();
    }

    /**
     * Test method for
     * {@link ch.epfl.calendar.display.EventDetailActivity#updateData()}.
     */
    public final void testFields() {
        onView(withId(R.id.eventName)).check(matches(withText(NAME)));
        onView(withId(R.id.eventDescription)).check(
                matches(withText("Description: " + DESCRIPTION)));

        // Just for code coverage...
        mActivity.updateData();
    }
    
    Activity getCurrentActivity() throws Throwable {
        getInstrumentation().waitForIdleSync();
        final Activity[] activity = new Activity[1];
        runTestOnUiThread(new Runnable() {
          @Override
          public void run() {
            java.util.Collection<Activity> activites = ActivityLifecycleMonitorRegistry.getInstance().getActivitiesInStage(Stage.RESUMED);
            activity[0] = Iterables.getOnlyElement(activites);
        }});
        return activity[0];
      }

}

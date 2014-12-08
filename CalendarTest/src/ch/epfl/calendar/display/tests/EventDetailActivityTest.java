/**
 * 
 */
package ch.epfl.calendar.display.tests;

import static com.google.android.apps.common.testing.ui.espresso.Espresso.onView;
import static com.google.android.apps.common.testing.ui.espresso.assertion.ViewAssertions.matches;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.withId;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.withText;
import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import ch.epfl.calendar.R;
import ch.epfl.calendar.display.EventDetailActivity;

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

}

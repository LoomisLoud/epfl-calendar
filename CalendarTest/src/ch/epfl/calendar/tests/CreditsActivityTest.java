package ch.epfl.calendar.tests;

import static com.google.android.apps.common.testing.ui.espresso.Espresso.onView;
import static com.google.android.apps.common.testing.ui.espresso.action.ViewActions.click;
import static com.google.android.apps.common.testing.ui.espresso.assertion.ViewAssertions.matches;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.isDisplayed;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.isEnabled;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.withId;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;
import android.widget.TextView;
import ch.epfl.calendar.CreditsActivity;
import ch.epfl.calendar.R;

/**
 * CreditsActivity test class
 *
 * @author lweingart
 *
 */
public class CreditsActivityTest extends
		ActivityInstrumentationTestCase2<CreditsActivity>  {

	private CreditsActivity mActivity;

	public CreditsActivityTest() {
		super(CreditsActivity.class);
	}

	@Override
	public void setUp() throws Exception {
		super.setUp();
		mActivity = getActivity();
	}

	@Override
	public void tearDown() throws Exception {
		super.tearDown();
	}

	public void testTextContent() {
		TextView textView = (TextView) mActivity.findViewById(R.id.credit_txtView_id);
		assertTrue(textView.getText().equals(CreditsActivity.AUTHORS));
	}

    public void testButtonIsDisplayedAndEnabled() {
//    	switchToTmpActivity();
        onView(withId(R.id.ok_btn)).check(matches(isDisplayed()));
        onView(withId(R.id.ok_btn)).check(matches(isEnabled()));
        onView(withId(R.id.ok_btn)).perform(click());
//        onData(is(instanceOf(CreditsActivity.class))).check(doesNotExist());
    }

    public void testButtonText() {
    	Button btn = (Button) mActivity.findViewById(R.id.ok_btn);
    	assertTrue(btn.getText().equals("OK"));
    }

//    private void switchToTmpActivity() {
//    	Intent i = new Intent(mActivity, TmpActivity.class);
//    	mActivity.startActivity(i);
//    }
//
//    /**
//     * local need of an activity
//     *
//     * @author lweingart
//     *
//     */
//    private class TmpActivity extends Activity {
//    	@Override
//    	protected void onCreate(Bundle savedInstanceState) {
//    		super.onCreate(savedInstanceState);
//    		Intent i = new Intent(this, CreditsActivity.class);
//    		startActivity(i);
//    	}
//    }
}

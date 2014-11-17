package ch.epfl.calendar.authentication.tests;

import static com.google.android.apps.common.testing.ui.espresso.Espresso.*;
import static com.google.android.apps.common.testing.ui.espresso.action.ViewActions.*;
import static com.google.android.apps.common.testing.ui.espresso.assertion.ViewAssertions.*;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.*;

import static org.hamcrest.Matchers.*;

import android.test.ActivityInstrumentationTestCase2;
import ch.epfl.calendar.R;
import ch.epfl.calendar.authentication.AuthenticationActivity;

/**
 * This class tests the AuthenticationActivity
 * @author AblionGE
 *
 */
public class AuthenticationActivityTest
    extends ActivityInstrumentationTestCase2<AuthenticationActivity> {

    public AuthenticationActivityTest() {
        super(AuthenticationActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        getActivity();
    }

    public final void testButtonIsDisplayedAndEnabled() {
        onView(withId(R.id.btnLogin)).check(matches(isDisplayed()));
        onView(withId(R.id.btnLogin)).check(matches(isEnabled()));
    }

    public final void testClickWithoutCredentials() {
        onView(withId(R.id.btnLogin)).perform(click());
    }

}

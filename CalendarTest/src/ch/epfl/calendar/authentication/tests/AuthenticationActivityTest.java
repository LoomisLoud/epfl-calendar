package ch.epfl.calendar.authentication.tests;

import static com.google.android.apps.common.testing.ui.espresso.Espresso.onView;
import static com.google.android.apps.common.testing.ui.espresso.action.ViewActions.click;
import static com.google.android.apps.common.testing.ui.espresso.action.ViewActions.typeText;
import static com.google.android.apps.common.testing.ui.espresso.assertion.ViewAssertions.doesNotExist;
import static com.google.android.apps.common.testing.ui.espresso.assertion.ViewAssertions.matches;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.isDisplayed;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.isEnabled;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.withId;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.withText;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.widget.Checkable;
import android.widget.EditText;
import android.widget.TextView;
import ch.epfl.calendar.App;
import ch.epfl.calendar.R;
import ch.epfl.calendar.apiInterface.CalendarClient;
import ch.epfl.calendar.authentication.AuthenticationActivity;
import ch.epfl.calendar.authentication.TequilaAuthenticationAPI;
import ch.epfl.calendar.testing.utils.Utils;

/**
 * This class tests the AuthenticationActivity
 * @author AblionGE
 *
 */
public class AuthenticationActivityTest
    extends ActivityInstrumentationTestCase2<AuthenticationActivity> {

    private AuthenticationActivity mAuthActivity;

    public AuthenticationActivityTest() {
        super(AuthenticationActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        mAuthActivity = getActivity();
    }
    
    @Override
    public void tearDown() throws Exception {
        Utils.pressBack(mAuthActivity);
        super.tearDown();
    }

    public void testButtonIsDisplayedAndEnabled() {
        onView(withId(R.id.btnLogin)).check(matches(isDisplayed()));
        onView(withId(R.id.btnLogin)).check(matches(isEnabled()));
    }

    public void testClickWithoutCredentials() {
        onView(withId(R.id.btnLogin)).perform(click());
        onView(withId(R.id.btnLogin)).check(matches(isDisplayed()));
        onView(withId(R.id.txtUsername)).check(matches(isDisplayed()));
        onView(withId(R.id.txtPassword)).check(matches(isDisplayed()));
    }

    public void testValueAtInit() {
        onView(withText(mAuthActivity.getString(R.string.login_submit_text))).check(matches(isDisplayed()));
        onView(withHint(mAuthActivity.getString(R.string.login_password_hint))).check(matches(isDisplayed()));
        onView(withHint(mAuthActivity.getString(R.string.login_username_hint))).check(matches(isDisplayed()));
    }

    public void testFillFields() {
        onView(withId(R.id.txtUsername)).perform(typeText("test_username"));
        onView(withId(R.id.txtPassword)).perform(typeText("test_password"));
        onView(withId(R.id.txtUsername)).check(matches(withText("test_username")));
        onView(withId(R.id.txtPassword)).check(matches(withText("test_password")));
    }
    
    public void testTequilaAuthenticationHandler() {
        onView(withId(R.id.txtUsername)).perform(typeText("test_username"));
        onView(withId(R.id.txtPassword)).perform(typeText("test_password"));
        onView(withId(R.id.txtUsername)).check(matches(withText("test_username")));
        onView(withId(R.id.txtPassword)).check(matches(withText("test_password")));
        
        mAuthActivity.tequilaAuthenticationHandlerOnError("Error");
        onView(withId(R.id.txtUsername)).check(matches(withText("")));
        onView(withId(R.id.txtPassword)).check(matches(withText("")));
        
        onView(withId(R.id.txtUsername)).perform(typeText("test_username"));
        onView(withId(R.id.txtPassword)).perform(typeText("test_password"));
        mAuthActivity.tequilaAuthenticationHandlerOnSuccess("123");
        assertEquals("123", TequilaAuthenticationAPI.getInstance().getSessionID(mAuthActivity.getApplicationContext()));
        assertEquals("test_username", App.getCurrentUsername());
        assertEquals("test_username", TequilaAuthenticationAPI.getInstance()
                .getUsername(mAuthActivity.getApplicationContext()));
    }

    private static Matcher<View> withHint(final String expectedHint) {
        return new TypeSafeMatcher<View>() {

            @Override
            public boolean matchesSafely(View view) {
                if (!(view instanceof EditText)) {
                    return false;
                }

                String hint = ((EditText) view).getHint().toString();

                return expectedHint.equals(hint);
            }

            @Override
            public void describeTo(Description description) {
            }
        };
    }
}

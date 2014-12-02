package ch.epfl.calendar.authentication.tests;

import static com.google.android.apps.common.testing.ui.espresso.Espresso.*;
import static com.google.android.apps.common.testing.ui.espresso.action.ViewActions.*;
import static com.google.android.apps.common.testing.ui.espresso.assertion.ViewAssertions.*;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import android.content.Context;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.widget.EditText;
import ch.epfl.calendar.R;
import ch.epfl.calendar.authentication.AuthenticationActivity;
import ch.epfl.calendar.authentication.TequilaAuthenticationTask;

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
    
    private int getIdByName(String name) {
        Context context = getInstrumentation().getTargetContext();
        int result = context.getResources().getIdentifier(name, "id",
                context.getPackageName());
        assertTrue("id for name not found: " + name, result != 0);
        return result;
    }

}

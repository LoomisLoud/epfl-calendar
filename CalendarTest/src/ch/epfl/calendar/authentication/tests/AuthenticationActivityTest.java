package ch.epfl.calendar.authentication.tests;

import static com.google.android.apps.common.testing.ui.espresso.Espresso.*;
import static com.google.android.apps.common.testing.ui.espresso.action.ViewActions.*;
import static com.google.android.apps.common.testing.ui.espresso.assertion.ViewAssertions.*;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.mockito.Mockito;

import com.google.android.apps.common.testing.ui.espresso.matcher.BoundedMatcher;

import static org.hamcrest.Matchers.*;

import android.content.Context;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.widget.EditText;
import ch.epfl.calendar.R;
import ch.epfl.calendar.authentication.AuthenticationActivity;
import ch.epfl.calendar.authentication.TequilaAuthenticationTask;
import ch.epfl.calendar.authentication.TequilaAuthenticationTask.TequilaAuthenticationListener;

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
    protected void setUp() throws Exception {
        super.setUp();
        mAuthActivity = getActivity();
    }

    public final void testButtonIsDisplayedAndEnabled() {
        onView(withId(R.id.btnLogin)).check(matches(isDisplayed()));
        onView(withId(R.id.btnLogin)).check(matches(isEnabled()));
    }

    public final void testClickWithoutCredentials() {
        onView(withId(R.id.btnLogin)).perform(click());
    }
    
    public final void testValueAtInit() {
        onView(withText(mAuthActivity.getString(R.string.login_submit_text))).check(matches(isDisplayed()));
        onView(withHint(mAuthActivity.getString(R.string.login_password_hint))).check(matches(isDisplayed()));
        onView(withHint(mAuthActivity.getString(R.string.login_username_hint))).check(matches(isDisplayed()));
    }
    
    public final void testProgressDialog() throws NoSuchMethodException {
        Method onPreExecute;
        TequilaAuthenticationListener listener = Mockito.mock(TequilaAuthenticationListener.class);
        TequilaAuthenticationTask instance = 
                new TequilaAuthenticationTask(mAuthActivity, listener, "test", "test");
        
        onPreExecute = (TequilaAuthenticationTask.class).getDeclaredMethod("onPreExecute", new Class[] {});
        onPreExecute.setAccessible(true);
        try {
            onPreExecute.invoke(instance, new Object[] {});
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        
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

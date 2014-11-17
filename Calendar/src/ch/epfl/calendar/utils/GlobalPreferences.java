package ch.epfl.calendar.utils;

import org.apache.http.cookie.Cookie;

import android.content.Context;
import ch.epfl.calendar.authentication.TequilaAuthenticationAPI;

/**
 * Serves as a place for everyone to have access to the preferences of the app
 * @author lweingart
 *
 */
public final class GlobalPreferences {

    private static GlobalPreferences mInstance;
    private Cookie mCookieWithSessionID = null;
    private Cookie mCookieWithTequilaUsername = null;
    private Cookie mCookieWithTequilaKey = null;

    private GlobalPreferences() {
    }

    public static GlobalPreferences getInstance() {
        if (mInstance == null) {
            mInstance = new GlobalPreferences();
        }
        return mInstance;
    }

    public static boolean isAuthenticated(Context context) {
        String tokenID = TequilaAuthenticationAPI.getInstance().getSessionID(context);
        return (tokenID != null) && !tokenID.isEmpty();
    }

    public Cookie getSessionIDCookie() {
    	return mCookieWithSessionID;
    }

    public void setSessionIDCookie(Cookie cookieWithSessionID) {
    	mCookieWithSessionID = cookieWithSessionID;
    }

    public Cookie getTequilaUsernameCookie() {
    	return mCookieWithTequilaUsername;
    }

    public void setTequilaUsernameCookie(Cookie cookieWithTequilaUsername) {
    	mCookieWithTequilaUsername = cookieWithTequilaUsername;
    }

    public Cookie getTequilaKeyCookie() {
    	return mCookieWithTequilaKey;
    }

    public void setTequilaKeyCookie(Cookie cookieWithTequilaKey) {
    	mCookieWithTequilaKey = cookieWithTequilaKey;
    }

}

package ch.epfl.calendar.utils;

import org.apache.http.cookie.Cookie;

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

    /**
     * @return the instance of this class
     */
    public static GlobalPreferences getInstance() {
        if (mInstance == null) {
            mInstance = new GlobalPreferences();
        }
        return mInstance;
    }

    /**
     * 
     * @return the {@link Cookie} containing the session ID for the current session.
     */
    public Cookie getSessionIDCookie() {
    	return mCookieWithSessionID;
    }

    /**
     * Set the {@link Cookie} containing the session ID.
     * @param cookieWithSessionID the {@link Cookie} to set.
     */
    public void setSessionIDCookie(Cookie cookieWithSessionID) {
    	mCookieWithSessionID = cookieWithSessionID;
    }

    /**
     * 
     * @return the {@link Cookie} containing the username of the current user.
     */
    public Cookie getTequilaUsernameCookie() {
    	return mCookieWithTequilaUsername;
    }

    /**
     * Sets the {@link Cookie} containing the username of the current user.
     * @param cookieWithTequilaUsername the cookie to set
     */
    public void setTequilaUsernameCookie(Cookie cookieWithTequilaUsername) {
    	mCookieWithTequilaUsername = cookieWithTequilaUsername;
    }

    /**
     * 
     * @return the {@link Cookie} containing the tequila key of the current user.
     */
    public Cookie getTequilaKeyCookie() {
    	return mCookieWithTequilaKey;
    }

    /**
     * Sets the {@link Cookie} containing the tequila key of the current user.
     * @param cookieWithTequilaUsername the cookie to set
     */
    public void setTequilaKeyCookie(Cookie cookieWithTequilaKey) {
    	mCookieWithTequilaKey = cookieWithTequilaKey;
    }

}

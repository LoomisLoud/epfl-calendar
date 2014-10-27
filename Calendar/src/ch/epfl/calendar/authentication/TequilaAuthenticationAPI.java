package ch.epfl.calendar.authentication;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;


/**
 * Basic wrapper for the Tequila authentication process
 * @author lweingart
 *
 */
public final class TequilaAuthenticationAPI {

    public static final String AUTHENTICATION_PREFERENCES_NAME = "user_session";
    public static final String AUTHENTICATION_SESSION_ID_KEY = "SESSION_ID";

    public static final int STATUS_CODE_OK = 200;
    public static final int STATUS_CODE_AUTH_RESPONSE = 302;

    private static TequilaAuthenticationAPI instance;

    private final String isAcademiaLoginURL;
    private final String tequilaAuthenticationURL;

    private static final String ISACADEMIA_LOGIN_URL = "https://isa.epfl.ch/service/secure/student/timetable/week";
    private static final String TEQUILA_AUTHENTICATION_URL = "https://tequila.epfl.ch/cgi-bin/tequila/login";

    public static TequilaAuthenticationAPI getInstance() {
        if (instance == null) {
            instance = new TequilaAuthenticationAPI();
        }
        return instance;
    }

    // disable the creation of objects
    private TequilaAuthenticationAPI() {
        isAcademiaLoginURL = ISACADEMIA_LOGIN_URL;
        tequilaAuthenticationURL = TEQUILA_AUTHENTICATION_URL;
    }

    /**
     * This function will only be called INSIDE this package.
     * And ONLY by the AuthenticationActivity! It stores the session id
     * inside the Shared Preferences.
     *
     * @param context
     * @param sessionID
     */
    void setSessionID(Context context, String sessionID) {
        SharedPreferences prefs = context.
                getSharedPreferences(TequilaAuthenticationAPI.AUTHENTICATION_PREFERENCES_NAME, Context.MODE_PRIVATE);
        Editor editor = prefs.edit();
        editor.putString(TequilaAuthenticationAPI.AUTHENTICATION_SESSION_ID_KEY, sessionID);
        editor.apply();
    }

    /**
     * This function should be called by the MainActivity if the user
     * selects to Log Out.
     *
     * @param context
     */
    public void clearSessionID(Context context) {
        SharedPreferences prefs = context.
                getSharedPreferences(TequilaAuthenticationAPI.AUTHENTICATION_PREFERENCES_NAME, Context.MODE_PRIVATE);
        Editor editor = prefs.edit();
        editor.remove(TequilaAuthenticationAPI.AUTHENTICATION_SESSION_ID_KEY);
        editor.apply();
    }

    /**
     * This function returns the sessionID if there is one stored
     * inside the SharedPreferences otherwise it returns an empty string
     * @param context
     * @return
     */
    public String getSessionID(Context context) {
        SharedPreferences prefs = context.
                getSharedPreferences(TequilaAuthenticationAPI.AUTHENTICATION_PREFERENCES_NAME, Context.MODE_PRIVATE);
        return prefs.getString(AUTHENTICATION_SESSION_ID_KEY, "");
    }

    String getIsAcademiaLoginURL() {
        return this.isAcademiaLoginURL;
    }

    String getTequilaAuthenticationURL() {
        return this.tequilaAuthenticationURL;
    }
}

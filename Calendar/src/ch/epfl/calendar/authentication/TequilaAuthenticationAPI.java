package ch.epfl.calendar.authentication;

import java.util.Calendar;
import java.util.GregorianCalendar;

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
    public static final String AUTHENTICATION_USERNAME = "USERNAME";
    public static final String AUTHENTICATION_TEQUILA_KEY = "TEQUILA_KEY";

    public static final int STATUS_CODE_OK = 200;
    public static final int STATUS_CODE_AUTH_RESPONSE = 302;

    private static TequilaAuthenticationAPI instance;

    private final String isAcademiaLoginURL;
    private final String tequilaAuthenticationURL;

    private static String isAcademiaLoginUrl =
            "https://isa.epfl.ch/service/secure/student/timetable/period?";
    private static final String TEQUILA_AUTHENTICATION_URL = "https://tequila.epfl.ch/cgi-bin/tequila/login";
    
    private static final int AUGUST_MONTH = 8;
    private static final int LAST_DAY_OF_AUGUST = 31;

    public static TequilaAuthenticationAPI getInstance() {
        if (instance == null) {
            instance = new TequilaAuthenticationAPI();
        }
        return instance;
    }

    // disable the creation of objects
    private TequilaAuthenticationAPI() {
        isAcademiaLoginURL = isAcademiaLoginUrl + calculateDate(new GregorianCalendar());
        tequilaAuthenticationURL = TEQUILA_AUTHENTICATION_URL;
    }
    
    private String calculateDate(Calendar currentDate) {
        String period = null;
        if (currentDate.before(new GregorianCalendar(
                currentDate.get(Calendar.YEAR),
                AUGUST_MONTH-1,
                LAST_DAY_OF_AUGUST)
        )) {
            period = "from=" + LAST_DAY_OF_AUGUST + "." + AUGUST_MONTH + "."
                    + (currentDate.get(Calendar.YEAR)-1) + "&to="
                    + LAST_DAY_OF_AUGUST + "." + AUGUST_MONTH
                    + "." + currentDate.get(Calendar.YEAR);
        } else {
            period = "from=" + LAST_DAY_OF_AUGUST + "." + AUGUST_MONTH + "."
                    + (currentDate.get(Calendar.YEAR)) + "&to="
                    + LAST_DAY_OF_AUGUST + "." + AUGUST_MONTH
                    + "." + (currentDate.get(Calendar.YEAR)+1);
        }
        return period;
    }

    public void clearStoredData(Context context) {
        clearSessionID(context);
        clearTequilaKey(context);
        clearUsername(context);
    }
    
    /**
     * This function will only be called INSIDE this package.
     * And ONLY by the AuthenticationActivity! It stores the session id
     * inside the Shared Preferences.
     *
     * @param context
     * @param sessionID
     */
    public void setSessionID(Context context, String sessionID) {
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
    
    /**
     * This function will only be called INSIDE this package.
     * And ONLY by the AuthenticationActivity! It stores the session id
     * inside the Shared Preferences.
     *
     * @param context
     * @param username
     */
    public void setUsername(Context context, String username) {
        SharedPreferences prefs = context.
                getSharedPreferences(TequilaAuthenticationAPI.AUTHENTICATION_PREFERENCES_NAME, Context.MODE_PRIVATE);
        Editor editor = prefs.edit();
        editor.putString(TequilaAuthenticationAPI.AUTHENTICATION_USERNAME, username);
        editor.apply();
    }

    /**
     * This function should be called by the MainActivity if the user
     * selects to Log Out.
     *
     * @param context
     */
    public void clearUsername(Context context) {
        SharedPreferences prefs = context.
                getSharedPreferences(TequilaAuthenticationAPI.AUTHENTICATION_PREFERENCES_NAME, Context.MODE_PRIVATE);
        Editor editor = prefs.edit();
        editor.remove(TequilaAuthenticationAPI.AUTHENTICATION_USERNAME);
        editor.apply();
    }

    /**
     * This function returns the sessionID if there is one stored
     * inside the SharedPreferences otherwise it returns an empty string
     * @param context
     * @return
     */
    public String getUsername(Context context) {
        SharedPreferences prefs = context.
                getSharedPreferences(TequilaAuthenticationAPI.AUTHENTICATION_PREFERENCES_NAME, Context.MODE_PRIVATE);
        return prefs.getString(AUTHENTICATION_USERNAME, "");
    }
    
    /**
     * This function will only be called INSIDE this package.
     * And ONLY by the AuthenticationActivity! It stores the session id
     * inside the Shared Preferences.
     *
     * @param context
     * @param tequilaKey
     */
    public void setTequilaKey(Context context, String tequilaKey) {
        SharedPreferences prefs = context.
                getSharedPreferences(TequilaAuthenticationAPI.AUTHENTICATION_PREFERENCES_NAME, Context.MODE_PRIVATE);
        Editor editor = prefs.edit();
        editor.putString(TequilaAuthenticationAPI.AUTHENTICATION_TEQUILA_KEY, tequilaKey);
        editor.apply();
    }

    /**
     * This function should be called by the MainActivity if the user
     * selects to Log Out.
     *
     * @param context
     */
    public void clearTequilaKey(Context context) {
        SharedPreferences prefs = context.
                getSharedPreferences(TequilaAuthenticationAPI.AUTHENTICATION_PREFERENCES_NAME, Context.MODE_PRIVATE);
        Editor editor = prefs.edit();
        editor.remove(TequilaAuthenticationAPI.AUTHENTICATION_TEQUILA_KEY);
        editor.apply();
    }

    /**
     * This function returns the sessionID if there is one stored
     * inside the SharedPreferences otherwise it returns an empty string
     * @param context
     * @return
     */
    public String getTequilaKey(Context context) {
        SharedPreferences prefs = context.
                getSharedPreferences(TequilaAuthenticationAPI.AUTHENTICATION_PREFERENCES_NAME, Context.MODE_PRIVATE);
        return prefs.getString(AUTHENTICATION_TEQUILA_KEY, "");
    }

    public String getIsAcademiaLoginURL() {
        return this.isAcademiaLoginURL;
    }

    public String getTequilaAuthenticationURL() {
        return this.tequilaAuthenticationURL;
    }
}

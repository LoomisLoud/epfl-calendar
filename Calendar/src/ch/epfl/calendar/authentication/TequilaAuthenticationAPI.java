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
public class TequilaAuthenticationAPI {

    /**
     * The name of the file containing the session informations in the {@link SharedPreferences}
     */
    public static final String AUTHENTICATION_PREFERENCES_NAME = "user_session";
    
    /**
     * The Key in the {@link SharedPreferences} to get the session id.
     */
    public static final String AUTHENTICATION_SESSION_ID_KEY = "SESSION_ID";
    
    /**
     * The username field in the {@link SharedPreferences}
     */
    public static final String AUTHENTICATION_USERNAME = "USERNAME";
    
    /**
     * The tequila key in the {@link SharedPreferences}
     */
    public static final String AUTHENTICATION_TEQUILA_KEY = "TEQUILA_KEY";

    /**
     * The HTTP status code returned by the server when it is ok (200)
     */
    public static final int STATUS_CODE_OK = 200;
    
    /**
     * Status code sent by the authentication response, actually is a redirection (302)/
     */
    public static final int STATUS_CODE_AUTH_RESPONSE = 302;

    private static TequilaAuthenticationAPI instance;

    private final String isAcademiaLoginURL;
    private final String tequilaAuthenticationURL;

    private static String isAcademiaLoginUrl =
            "https://isa.epfl.ch/service/secure/student/timetable/period?";
    private static final String TEQUILA_AUTHENTICATION_URL = "https://tequila.epfl.ch/cgi-bin/tequila/login";

    private static final int AUGUST_MONTH = 8;
    private static final int LAST_DAY_OF_AUGUST = 31;

    /**
     * 
     * @return the unique instance of this class.
     */
    public static TequilaAuthenticationAPI getInstance() {
        if (instance == null) {
            instance = new TequilaAuthenticationAPI();
        }
        return instance;
    }

    /**
     * The constructor, should not be called directly. Cannot be private because we need the class not to be final.
     */
    public TequilaAuthenticationAPI() {
        isAcademiaLoginURL = isAcademiaLoginUrl + calculateDate(new GregorianCalendar());
        tequilaAuthenticationURL = TEQUILA_AUTHENTICATION_URL;
    }
    
    /**
     * Removes the stored data from the {@link SharedPreferences}.
     * @param context the context of the application
     */
    public void clearStoredData(Context context) {
        clearSessionID(context);
        clearTequilaKey(context);
        clearUsername(context);
    }
    
    /**
     * Stores the session ID in the {@link SharedPreferences}
     * @param context the context of the application
     * @param sessionID the session ID to store
     */
    public void setSessionID(Context context, String sessionID) {
        SharedPreferences prefs = context.
                getSharedPreferences(TequilaAuthenticationAPI.AUTHENTICATION_PREFERENCES_NAME, Context.MODE_PRIVATE);
        Editor editor = prefs.edit();
        editor.putString(TequilaAuthenticationAPI.AUTHENTICATION_SESSION_ID_KEY, sessionID);
        editor.apply();
    }

    /**
     * This function should be called whenever the user logs out
     *
     * @param context the context of the application
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
     * @param context the context of the application
     * @return the session ID of the current user if it exists
     */
    public String getSessionID(Context context) {
        SharedPreferences prefs = context.
                getSharedPreferences(TequilaAuthenticationAPI.AUTHENTICATION_PREFERENCES_NAME, Context.MODE_PRIVATE);
        return prefs.getString(AUTHENTICATION_SESSION_ID_KEY, "");
    }
    
    /**
     * This function stores the username of the current user in the {@link SharedPreferences}
     *
     * @param context the context of the application
     * @param username the username to store
     */
    public void setUsername(Context context, String username) {
        SharedPreferences prefs = context.
                getSharedPreferences(TequilaAuthenticationAPI.AUTHENTICATION_PREFERENCES_NAME, Context.MODE_PRIVATE);
        Editor editor = prefs.edit();
        editor.putString(TequilaAuthenticationAPI.AUTHENTICATION_USERNAME, username);
        editor.apply();
    }

    /**
     * This function should be called whenever the user logs out.
     *
     * @param context the context of the application
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
     * @param context the context of the application
     * @return the sessionID if there is one stored inside the SharedPreferences otherwise it returns an empty string
     */
    public String getUsername(Context context) {
        SharedPreferences prefs = context.
                getSharedPreferences(TequilaAuthenticationAPI.AUTHENTICATION_PREFERENCES_NAME, Context.MODE_PRIVATE);
        return prefs.getString(AUTHENTICATION_USERNAME, "");
    }
    
    /**
     * This function stores the tequila key in the {@link SharedPreferences}
     *
     * @param context the context of the application
     * @param tequilaKey the tequila key to store
     */
    public void setTequilaKey(Context context, String tequilaKey) {
        SharedPreferences prefs = context.
                getSharedPreferences(TequilaAuthenticationAPI.AUTHENTICATION_PREFERENCES_NAME, Context.MODE_PRIVATE);
        Editor editor = prefs.edit();
        editor.putString(TequilaAuthenticationAPI.AUTHENTICATION_TEQUILA_KEY, tequilaKey);
        editor.apply();
    }

    /**
     * This function should be called whenever the user logs out.
     *
     * @param context the context of the application
     */
    public void clearTequilaKey(Context context) {
        SharedPreferences prefs = context.
                getSharedPreferences(TequilaAuthenticationAPI.AUTHENTICATION_PREFERENCES_NAME, Context.MODE_PRIVATE);
        Editor editor = prefs.edit();
        editor.remove(TequilaAuthenticationAPI.AUTHENTICATION_TEQUILA_KEY);
        editor.apply();
    }

    /**
     * This function returns the tequila key if there is one stored
     * inside the SharedPreferences otherwise it returns an empty string
     * @param context context the context of the application
     * @return the tequila key if there is one stored inside the SharedPreferences otherwise it returns an empty string
     */
    public String getTequilaKey(Context context) {
        SharedPreferences prefs = context.
                getSharedPreferences(TequilaAuthenticationAPI.AUTHENTICATION_PREFERENCES_NAME, Context.MODE_PRIVATE);
        return prefs.getString(AUTHENTICATION_TEQUILA_KEY, "");
    }

    /**
     * 
     * @return the URL to log on IS Academia
     */
    public String getIsAcademiaLoginURL() {
        return this.isAcademiaLoginURL;
    }

    /**
     * 
     * @return The URL to authenticate on Tequila
     */
    public String getTequilaAuthenticationURL() {
        return this.tequilaAuthenticationURL;
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
}

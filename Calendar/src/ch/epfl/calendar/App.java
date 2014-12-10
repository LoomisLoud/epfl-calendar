/**
 *
 */
package ch.epfl.calendar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import android.app.ActionBar;
import android.app.Application;
import android.content.Context;
import ch.epfl.calendar.persistence.DBHelper;

/**
 * @author lweingart
 * 
 */
public class App extends Application {

    /**
     * Database file name.
     */
    public static final String DATABASE_NAME = "calendar.db";

    /**
     * Default value for event course name.
     */
    public static final String NO_COURSE = "NoCourse";

    /**
     * Database version. This number must be increased whenever the database
     * schema is upgraded in order to trigger the
     * {@link SQLiteOpenHelper#onUpgrade(android.database.sqlite.SQLiteDatabase, int, int)}
     * method.
     */
    public static final int DATABASE_VERSION = 2;

    /**
     * Index 0 (constant created to be checkstyle compliant).
     */
    public static final int ZERO_INDEX = 0;

    /**
     * Index value of the beginning of the field time in a calendar string.
     */
    public static final int START_TIME_INDEX = 11;

    /**
     * Index value of the end of the field date in a calendar string
     */
    public static final int END_DATE_INDEX = 10;

    /**
     * The boolean true in String
     */
    public static final String TRUE = "true";

    /**
     * The boolean false in String
     */
    public static final String FALSE = "false";

    /**
     * Length of the date part in a split calendar string.
     */
    private static final int DATE_PARTS_LENGTH = 3;

    /**
     * Length of the hour part in a split calendar string.
     */
    private static final int HOUR_PARTS_LENGTH = 2;

    /**
     * Smallest index of a day in a month.
     */
    private static final int DAY_MIN = 1;

    /**
     * Greatest index of a day in a month.
     */
    private static final int DAY_MAX = 31;

    // In a gregorian java calendar, a month starts at 0 and ends at 11
    /**
     * Smallest index of a month in a year.
     */
    private static final int MONTH_MIN = 0;

    /**
     * Greatest index of a month in a year.
     */
    private static final int MONTH_MAX = 11;

    /**
     * 1st year of the epoch.
     */
    private static final int YEAR_MIN = 1970;

    /**
     * 1st hour in a day.
     */
    private static final int HOUR_MIN = 0;

    /**
     * Last hour in a day.
     */
    private static final int HOUR_MAX = 23;

    /**
     * 1st minute in an hour.
     */
    private static final int MINUTE_MIN = 0;

    /**
     * Last minute in an hour.
     */
    private static final int MINUTE_MAX = 59;
    
    private static final String DATE_FORMAT = "dd.MM.yyyy";
    
    private static final String HOUR_12_FORMAT = "hh:mm aa";

    private static final String HOUR_24_FORMAT = "HH:mm";

    /**
     * Database helper.
     */
    private static DBHelper mDBHelper;

    /**
     * Application context.
     */
    private static Context mContext;

    /**
     * DefaultActionBarActivity
     */
    private static DefaultActionBarActivity mActionBar;
    
    /**
     * The username of the current user.
     */
    private static String mCurrentUsername;

    @Override
    public void onCreate() {
        super.onCreate();
        App.mContext = this.getApplicationContext();
        App.mDBHelper = new DBHelper(App.mContext, App.DATABASE_NAME);
    }

    /**
     * 
     * @return The {@link DBHelper} of the application.
     */
    public static DBHelper getDBHelper() {
        return App.mDBHelper;
    }

    /**
     * Creates a new {@link DBHelper} with the new database name databaseName.
     * @param databaseName
     */
    public static void setDBHelper(String databaseName) {
        mDBHelper = new DBHelper(App.mContext, databaseName);
    }

    /**
     * 
     * @return the context of this class.
     */
    public static Context getAppContext() {
        return App.mContext;
    }

    /**
     * Parse a csv string into an array list of strings.
     * 
     * @param csv
     * @return an {@link ArrayList} of {@link String} containing the elements parsed from the csv.
     */
    public static ArrayList<String> parseFromCSVString(String csv) {
        String[] ary = csv.split(",");
        ArrayList<String> result = new ArrayList<String>();
        for (int i = 0; i < ary.length; i++) {
            result.add(ary[i]);
        }
        return result;
    }

    /**
     * Transform an array list of string into a csv string.
     * 
     * @param array
     * @return a csv {@link String} constructed from the {@link List} given as parameter.
     */
    public static String csvStringFromList(List<String> list) {
        ArrayList<String> array = new ArrayList<String>(list);
        return new String(array.toString().replace("[", "").replace(", ", ",")
                .replace("]", ""));
    }

    /**
     * Create a calendar object from two strings. date must be of format
     * dd.mm.yyy hourArg must be of format hh:mm
     * 
     * @param date format : dd.mm.yy
     * @param hourArg format : hh:mm
     * @return a {@link Calendar} object corresponding to the date define by the two parameters.
     */
    public static Calendar createCalendar(String date, String hourArg) {
        if (date != null && hourArg != null) {
            // Format of date : dd.mm.yyyy
            String[] dateParts = date.split("\\.");
            if (dateParts.length != DATE_PARTS_LENGTH) {
                // Exception catched in ISAXMLParser
                throw new IllegalArgumentException("Parsing date failed");
            }
            // Format of hour : hh:mm
            String[] timeParts = hourArg.split("\\:");
            if (timeParts.length != HOUR_PARTS_LENGTH) {
                throw new IllegalArgumentException("Parsing date failed");
            }
            int year = Integer.parseInt(dateParts[2]);
            int month = Integer.parseInt(dateParts[1]) - 1;
            int day = Integer.parseInt(dateParts[0]);
            int hour = Integer.parseInt(timeParts[0]);
            int minute = Integer.parseInt(timeParts[1]);

            // Day
            if (day < DAY_MIN || day > DAY_MAX) {
                throw new IllegalArgumentException("Parsed date is impossible");
            }
            if (month < MONTH_MIN || month > MONTH_MAX) {
                throw new IllegalArgumentException("Parsed date is impossible");
            }
            if (year < YEAR_MIN) {
                throw new IllegalArgumentException("Parsed date is impossible");
            }
            if (minute < MINUTE_MIN || minute > MINUTE_MAX) {
                throw new IllegalArgumentException("Parsed date is impossible");
            }
            if (hour < HOUR_MIN || hour > HOUR_MAX) {
                throw new IllegalArgumentException("Parsed date is impossible");
            }
            return new GregorianCalendar(year, month, day, hour, minute);
        } else {
            throw new NullPointerException(
                    "Date or Hour is null in createCalendar()");
        }
    }

    /**
     * Method to create a {@link String} of the form 'dd.mm.yyy hh:mm' from a {@link Calendar}
     * 
     * @param date
     * @return a {@link String} of the form 'dd.mm.yyy hh:mm' create from the {@link Calendar} object date.
     */
    public static String calendarToBasicFormatString(Calendar date) {        
        if (date == null) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT + " " + HOUR_24_FORMAT);        
        return sdf.format(date.getTime());
    }
    
    /**
     * Method to write a calendar in the form 'dd.mm.yyy hh:mm-hh2:mm2' if they are on the same day
     * 
     * @param date
     * @return
     */
    public static String[] calendarToBasicFormatStringSameDaySpecialFormat(Calendar date, Calendar date2) {
        if (date == null || date2 == null) {
            return null;
        }
        String stringDateFormat = DATE_FORMAT;
        String stringHourFormat = HOUR_12_FORMAT;
        SimpleDateFormat sdfDate = new SimpleDateFormat(stringDateFormat, Locale.US);
        SimpleDateFormat sdfTime = new SimpleDateFormat(stringHourFormat, Locale.US);
        
        String datesToReturn = null;
        // compare to see if both date are on the same days
        if (date.get(Calendar.YEAR) == date2.get(Calendar.YEAR)
                && date.get(Calendar.DAY_OF_YEAR) == date2.get(Calendar.DAY_OF_YEAR)) {
            datesToReturn = sdfDate.format(date.getTime());
        } else {
            datesToReturn = sdfDate.format(date.getTime()) + "-" + sdfDate.format(date2.getTime());
        }

        String hoursToReturn = sdfTime.format(date.getTime()) + "-" + sdfTime.format(date2.getTime());
        
        return new String[] {datesToReturn, hoursToReturn};
    }

    /**
     * 
     * @param date
     * @return a String representing the calendar in  format "hh:mm"
     */
    public static String calendarHourToBasicFormatString(Calendar date) {
        if (date == null) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(HOUR_24_FORMAT);
        return sdf.format(date.getTime());
    }
    
    /**
     * @param date
     * @return the hour contained in the Calendar in a String formated "hh:mm aa" where aa parses to AM/PM.
     */
    public static String calendarTo12HoursString(Calendar date) {
        if (date == null) {
            return null;
        }
        String format = HOUR_12_FORMAT;
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.US);
        return sdf.format(date.getTime());
    }

    /**
     * 
     * @param bool
     * @return "true" if bool == true, "false" otherwise.
     */
    public static String boolToString(boolean bool) {
        if (bool) {
            return App.TRUE;
        } else {
            return App.FALSE;
        }
    }
    
    /**
     * 
     * @param strBool
     * @return true if strBool.equals("true"), false otherwise.
     */
    public static boolean stringToBool(String strBool) {
        if (strBool == null) {
            return false;
        }
        return strBool.equals(App.TRUE);
    }

    /**
     * 
     * @return The action bar corresponding to the application.
     */
    public static DefaultActionBarActivity getActionBar() {
        return mActionBar;
    }

    /**
     * Sets the {@link DefaultActionBarActivity} of the application.
     * @param actionBar
     */
    public static void setActionBar(DefaultActionBarActivity actionBar) {
        App.mActionBar = actionBar;
    }

    /**
     * 
     * @return the username of the currently connected user
     */
    public static String getCurrentUsername() {
        return mCurrentUsername;
    }

    /**
     * changes the current username.
     * @param currentUsername
     */
    public static void setCurrentUsername(String currentUsername) {
        App.mCurrentUsername = currentUsername;
    }
}

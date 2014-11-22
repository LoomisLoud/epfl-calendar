/**
 *
 */
package ch.epfl.calendar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import ch.epfl.calendar.persistence.DBHelper;
import android.app.Application;
import android.content.Context;

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
	 * Database version. This number must be increased whenever the database
	 * schema is upgraded in order to trigger the
	 * {@link SQLiteOpenHelper#onUpgrade(android.database.sqlite.SQLiteDatabase, int, int)}
	 * method.
	 */
	public static final int DATABASE_VERSION = 1;

	/**
	 * Index 0.
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
	 * Database helper.
	 */
	private static DBHelper mDBHelper;

	/**
	 * Application context.
	 */
	private static Context mContext;

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

	/**
	 * 10 in a constant.
	 */
	private static final int NUMERIC_TEN = 10;


	@Override
	public void onCreate() {
		super.onCreate();
		App.mContext = this.getApplicationContext();
		App.mDBHelper = new DBHelper(App.mContext);
	}

	public static DBHelper getDBHelper() {
		return App.mDBHelper;
	}

	public static Context getAppContext() {
		return App.mContext;
	}

	/**
	 * Parse a csv string into an array list of strings.
	 *
	 * @param csv
	 * @return
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
	 * @return
	 */
	public static String csvStringFromList(List<String> list) {
		ArrayList<String> array = new ArrayList<String>(list);
		return new String(array.toString()
				.replace("[", "")
				.replace(", ", ",")
				.replace("]", ""));
	}

	/**
	 * Create a calendar object from two strings.
	 * date must be of format dd.mm.yyy
	 * hourArg must be of format hh:mm
	 *
	 * @param date
	 * @param hourArg
	 * @return
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
     * Method to write a calendar in the form 'dd.mm.yyy hh:mm'
     *
     * @param date
     * @return
     */
    public static String calendarToBasicFormatString(Calendar date) {
    	String dd;
    	String mm;
    	String yyyy = Integer.toString(date.get(Calendar.YEAR));
    	String hh;
    	String min;

    	int day = date.get(Calendar.DAY_OF_MONTH);
    	dd = Integer.toString(day);
    	if (day < NUMERIC_TEN) {
    		dd = "0".concat(dd);
		}

    	int month = date.get(Calendar.MONTH);
    	mm = Integer.toString(month);
    	if (month < NUMERIC_TEN) {
			mm = "0".concat(mm);
		}

    	int hour = date.get(Calendar.HOUR_OF_DAY);
    	hh = Integer.toString(hour);
    	if (hour < NUMERIC_TEN) {
			hh = "0".concat(hh);
		}

    	int minutes = date.get(Calendar.MINUTE);
    	min = Integer.toString(minutes);
    	if (minutes < NUMERIC_TEN) {
			min = "0".concat(min);
		}

    	return dd+"."+mm+"."+yyyy+" "+hh+":"+min;
    }
}

/**
 *
 */
package ch.epfl.calendar.tests;

import java.util.ArrayList;
import java.util.GregorianCalendar;

import junit.framework.TestCase;
import android.util.Log;
import ch.epfl.calendar.App;
import ch.epfl.calendar.data.Period;

/**
 * Test class for the App class.
 * 
 * @author lweingart
 * 
 */
public class AppTest extends TestCase {

    private static final String CSV = "foo,bar,foobar";
    private static final ArrayList<String> ARRAY = constructArray();
    private static final String CALENDAR_IN_STRING = "03.09.2007 08:36";
    private static final int YEAR = 2007;
    // Month for GregorianCalendar begin at 0 for January
    private static final int MONTH = 8;
    private static final int DAY = 3;
    private static final int HOUR = 8;
    private static final int MINUTES = 36;

    public void testParseFromCSVString() {
        ArrayList<String> expectedResult = ARRAY;
        ArrayList<String> result = App.parseFromCSVString(CSV);
        Log.i("expected result = ", expectedResult.toString());
        Log.i("result = ", result.toString());
        assertEquals(expectedResult, result);
    }

    public void testCsvStringFromList() {
        String expectedResult = CSV;
        String result = App.csvStringFromList(ARRAY);
        Log.i("expected result = ", expectedResult);
        Log.i("result = ", result);
        assertEquals(expectedResult, result);
    }

    private static ArrayList<String> constructArray() {
        ArrayList<String> result = new ArrayList<String>();
        result.add("foo");
        result.add("bar");
        result.add("foobar");
        return result;
    }

    public static void testCreateCalendar() {
        try {
            // Null Period
            new Period(null, null, null, null, null, null);
            fail("Should have thrown NullPointerException");
        } catch (NullPointerException e) {
            // success
        }

        try {
            // Bad format of date
            new Period("", "16:15", "17:15", null, null, null);
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // success
        }

        try {
            // Bad format of start date
            new Period("16.10.2014", "", "17:15", null, null, null);
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // success
        }

        try {
            // Bad format of end date
            new Period("16.10.2014", "16:15", "", null, null, null);
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // success
        }

        try {
            // Bad format of day
            new Period("-1.10.2014", "16:15", "17:15", null, null, null);
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // success
        }

        try {
            // Bad format of day
            new Period("32.10.2014", "16:15", "17:15", null, null, null);
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // success
        }

        try {
            // Bad format of month
            new Period("16.00.2014", "16:15", "17:15", null, null, null);
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // success
        }

        try {
            // Bad format of month
            new Period("16.13.2014", "16:15", "17:15", null, null, null);
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // success
        }

        try {
            // Bad format of year
            new Period("16.01.1969", "16:15", "17:15", null, null, null);
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // success
        }

        try {
            // Bad format of hour
            new Period("16.01.2014", "-1:15", "17:15", null, null, null);
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // success
        }

        try {
            // Bad format of hour
            new Period("16.01.2014", "24:15", "17:15", null, null, null);
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // success
        }

        try {
            // Bad format of minute
            new Period("16.01.2014", "16:-1", "17:15", null, null, null);
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // success
        }

        try {
            // Bad format of minute
            new Period("16.01.2014", "16:60", "17:15", null, null, null);
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // success
        }
    }

    public static void testCalendarToBasicFormatString() {
        GregorianCalendar cal = new GregorianCalendar(YEAR, MONTH, DAY, HOUR,
                MINUTES);
        String expectedResult = CALENDAR_IN_STRING;
        String result = App.calendarToBasicFormatString(cal);
        assertEquals(expectedResult, result);
        
        cal = new GregorianCalendar(YEAR, MONTH, DAY, 20,
                MINUTES);
        expectedResult = "03.09.2007 20:36";
        result = App.calendarToBasicFormatString(cal);
        assertEquals(expectedResult, result);
        
        result = App.calendarToBasicFormatString(null);
        assertEquals(null, result);
    }
    
    public static void testCalendarHourToBasicFormatString() {
        GregorianCalendar cal = new GregorianCalendar(YEAR, MONTH, DAY, HOUR,
                MINUTES);
        String expectedResult = "08:36";
        
        String returned = App.calendarHourToBasicFormatString(null);
        assertEquals(null, returned);
        
        returned = App.calendarHourToBasicFormatString(cal);
        assertEquals(expectedResult, returned);
        
        cal = new GregorianCalendar(YEAR, MONTH, DAY, 20,
                MINUTES);
        expectedResult = "20:36";
        returned = App.calendarHourToBasicFormatString(cal);
        assertEquals(expectedResult, returned);
    }
    
    public static void testCalendarToBasicFormatStringSameDaySpecialFormat() {
        GregorianCalendar cal = new GregorianCalendar(YEAR, MONTH, DAY, HOUR,
                MINUTES);
        GregorianCalendar cal2 = new GregorianCalendar(YEAR, MONTH, DAY, HOUR+8,
                MINUTES);
        String expectedDate = "03.09.2007";
        String expectedHour = "08:36 AM-04:36 PM";
        
        String[] returned = App.calendarToBasicFormatStringSameDaySpecialFormat(null, null);
        assertEquals(null, returned);
        
        returned = App.calendarToBasicFormatStringSameDaySpecialFormat(cal, cal2);
        assertEquals(expectedDate, returned[0]);
        assertEquals(expectedHour, returned[1]);
        
        cal2 = new GregorianCalendar(YEAR, MONTH, DAY+1, HOUR+8,
                MINUTES);
        expectedDate = "03.09.2007-04.09.2007";
        returned = App.calendarToBasicFormatStringSameDaySpecialFormat(cal, cal2);
        assertEquals(expectedDate, returned[0]);
        assertEquals(expectedHour, returned[1]);
    }
    
    public static void testCalendarTo12HoursString() {
        GregorianCalendar cal = new GregorianCalendar(YEAR, MONTH, DAY, HOUR,
                MINUTES);
        
        String returned = App.calendarTo12HoursString(null);
        assertEquals(null, returned);
        
        returned = App.calendarTo12HoursString(cal);
        assertEquals("08:36 AM", returned);
        
        cal = new GregorianCalendar(YEAR, MONTH, DAY, HOUR+8,
                MINUTES);
        returned = App.calendarTo12HoursString(cal);
        assertEquals("04:36 PM", returned);
    }
    
    public static void testBoolToString() {
        String returned = App.boolToString(true);
        assertEquals("true", returned);
        
        returned = App.boolToString(false);
        assertEquals("false", returned);
    }
    
    public static void testStringToBool() {
        boolean returned = App.stringToBool(null);
        assertEquals(false, returned);
        
        returned = App.stringToBool("true");
        assertEquals(true, returned);
        
        returned = App.stringToBool("false");
        assertEquals(false, returned);
    }
}
/**
 *
 */
package ch.epfl.calendar.test;

import java.util.ArrayList;

import android.util.Log;

import ch.epfl.calendar.App;
import ch.epfl.calendar.data.Period;

import junit.framework.TestCase;

/**
 * Test class for the App class.
 *
 * @author lweingart
 *
 */
public class AppTest extends TestCase {

	public static final String CSV = "foo,bar,foobar";
	public static final ArrayList<String> ARRAY = constructArray();


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

	public void testCreateCalendar() {
		try {
			//Null Period
			new Period(null, null, null, null, null);
			fail("Should have thrown NullPointerException");
		} catch (NullPointerException e) {
			//success
		}

		try {
			//Bad format of date
			new Period("", "16:15", "17:15", null, null);
			fail("Should have thrown IllegalArgumentException");
		} catch (IllegalArgumentException e) {
			//success
		}

		try {
			//Bad format of start date
			new Period("16.10.2014", "", "17:15", null, null);
			fail("Should have thrown IllegalArgumentException");
		} catch (IllegalArgumentException e) {
			//success
		}

		try {
			//Bad format of end date
			new Period("16.10.2014", "16:15", "", null, null);
			fail("Should have thrown IllegalArgumentException");
		} catch (IllegalArgumentException e) {
			//success
		}

		try {
			//Bad format of day
			new Period("-1.10.2014", "16:15", "17:15", null, null);
			fail("Should have thrown IllegalArgumentException");
		} catch (IllegalArgumentException e) {
			//success
		}

		try {
			//Bad format of day
			new Period("32.10.2014", "16:15", "17:15", null, null);
			fail("Should have thrown IllegalArgumentException");
		} catch (IllegalArgumentException e) {
			//success
		}

		try {
			//Bad format of month
			new Period("16.00.2014", "16:15", "17:15", null, null);
			fail("Should have thrown IllegalArgumentException");
		} catch (IllegalArgumentException e) {
			//success
		}

		try {
			//Bad format of month
			new Period("16.13.2014", "16:15", "17:15", null, null);
			fail("Should have thrown IllegalArgumentException");
		} catch (IllegalArgumentException e) {
			//success
		}

		try {
			//Bad format of year
			new Period("16.01.1969", "16:15", "17:15", null, null);
			fail("Should have thrown IllegalArgumentException");
		} catch (IllegalArgumentException e) {
			//success
		}

		try {
			//Bad format of hour
			new Period("16.01.2014", "-1:15", "17:15", null, null);
			fail("Should have thrown IllegalArgumentException");
		} catch (IllegalArgumentException e) {
			//success
		}

		try {
			//Bad format of hour
			new Period("16.01.2014", "24:15", "17:15", null, null);
			fail("Should have thrown IllegalArgumentException");
		} catch (IllegalArgumentException e) {
			//success
		}

		try {
			//Bad format of minute
			new Period("16.01.2014", "16:-1", "17:15", null, null);
			fail("Should have thrown IllegalArgumentException");
		} catch (IllegalArgumentException e) {
			//success
		}

		try {
			//Bad format of minute
			new Period("16.01.2014", "16:60", "17:15", null, null);
			fail("Should have thrown IllegalArgumentException");
		} catch (IllegalArgumentException e) {
			//success
		}
	}
}
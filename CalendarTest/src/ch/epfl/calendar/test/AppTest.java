/**
 *
 */
package ch.epfl.calendar.test;

import java.util.ArrayList;

import android.util.Log;

import ch.epfl.calendar.App;

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
}

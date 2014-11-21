/**
 *
 */
package ch.epfl.calendar.data.tests;

import java.util.ArrayList;
import java.util.Calendar;

import junit.framework.TestCase;
import ch.epfl.calendar.data.Period;
import ch.epfl.calendar.data.PeriodType;

/**
 * @author LoomisLoud
 *
 */
public class PeriodTest extends TestCase {

	private Period mPeriod;


	public void testInvertedDate() {
		mPeriod = new Period("16.01.2014", "17:15", "16:15", null, null);
		Period normalPeriod = new Period("16.01.2014", "16:15", "17:15", null, null);
		assertEquals(mPeriod.getStartDate(), normalPeriod.getStartDate());
		assertEquals(mPeriod.getEndDate(), normalPeriod.getEndDate());
	}

	public void testSetters() {
		mPeriod = new Period("16.01.2014", "16:15", "17:15", "cours", null);
		ArrayList<String> testArray = null;
		Calendar testCal = null;
		try {
			String nullString = null;
			mPeriod.setType(nullString);
			fail("Should have thrown NullPointerException");
		} catch (NullPointerException e) {
			// success
		}

		try {
			PeriodType nullString = null;
			mPeriod.setType(nullString);
			fail("Should have thrown NullPointerException");
		} catch (NullPointerException e) {
			// success
		}

		mPeriod.setType("courss");
		assertEquals(mPeriod.getType(), PeriodType.DEFAULT);

		mPeriod.setType("");
		assertEquals(mPeriod.getType(), PeriodType.DEFAULT);

		mPeriod.setType("projet");
		assertEquals(mPeriod.getType(), PeriodType.PROJECT);

		mPeriod.setType("project");
		assertEquals(mPeriod.getType(), PeriodType.PROJECT);

		mPeriod.setType("exercices");
		assertEquals(mPeriod.getType(), PeriodType.EXERCISES);

		mPeriod.setType("exercises");
		assertEquals(mPeriod.getType(), PeriodType.EXERCISES);

		mPeriod.setType("cours");
		assertEquals(mPeriod.getType(), PeriodType.LECTURE);

		mPeriod.setType("lecture");
		assertEquals(mPeriod.getType(), PeriodType.LECTURE);

		mPeriod.setType(PeriodType.DEFAULT);
		assertEquals(mPeriod.getType(), PeriodType.DEFAULT);

		mPeriod.setType(PeriodType.EXERCISES);
		assertEquals(mPeriod.getType(), PeriodType.EXERCISES);

		mPeriod.setType(PeriodType.LECTURE);
		assertEquals(mPeriod.getType(), PeriodType.LECTURE);

		mPeriod.setType(PeriodType.PROJECT);
		assertEquals(mPeriod.getType(), PeriodType.PROJECT);

		try {
			mPeriod.setRooms(testArray);
			fail("Should have thrown NullPointerException");
		} catch (NullPointerException e) {
			//success
		}

		testArray = new ArrayList<String>();
		testArray.add("Room1");
		testArray.add("Room2");

		mPeriod.setRooms(testArray);
		assertEquals(mPeriod.getRooms().get(0), "Room1");
		assertEquals(mPeriod.getRooms().get(1), "Room2");

		try {
			mPeriod.setStartDate(testCal);
			fail("Should have thrown NullPointerException");
		} catch (NullPointerException e) {
			//success
		}

		try {
			mPeriod.setEndDate(testCal);
			fail("Should have thrown NullPointerException");
		} catch (NullPointerException e) {
			//success
		}

		Period p = new Period("10.10.2010", "10:12", "10:15", null, null);
		mPeriod.setStartDate(p.getStartDate());
		assertEquals(mPeriod.getStartDate(), p.getStartDate());

		mPeriod.setEndDate(p.getEndDate());
		assertEquals(mPeriod.getEndDate(), p.getEndDate());
	}
}

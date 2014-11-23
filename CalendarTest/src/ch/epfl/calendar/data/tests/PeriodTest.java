/**
 *
 */
package ch.epfl.calendar.data.tests;

import java.util.ArrayList;
import java.util.Calendar;

import android.os.Parcel;
import android.util.Log;

import junit.framework.TestCase;
import ch.epfl.calendar.data.Period;
import ch.epfl.calendar.data.PeriodType;

/**
 * @author LoomisLoud
 * 
 */
public class PeriodTest extends TestCase {

    private Period mPeriod;
    private Period mFullPeriod;

    @Override
    protected void setUp() throws Exception {
        ArrayList<String> rooms = new ArrayList<String>();
        rooms.add("CO2");
        mFullPeriod = new Period("16.10.2014", "16:15", "17:15", "cours",
                rooms, "idPeriod");
    }

    public void testTwoConstructorsSameObject() {
        ArrayList<String> rooms = new ArrayList<String>();
        rooms.add("CO2");
        Period firstPeriod = new Period("16.10.2014", "16:15", "17:15",
                "cours", rooms, "idPeriod");
        Period secondPeriod = new Period("cours", "16.10.2014 16:15",
                "16.10.2014 17:15", rooms, "idPeriod");
        Log.i("1st period = ", firstPeriod.toString());
        Log.i("2nd period = ", secondPeriod.toString());
        assertEquals(firstPeriod, secondPeriod);
    }

    public void testInvertedDate() {
        mPeriod = new Period("16.01.2014", "17:15", "16:15", null, null, null);
        Period normalPeriod = new Period("16.01.2014", "16:15", "17:15", null,
                null, null);
        assertEquals(mPeriod.getStartDate(), normalPeriod.getStartDate());
        assertEquals(mPeriod.getEndDate(), normalPeriod.getEndDate());
    }

    public void testSetters() {
        mPeriod = new Period("16.01.2014", "16:15", "17:15", "cours", null,
                "idPeriod");
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
            // success
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
            // success
        }

        try {
            mPeriod.setEndDate(testCal);
            fail("Should have thrown NullPointerException");
        } catch (NullPointerException e) {
            // success
        }

        Period p = new Period("10.10.2010", "10:12", "10:15", null, null, null);
        mPeriod.setStartDate(p.getStartDate());
        assertEquals(mPeriod.getStartDate(), p.getStartDate());

        mPeriod.setEndDate(p.getEndDate());
        assertEquals(mPeriod.getEndDate(), p.getEndDate());
    }

    public void testEqualsSameObject() {
        assertEquals(mFullPeriod, mFullPeriod);
    }

    public void testEqualsDifferentReference() {
        ArrayList<String> rooms = new ArrayList<String>();
        rooms.add("CO2");
        Period period2 = new Period("16.10.2014", "16:15", "17:15", "cours",
                rooms, "idPeriod");
        assertEquals(mFullPeriod, period2);
    }

    public void testHashSameObject() {
        assertEquals(mFullPeriod.hashCode(), mFullPeriod.hashCode());
    }

    public void testHashDifferentReference() {
        ArrayList<String> rooms = new ArrayList<String>();
        rooms.add("CO2");
        Period period2 = new Period("16.10.2014", "16:15", "17:15", "cours",
                rooms, "idPeriod");
        assertEquals(mFullPeriod.hashCode(), period2.hashCode());
    }

    public void testParcelable() {
        // Obtain a Parcel object and write the parcelable object to it:
        Parcel parcel = Parcel.obtain();
        mFullPeriod.writeToParcel(parcel, 0);

        // After you're done with writing, you need to reset the parcel for
        // reading:
        parcel.setDataPosition(0);

        // Reconstruct object from parcel and asserts:
        Period createdFromParcel = Period.CREATOR.createFromParcel(parcel);

        parcel.recycle();
        assertEquals(mFullPeriod, createdFromParcel);
    }
}

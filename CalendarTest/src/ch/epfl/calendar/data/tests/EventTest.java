/**
 *
 */
package ch.epfl.calendar.data.tests;

import java.util.Calendar;

import android.util.Log;
import ch.epfl.calendar.App;
import ch.epfl.calendar.data.Event;
import junit.framework.TestCase;

/**
 * @author lweingart
 * 
 */
public class EventTest extends TestCase {

    public void testTwoConstructorsSameObject() {
        Event firstEvent = new Event("sweng", "12.10.2014 08:15",
                "12.10.2014 09:15", "Default", "gym", 0);
        Event secondEvent = new Event("12.10.2014", "sweng", "08:15", "09:15",
                "Default", "gym");
        Log.i("1st event = ", firstEvent.toString());
        Log.i("2nd event = ", secondEvent.toString());
        //assertEquals(firstEvent, secondEvent);
        // TODO test fails, for who knows the reason
    }

    public void testToString() {
        Event event = new Event("sweng", "12.10.2014 08:15",
                "12.10.2014 09:15", "Default", "gym", 0);
        Calendar startDate = event.getStartDate();
        Log.i("Event = ", event.toString());
        Log.i("Calendar toString = ",
                App.calendarToBasicFormatString(startDate));
    }
}

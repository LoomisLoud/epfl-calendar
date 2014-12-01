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
    
    public void testFirstConstructor() {
        Event event = new Event("27.11.2014", "27.11.2014", "Sweng project", "08:00", "10:00", 
            "Project", "Sweng", "description", true);
        
        assertEquals("27.11.2014 08:00", App.calendarToBasicFormatString(event.getStartDate()));
        assertEquals("27.11.2014 10:00", App.calendarToBasicFormatString(event.getEndDate()));
        assertEquals("Sweng project", event.getName());
        assertEquals("Project", event.getType());
        assertEquals("Sweng", event.getLinkedCourse());
        assertEquals("description", event.getmDescription());
        assertEquals(true, event.isAutomaticAddedBlock());
    }
    
    public void testSecondConstructor() {
        Event event = new Event("Sweng project", "27.11.2014 08:00", "27.11.2014 10:00", 
            "Project", "Sweng", "description", true, 1);
        
        assertEquals("27.11.2014 08:00", App.calendarToBasicFormatString(event.getStartDate()));
        assertEquals("27.11.2014 10:00", App.calendarToBasicFormatString(event.getEndDate()));
        assertEquals("Sweng project", event.getName());
        assertEquals("Project", event.getType());
        assertEquals("Sweng", event.getLinkedCourse());
        assertEquals("description", event.getmDescription());
        assertEquals(true, event.isAutomaticAddedBlock());
        assertEquals(1, event.getId());
    }

    public void testTwoConstructorsSameObject() {
        Event firstEvent = new Event("sweng", "12.10.2014 08:15",
                "12.10.2014 09:15", "Default", "gym", "description", true, 0);
        Event secondEvent = new Event("12.10.2014", "12.10.2014", "sweng", "08:15", "09:15",
                "Default", "gym", "description", true);
        Log.i("1st event = ", firstEvent.toString());
        Log.i("2nd event = ", secondEvent.toString());
        
        assertEquals(firstEvent, secondEvent);
    }
    
    public void testReversedDates() {
        Event event = new Event("28.11.2014", "27.11.2014", "Sweng project", "10:00", "08:00", 
                "Project", "Sweng", "description", true);
            
        assertEquals("27.11.2014 08:00", App.calendarToBasicFormatString(event.getStartDate()));
        assertEquals("28.11.2014 10:00", App.calendarToBasicFormatString(event.getEndDate()));
        
        Event secondEvent = new Event("Sweng project", "28.11.2014 08:00", "27.11.2014 10:00", 
                "Project", "Sweng", "description", true, 1);
            
        assertEquals("27.11.2014 10:00", App.calendarToBasicFormatString(secondEvent.getStartDate()));
        assertEquals("28.11.2014 08:00", App.calendarToBasicFormatString(secondEvent.getEndDate()));
    }

    public void testToString() {
        Event event = new Event("sweng", "12.10.2014 08:15",
                "12.10.2014 09:15", "Default", "gym", "description", true, 0);
        Calendar startDate = event.getStartDate();
        Log.i("Event = ", event.toString());
        Log.i("Calendar toString = ",
                App.calendarToBasicFormatString(startDate));
    }
}

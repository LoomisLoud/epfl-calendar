/**
 * 
 */
package ch.epfl.calendar.data.tests;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import ch.epfl.calendar.data.Course;
import ch.epfl.calendar.data.Period;

/**
 * Test class for the {@link ch.epfl.calendar.data.Course} class
 * 
 * @author gilbrechbuhler
 *
 */
public class CourseTest {

    @Test
    public void testAddPeriod() {
        Course course = new Course("test", "date", "startTime", "endTime", "cours", new ArrayList<String>());
        Period period = new Period("date2", "startTime2", "endTime2", "exercice", new ArrayList<String>());
        
        course.addPeriod(period);
        List<Period> returnedPeriods = course.getPeriods();
        
        assertEquals(returnedPeriods.size(), 2);
        assertEquals(returnedPeriods.get(1).getDate(), "date2");
        assertEquals(returnedPeriods.get(1).getStartTime(), "startTime2");
        assertEquals(returnedPeriods.get(1).getEndTime(), "endTime2");
        assertEquals(returnedPeriods.get(1).getType(), "exercice");
        assertEquals(returnedPeriods.get(1).getRooms().size(), 0);
    }

}

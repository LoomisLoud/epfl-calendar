/**
 * 
 */
package ch.epfl.calendar.data.tests;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import ch.epfl.calendar.data.Course;
import ch.epfl.calendar.data.Period;
import ch.epfl.calendar.data.PeriodType;

/**
 * Test class for the {@link ch.epfl.calendar.data.Course} class
 * 
 * @author gilbrechbuhler
 *
 */
public class CourseTest extends TestCase {

    public void testAddPeriod() {
        Course course = new Course("test", "16.06.2014", "16:15", "17:15", "cours", new ArrayList<String>());
        Period period = new Period("16.06.2014", "16:15", "17:15", "Exercices", new ArrayList<String>());

        course.addPeriod(period);
        List<Period> returnedPeriods = course.getPeriods();
        
        assertEquals(returnedPeriods.size(), 2);
        //TODO : TESTS
//        assertEquals(returnedPeriods.get(1).getDate(), "date2");
//        assertEquals(returnedPeriods.get(1).getStartTime(), "startTime2");
//        assertEquals(returnedPeriods.get(1).getEndTime(), "endTime2");
        System.out.println(returnedPeriods.get(1).getType().toString());
        assertEquals(returnedPeriods.get(1).getType(), PeriodType.EXERCISES);
        assertEquals(returnedPeriods.get(1).getRooms().size(), 0);
    }
    
    //TODO : Test all type of Period (including DEFAULT, in french, in english)

}

/**
 * 
 */
package ch.epfl.calendar.test;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.junit.Test;

import ch.epfl.calendar.apiInterface.CalendarClient;
import ch.epfl.calendar.apiInterface.CalendarClientException;
import ch.epfl.calendar.apiInterface.CalendarClientInterface;
import ch.epfl.calendar.data.Course;

/**
 * @author gilbrechbuhler
 *
 */
public class CalendarClientTest extends TestCase {

    @Test
    public void testGetISAInformation() {
        CalendarClientInterface fetcher = new CalendarClient();
        List<Course> result = new ArrayList<Course>();
        try {
            result = fetcher.getISAInformations();
            assertNotNull(result);
        } catch (CalendarClientException e) {
            fail("An exception occured while using the interface.");
        }
        assertFalse("The returned colleciton should not be empty", result.isEmpty());
    }

}

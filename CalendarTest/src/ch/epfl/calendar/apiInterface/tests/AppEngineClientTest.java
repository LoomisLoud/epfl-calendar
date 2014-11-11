/**
 *
 */
package ch.epfl.calendar.apiInterface.tests;

import junit.framework.TestCase;
import ch.epfl.calendar.apiInterface.AppEngineClient;
import ch.epfl.calendar.apiInterface.CalendarClientException;
import ch.epfl.calendar.apiInterface.DatabaseInterface;
import ch.epfl.calendar.data.Course;

/**
 * Test class for {@link ch.epfl.calendar.apiInterface.AppEngineClient}
 *
 * @author gilbrechbuhler
 *
 */
public class AppEngineClientTest extends TestCase {

    private DatabaseInterface dbInterface;

    // FIXME : see with Jonas to have a local app engine on the jenkins server.
    /*public void testGetCourseErrors() {
        try {
            dbInterface = new AppEngineClient("http://10.0.2.2:8080");
            Course course = dbInterface.getCourseByCode("notExisitingCode");
            assertNull(course);
        } catch (CalendarClientException calendarClientException) {
            System.out.println("An error occured.");
        }

        try {
            dbInterface = new AppEngineClient("http://10.0:8080");
            dbInterface.getCourseByCode("CS-470");
            fail("Missing exception");
        } catch (CalendarClientException calendarClientExc) {
        }
    }

    public void testGetCourseByCode() {
        try {
            dbInterface = new AppEngineClient("http://10.0.2.2:8080");
            Course course = dbInterface.getCourseByCode("CS-470");
            assertEquals("CS-470", course.getCode());
            assertEquals("", course.getDescription());
            assertEquals("Advanced computer architecture", course.getName());
            assertEquals("Pr. Ienne", course.getTeacher());
            assertEquals(4, course.getCredits());
        } catch (CalendarClientException calendarClientException) {
            fail("An error occured.");
        }
    }

    public void testGetPeriodsByCourseCode() {
        try {
            dbInterface = new AppEngineClient("http://10.0.2.2:8080");
            Course course = dbInterface.getCourseByCode("CS-470");
            assertEquals(2, course.getPeriods().size());
            assertEquals("08.10.2014", course.getPeriods().get(0).getDate());
            assertEquals("16:00", course.getPeriods().get(0).getStartTime());
            assertEquals("18:00", course.getPeriods().get(0).getEndTime());
            assertEquals("exercises", course.getPeriods().get(0).getType());
            assertEquals("GCA331", course.getPeriods().get(0).getRooms().get(0));
            assertEquals("GCB331", course.getPeriods().get(0).getRooms().get(1));
            try {
                course.getPeriods().get(0).getJSONFromPeriod(course.getCode());
            } catch (JSONException jsonException) {
                jsonException.printStackTrace();
            }

            assertEquals("05.10.2014", course.getPeriods().get(1).getDate());
            assertEquals("12:00", course.getPeriods().get(1).getStartTime());
            assertEquals("14:00", course.getPeriods().get(1).getEndTime());
            assertEquals("lecture", course.getPeriods().get(1).getType());
            assertEquals("GCA331", course.getPeriods().get(1).getRooms().get(0));
            assertEquals("GCB331", course.getPeriods().get(1).getRooms().get(1));
        } catch (CalendarClientException calendarClientException) {
            fail("Exception occured");
        }
    }

    public void testCreatePeriod() {
        try {
            dbInterface = new AppEngineClient("http://10.0.2.2:8080");
            Course course = dbInterface.getCourseByCode("CS-472");
            List<String> rooms = new ArrayList<String>();
            rooms.add("CO2");
            Period period = new Period("25.12.2014", "14:00", "16:00", "Labs", rooms);
            course.addPeriod(period);
            dbInterface.createPeriod(period, course.getCode());

            Course newCourse = dbInterface.getCourseByCode("CS-472");
            assertEquals("25.12.2014", newCourse.getPeriods().get(0).getDate());
            assertEquals("14:00", newCourse.getPeriods().get(0).getStartTime());
            assertEquals("16:00", newCourse.getPeriods().get(0).getEndTime());
            assertEquals("Labs", newCourse.getPeriods().get(0).getType());
            assertEquals("CO2", newCourse.getPeriods().get(0).getRooms().get(0));
        } catch (CalendarClientException calendarClientException) {
            fail("Exception occured");
        }
    }*/

    public void testOnlineURL() {
        try {
            dbInterface = new AppEngineClient("http://versatile-hull-742.appspot.com");
            Course course = dbInterface.getCourseByCode("CS-470");
            /*System.out.println(course.getDescription());
            System.out.println("GET DESCRIPTION");
            System.out.println(course.getDescription());*/
            assertEquals("CS-470", course.getCode());
            assertEquals("Augmenter au maximum la performance :\n"
                +"\n\n\nPrincipes de parallelisme au niveau des instructions.\n"
                + "\"Register renaming\".\n"
                +"Prediction et speculation.\n"
                +"\"Simultaneous multithreading\".\n"
                + "VLIWs et techniques de compilation pour ILP.\n"
                +"\"Dynamic binary translation\". \n"
                +"\nProcesseurs embarqués :\n"
                + "\n\n\n"
                +"Particularités par rapport aux processeurs non-embarqués.\n"
                +"Survol des DSP et de leur défis pour la compilation.\n"
                +"Processeurs configurables et customisation.\n", course.getDescription());
            assertEquals("Ienne", course.getTeacher());
            assertEquals(4, course.getCredits());
        } catch (CalendarClientException calendarClientException) {
            fail("An error occured.");
        }
    }
}

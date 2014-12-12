/**
 *
 */
package ch.epfl.calendar.test.apiInterface;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import org.mockito.Mockito;

import ch.epfl.calendar.apiInterface.CalendarClient;
import ch.epfl.calendar.apiInterface.CalendarClientDownloadInterface;
import ch.epfl.calendar.authentication.TequilaAuthenticationTask;
import ch.epfl.calendar.data.Course;
import ch.epfl.calendar.test.utils.MockTestCase;
/**
 * @author gilbrechbuhler
 * 
 */
public class CalendarClientTest extends MockTestCase {

    private static final String COURSE_XML = "<data status=\"Termine\" date=\"20141017 16:08:36\""
            + " key=\"1864682915\" dateFin=\"19.10.2014\" dateDebut=\"13.10.2014\"><study-period>"
            + "<id>1808047617</id><date>13.10.2014</date>"
            + "<duration>105</duration><day>1</day><startTime>14:15</startTime><endTime>16:00</endTime>"
            + "<type><text lang=\"en\">Lecture</text><text lang=\"fr\">Cours</text></type> "
            + "+ <course><id>2258712</id><name><text lang=\"fr\">Algorithms</text></name></course>"
            + "<room><id>2192131</id><code>CO2</code><name><text lang=\"fr\">CO 2</text>"
            + "</name></room></study-period><study-period><id>1808048631</id><date>13.10.2014</date>"
            + "<duration>105</duration><day>1</day><startTime>16:15</startTime><endTime>18:00</endTime>"
            + "<type><text lang=\"en\">Exercises</text><text lang=\"fr\">Exercices</text></type>"
            + "<course><id>2258712</id><name><text lang=\"fr\">Algorithms</text></name></course>"
            + "<room><id>2189182</id><code>GCB331</code><name><text lang=\"fr\">GC B3 31</text></name></room>"
            + "<room><id>2189101</id><code>GCA331</code><name><text lang=\"fr\">GC A3 31</text></name></room>"
            + "<room><id>1614950371</id><code>GCD0386</code><name><text lang=\"fr\">GC D0 386</text></name></room>"
            + "<room><id>2189114</id><code>GCB330</code><name><text lang=\"fr\">GC B3 30</text></name></room>"
            + "</study-period><study-period><id>1808331964</id><date>14.10.2014</date><duration>105</duration>"
            + "<day>2</day><startTime>08:15</startTime><endTime>10:00</endTime><type><text lang=\"en\">"
            + "Lecture</text><text lang=\"fr\">Cours</text></type><course><id>24092923</id><name>"
            + "<text lang=\"fr\">Software engineering</text></name></course><room><id>4255362</id>"
            + "<code>BC02</code><name><text lang=\"fr\">BC 02</text></name></room><room><id>4255327</id>"
            + "<code>BC01</code><name><text lang=\"fr\">BC 01</text></name></room><room><id>4255386</id>"
            + "<code>BC03</code><name><text lang=\"fr\">BC 03</text></name></room><room><id>4255408</id>"
            + "<code>BC04</code><name><text lang=\"fr\">BC 04</text></name></room></study-period>"
            + "</data>";
    private static final int NUMBER_OF_COURSES = 2;
    private static final String[] COURSE_NAMES = {
        "Algorithms", "Software engineering"
    };
    private static final int[] NUMBER_OF_PERIODS_BY_COURSES = {
        2, 1
    };

    private TequilaAuthenticationTask task;
    private CalendarClient calendarClient;

    protected void setUp() throws Exception {
        task = Mockito.mock(TequilaAuthenticationTask.class);
    }

    @SuppressWarnings("unchecked")
    public final void testCallback() throws NoSuchMethodException,
            IllegalAccessException, IllegalArgumentException,
            InvocationTargetException {
        CalendarClientDownloadInterface downloadInterface = Mockito
                .mock(CalendarClientDownloadInterface.class);
        Mockito.doNothing()
                .when(downloadInterface)
                .callbackISAcademia(Mockito.any(Boolean.class),
                        Mockito.any(List.class));
        Mockito.doReturn(COURSE_XML).when(task).getResult();
        calendarClient = Mockito
                .spy(new CalendarClient(null, downloadInterface));
        Mockito.doReturn(task).when(calendarClient).getTask();

        Method callback;
        callback = (CalendarClient.class).getDeclaredMethod("callback",
                boolean.class);
        callback.setAccessible(true);
        callback.invoke(calendarClient, new Object[] {
            true
        });

        List<Course> constructedList = calendarClient.getCourseListForTests();
        assertEquals(NUMBER_OF_COURSES, constructedList.size());
        int i = 0;
        for (Course course : constructedList) {
            assertEquals(COURSE_NAMES[i], course.getName());
            assertEquals(NUMBER_OF_PERIODS_BY_COURSES[i], course.getPeriods()
                    .size());
            i++;
        }
    }

    @SuppressWarnings("unchecked")
    public final void testCallbackWhenNoSuccess() throws NoSuchMethodException,
            IllegalAccessException, IllegalArgumentException,
            InvocationTargetException {
        CalendarClientDownloadInterface downloadInterface = Mockito
                .mock(CalendarClientDownloadInterface.class);
        Mockito.doNothing()
                .when(downloadInterface)
                .callbackISAcademia(Mockito.any(Boolean.class),
                        Mockito.any(List.class));
        calendarClient = Mockito
                .spy(new CalendarClient(null, downloadInterface));
        Mockito.doReturn(task).when(calendarClient).getTask();

        Method callback;
        callback = (CalendarClient.class).getDeclaredMethod("callback",
                boolean.class);
        callback.setAccessible(true);
        callback.invoke(calendarClient, new Object[] {
            false
        });

        List<Course> constructedList = calendarClient.getCourseListForTests();
        assertEquals(0, constructedList.size());
    }

    @SuppressWarnings("unchecked")
    public void testTequilaAuthenticationHandler() {
        CalendarClientDownloadInterface downloadInterface = Mockito
                .mock(CalendarClientDownloadInterface.class);
        Mockito.doNothing()
                .when(downloadInterface)
                .callbackISAcademia(Mockito.any(Boolean.class),
                        Mockito.any(List.class));
        Mockito.doReturn(COURSE_XML).when(task).getResult();
        calendarClient = Mockito
                .spy(new CalendarClient(null, downloadInterface));
        Mockito.doReturn(task).when(calendarClient).getTask();

        calendarClient.tequilaAuthenticationHandlerOnError("Error");
        List<Course> constructedList = calendarClient.getCourseListForTests();
        assertEquals(0, constructedList.size());

        calendarClient.tequilaAuthenticationHandlerOnSuccess("123");
        constructedList = calendarClient.getCourseListForTests();
        assertEquals(NUMBER_OF_COURSES, constructedList.size());
        int i = 0;
        for (Course course : constructedList) {
            assertEquals(COURSE_NAMES[i], course.getName());
            assertEquals(NUMBER_OF_PERIODS_BY_COURSES[i], course.getPeriods()
                    .size());
            i++;
        }
    }
}

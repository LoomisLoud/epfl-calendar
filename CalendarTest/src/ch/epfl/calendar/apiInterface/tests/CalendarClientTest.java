/**
 *
 */
package ch.epfl.calendar.apiInterface.tests;

import org.mockito.Mockito;

import ch.epfl.calendar.apiInterface.CalendarClient;
import ch.epfl.calendar.authentication.TequilaAuthenticationTask;
import ch.epfl.calendar.testing.utils.MockTestCase;
/**
 * @author gilbrechbuhler
 *
 */
public class CalendarClientTest extends MockTestCase {

    private TequilaAuthenticationTask task;
    private CalendarClient calendarClient;

    protected void setUp() throws Exception {
        task = Mockito.mock(TequilaAuthenticationTask.class);
        calendarClient = Mockito.spy(new CalendarClient(null, null));
    }

    public final void testGetIsaInformations() {
        //TODO
    }

    public final void testcallback() {
        //TODO
    }

}

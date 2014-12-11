/**
 *
 */
package ch.epfl.calendar.apiInterface.tests;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import junit.framework.TestCase;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.mockito.Mockito;

import ch.epfl.calendar.apiInterface.AppEngineClient;
import ch.epfl.calendar.apiInterface.CalendarClientException;
import ch.epfl.calendar.data.Course;
import ch.epfl.calendar.testing.utils.MockHttpClient;

/**
 * Test class for {@link ch.epfl.calendar.apiInterface.AppEngineClient}
 *
 * @author gilbrechbuhler
 *
 */
public class AppEngineClientTest extends TestCase {

    private static final int HTTP_OK = 200;
    private static final int HTTP_NOT_OK = 404;
    private static final String CORRECT_URL = "http://www.test.ch/";
    private static final String NOT_EXISTING_URL = "http://not-existing-page.com/";
    private static final String INCORRECT_URL = "http.not.a.good.url.ch";
    private static final String UTF8_ENCODING = "UTF-8";
    private static final String COURSE_JSON_STRING = "{\"code\":\"BIO-341\",\"professorName\":\"Naef\","
            + "\"name\":\"Mod\u00e9lisation math\u00e9matique et computationnelle en biologie\","
            + "\"numberOfCredits\":4,\"description\":\"This course introduces dynamical systems theory for "
            + "modeling simple biological networks. Qualitative analysis of non-linear dynamical models will "
            + "be developed in conjunction with simulations. The focus is on applications to "
            + "biological networks.\"}";
    private static final String COURSE_NAME = "Modélisation mathématique et computationnelle en biologie";
    private static final String COURSE_CODE = "BIO-341";
    private static final int COURSE_CREDITS = 4;
    private static final String COURSE_DESCRIPTION = "This course introduces dynamical systems theory for "
            + "modeling simple biological networks. Qualitative analysis of non-linear dynamical models will "
            + "be developed in conjunction with simulations. The focus is on applications to "
            + "biological networks.";
    private static final String COURSE_TEACHER = "Naef";
    
    private MockHttpClient mockHttpClient;
    private HttpResponse mockHttpResponse;
    private StatusLine mockStatusLine;
    private AppEngineClient appEngineClient;
    private HttpEntity mockHttpEntity;
    private InputStream is;
    
    public void setUp() throws ClientProtocolException, IOException, CalendarClientException {
        mockHttpResponse = Mockito.mock(HttpResponse.class);
        mockStatusLine = Mockito.mock(StatusLine.class);
        mockHttpEntity = Mockito.mock(HttpEntity.class);
        
        appEngineClient = Mockito.spy(new AppEngineClient(CORRECT_URL));
        is = new ByteArrayInputStream(COURSE_JSON_STRING.getBytes(UTF8_ENCODING));
        
        mockHttpClient = new MockHttpClient(mockHttpResponse);
        Mockito.doReturn(mockHttpClient).when(appEngineClient).getHttpClient();
        Mockito.doReturn(HTTP_OK).when(mockStatusLine).getStatusCode();
        Mockito.doReturn(mockStatusLine).when(mockHttpResponse).getStatusLine();
        Mockito.doReturn(mockHttpEntity).when(mockHttpResponse).getEntity();
        Mockito.doReturn(is).when(mockHttpEntity).getContent();
    }
    
    public void testConstructor() {
        try {
            new AppEngineClient(CORRECT_URL);
        } catch (CalendarClientException e) {
            fail("This exception should not be raised.");
        }
    }
    
    public void testConstructorWhenBadUrl() {
        try {
            new AppEngineClient(INCORRECT_URL);
            fail("This exception should not be raised.");
        } catch (CalendarClientException e) {
        }
    }
    
    public void testGetCourseByName() {
        Course returnedCourse = null;
        try {
            returnedCourse = appEngineClient.getCourseByName(COURSE_NAME);
        } catch (CalendarClientException e) {
            fail("This exception should not be raised");
        }
        
        assertEquals(COURSE_NAME, returnedCourse.getName());
        assertEquals(COURSE_CODE, returnedCourse.getCode());
        assertEquals(COURSE_CREDITS, returnedCourse.getCredits());
        assertEquals(COURSE_DESCRIPTION, returnedCourse.getDescription());
        assertEquals(COURSE_TEACHER, returnedCourse.getTeacher());
    }
    
    public void testGetCourseByNameWhenArgNull() {
        try {
            appEngineClient.getCourseByName(null);
            fail("An exception should be raised.");
        } catch (CalendarClientException e) {
        }
    }
    
    public void testGetCourseByCode() {
        Course returnedCourse = null;
        try {
            returnedCourse = appEngineClient.getCourseByCode(COURSE_CODE);
        } catch (CalendarClientException e) {
            fail("This exception should not be raised");
        }
        
        assertEquals(COURSE_NAME, returnedCourse.getName());
        assertEquals(COURSE_CODE, returnedCourse.getCode());
        assertEquals(COURSE_CREDITS, returnedCourse.getCredits());
        assertEquals(COURSE_DESCRIPTION, returnedCourse.getDescription());
        assertEquals(COURSE_TEACHER, returnedCourse.getTeacher());
    }
    
    public void testNotHTTPOKCode() {
        Mockito.doReturn(HTTP_NOT_OK).when(mockStatusLine).getStatusCode();
        try {
            appEngineClient.getCourseByCode(COURSE_CODE);
            fail("An exception should be raised.");
        } catch (CalendarClientException e) {
        }
    }
    
    public void testGetCourseWhenBadUrl() throws CalendarClientException, NoSuchMethodException, 
        IllegalAccessException, IllegalArgumentException {
        AppEngineClient client = new AppEngineClient(CORRECT_URL);
        Method getCourse;
        getCourse = (AppEngineClient.class).getDeclaredMethod("getCourse", String.class);
        getCourse.setAccessible(true);
        
        try {
            getCourse.invoke(client, NOT_EXISTING_URL);
        } catch (InvocationTargetException e) {
            if (!(e.getCause() instanceof CalendarClientException)) {
                fail("CalendarClientException should be raised here.");
            }
        }
    }
}

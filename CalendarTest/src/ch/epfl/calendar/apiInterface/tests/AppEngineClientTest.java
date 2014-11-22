/**
 *
 */
package ch.epfl.calendar.apiInterface.tests;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;
import org.mockito.Mockito;


import ch.epfl.calendar.apiInterface.AppEngineClient;
import ch.epfl.calendar.apiInterface.CalendarClientException;
import ch.epfl.calendar.apiInterface.DatabaseInterface;
import ch.epfl.calendar.data.Course;

import junit.framework.TestCase;

/**
 * Test class for {@link ch.epfl.calendar.apiInterface.AppEngineClient}
 *
 * @author gilbrechbuhler
 *
 */
public class AppEngineClientTest extends TestCase {

    //private DatabaseInterface dbInterface;
    private static final String CORRECT_URL = "http://www.test.ch/";
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
    
    private DefaultHttpClient mockHttpClient;
    private HttpResponse mockHttpResponse;
    private StatusLine mockStatusLine;
    private AppEngineClient appEngineClient;
    private HttpEntity mockHttpEntity;
    private HttpGet httpGet;
    private InputStream is;
    
    public void setUp() throws ClientProtocolException, IOException, CalendarClientException {
        mockHttpClient = Mockito.mock(DefaultHttpClient.class);
        mockHttpResponse = Mockito.mock(HttpResponse.class);
        mockStatusLine = Mockito.mock(StatusLine.class);
        mockHttpEntity = Mockito.mock(HttpEntity.class);
        httpGet = new HttpGet();
        
        appEngineClient = Mockito.spy(new AppEngineClient(CORRECT_URL));
        is = new ByteArrayInputStream(COURSE_JSON_STRING.getBytes(UTF8_ENCODING));
        
        Mockito.doReturn(mockHttpClient).when(appEngineClient).getHttpClient();
        Mockito.doReturn(200).when(mockStatusLine).getStatusCode();
        Mockito.doReturn(mockStatusLine).when(mockHttpResponse).getStatusLine();
        Mockito.doReturn(mockHttpEntity).when(mockHttpResponse).getEntity();
        Mockito.doReturn(is).when(mockHttpEntity).getContent();
        Mockito.doReturn(mockHttpResponse).when(mockHttpClient).execute(httpGet);
    }
    
    public void testConstructor() {
        try {
            AppEngineClient testClient = new AppEngineClient(CORRECT_URL);
        } catch (CalendarClientException e) {
            fail("This exception should not be raised.");
        }
    }
    
    public void testConstructorWhenBadUrl() {
        try {
            AppEngineClient testClient = new AppEngineClient(INCORRECT_URL);
            fail("This exception should not be raised.");
        } catch (CalendarClientException e) {
        }
    }
    
    public void testGetCourseByName() {
        Course returnedCourse = null;
        try {
            returnedCourse = appEngineClient.getCourseByCode("BIO-341");
        } catch (CalendarClientException e) {
            fail("This exception should not be raised");
        }
        
        assertEquals(COURSE_NAME, returnedCourse.getName());
        assertEquals(COURSE_CODE, returnedCourse.getCode());
        assertEquals(COURSE_CREDITS, returnedCourse.getCredits());
        assertEquals(COURSE_DESCRIPTION, returnedCourse.getDescription());
        assertEquals(COURSE_TEACHER, returnedCourse.getTeacher());
    }
}

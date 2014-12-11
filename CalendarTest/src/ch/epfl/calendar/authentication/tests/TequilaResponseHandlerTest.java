package ch.epfl.calendar.authentication.tests;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.message.BasicStatusLine;
import org.mockito.Mockito;

import ch.epfl.calendar.authentication.TequilaResponseHandler;
import ch.epfl.calendar.testing.utils.MockTestCase;

/**
 * Class for testing the TequilaResponseHandler
 * @author AblionGE
 *
 */
public class TequilaResponseHandlerTest extends MockTestCase {
    
    private static final int HTTP_CODE_OK = 200;
    private static final int HTTP_CODE_REDIRECTED = 302;
    private TequilaResponseHandler handler = null;
    private HttpResponse response = null;
    private StatusLine statusLine = null;
    private static final String TAG = "CustomResponseHandler Class:: ";
    
    protected void setUp() throws Exception {
        super.setUp();
        response = Mockito.mock(HttpResponse.class);
        statusLine = Mockito.mock(BasicStatusLine.class);
        Mockito.doReturn(statusLine).when(response).getStatusLine();
        Mockito.doReturn(HTTP_CODE_OK).when(statusLine).getStatusCode();
    }
    
    public final void testTequilaResponseHandlerWithWrongExpectedCode() {
        try {
            handler = new TequilaResponseHandler(HTTP_CODE_REDIRECTED);
            handler.handleResponse(response);
            fail("testTequilaResponseHandlerWithWrongExpectedCode pass...");
        } catch (ClientProtocolException e) {
            if (e.getMessage().equals(TAG + "Wrong Http Code received")) {
                //Waited Exception
            } else {
                fail("Wrong message");
            }
        } catch (IOException e) {
            //We don't test this case here
        }
    }
    
    public final void testTequilaResponseHandlerWithEntityNull() {
        Mockito.doReturn(null).when(response).getEntity();
        handler = new TequilaResponseHandler(HTTP_CODE_OK);
        String result = null;
        try {
            result = handler.handleResponse(response);
        } catch (ClientProtocolException e) {
            fail("ClientProtocolException not waited");
        } catch (IOException e) {
            fail("IOException not waited");
        }
        assertNull(result);
    }
    
    public final void testTequilaResponseHandlerCorrectBehavior() {
        handler = new TequilaResponseHandler(HTTP_CODE_OK);
        HttpEntity entity = Mockito.mock(HttpEntity.class);
        Mockito.doReturn(entity).when(response).getEntity();
        String result = null;
        try {
            Mockito.doReturn(new ByteArrayInputStream("test".getBytes())).when(entity).getContent();
            result = handler.handleResponse(response);
        } catch (IllegalStateException e) {
            fail("IllegalStateException not waited");
        } catch (IOException e) {
            fail("IOException not waited");
        }
        assertEquals("test", result);
    }

}

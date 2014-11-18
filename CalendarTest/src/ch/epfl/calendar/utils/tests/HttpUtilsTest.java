/**
 * 
 */
package ch.epfl.calendar.utils.tests;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.AbstractHttpClient;
import org.mockito.Mockito;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import ch.epfl.calendar.apiInterface.CalendarClientException;
import ch.epfl.calendar.testing.utils.MockTestCase;
import ch.epfl.calendar.utils.HttpUtils;

/**
 * @author gilbrechbuhler
 *
 */
public class HttpUtilsTest extends MockTestCase {
    private Context mContext;
    private HttpUtils mHttpUtils;
    
    private static final String COOKIE_TO_FIND_NAME = "cookie_to_find";
    private static final String LOCATION_VALUE = "location=123456";
    private static final String MALFORMED_LOCATION_VALUE = "location123456";
    private static final String TOKEN_HEADER = "123456";
    private static final String EXCEPTION_SHOULD_BE_RAISED = "An exception should have been raised";
    private static final String EXCEPTION_SHOULD_NOT_BE_RAISED = "The exception should not have been raised";
    
    public void setUp() throws Exception {
        super.setUp();
        mContext = getInstrumentation().getTargetContext();
        mHttpUtils = new HttpUtils();
    }
    
    public void testIsNetworkWorking() {
        Context localContext = Mockito.mock(Context.class);
        ConnectivityManager connMgr = Mockito.mock(ConnectivityManager.class);
        NetworkInfo netInfo = Mockito.mock(NetworkInfo.class);
        Mockito.doReturn(true).when(netInfo).isConnected();
        Mockito.doReturn(netInfo).when(connMgr).getActiveNetworkInfo();
        Mockito.doReturn(connMgr).when(localContext).getSystemService(Mockito.any(String.class));
        boolean testIsConnected = HttpUtils.isNetworkWorking(localContext);
        
        assertEquals(true, testIsConnected);
    }
    
    public void testIsNetworkWorkingWhenNetInfoNull() {
        Context localContext = Mockito.mock(Context.class);
        ConnectivityManager connMgr = Mockito.mock(ConnectivityManager.class);
        Mockito.doReturn(null).when(connMgr).getActiveNetworkInfo();
        Mockito.doReturn(connMgr).when(localContext).getSystemService(Mockito.any(String.class));
        boolean testIsConnected = HttpUtils.isNetworkWorking(localContext);
        
        assertEquals(false, testIsConnected);
    }
    
    public void testIsNetworkWorkingWhenNoConnection() {
        Context localContext = Mockito.mock(Context.class);
        ConnectivityManager connMgr = Mockito.mock(ConnectivityManager.class);
        NetworkInfo netInfo = Mockito.mock(NetworkInfo.class);
        Mockito.doReturn(false).when(netInfo).isConnected();
        Mockito.doReturn(netInfo).when(connMgr).getActiveNetworkInfo();
        Mockito.doReturn(connMgr).when(localContext).getSystemService(Mockito.any(String.class));
        boolean testIsConnected = HttpUtils.isNetworkWorking(localContext);
        
        assertEquals(false, testIsConnected);
    }
    
    public List<Cookie> fillMockList() {
        List<Cookie> cookieList = new ArrayList<Cookie>();
        Cookie c1 = Mockito.mock(Cookie.class);
        Mockito.doReturn("mock_cookie_1").when(c1).getName();
        cookieList.add(c1);
        Cookie c2 = Mockito.mock(Cookie.class);
        Mockito.doReturn("cookie_test").when(c2).getName();
        cookieList.add(c2);
        Cookie c3 = Mockito.mock(Cookie.class);
        Mockito.doReturn("cookie_test").when(c3).getName();
        cookieList.add(c3);
        
        return cookieList;
    }
    
    public void testGetCookieWhenCookieThere() {
        AbstractHttpClient abstractClient = Mockito.mock(AbstractHttpClient.class);
        List<Cookie> cookieList = fillMockList();
        Cookie c = Mockito.mock(Cookie.class);
        CookieStore cookieStore = Mockito.mock(CookieStore.class);
        Mockito.doReturn(cookieList).when(cookieStore).getCookies();
        Mockito.doReturn(cookieStore).when(abstractClient).getCookieStore();
        Mockito.doReturn(COOKIE_TO_FIND_NAME).when(c).getName();
        cookieList.add(c);
        
        Cookie returned = mHttpUtils.getCookie(abstractClient, COOKIE_TO_FIND_NAME);
        assertEquals(c.getName(), returned.getName());
    }
    
    public void testGetCookieWhenCookieNoThere() {
        AbstractHttpClient abstractClient = Mockito.mock(AbstractHttpClient.class);
        List<Cookie> cookieList = fillMockList();
        CookieStore cookieStore = Mockito.mock(CookieStore.class);
        Mockito.doReturn(cookieList).when(cookieStore).getCookies();
        Mockito.doReturn(cookieStore).when(abstractClient).getCookieStore();
        
        Cookie returned = mHttpUtils.getCookie(abstractClient, COOKIE_TO_FIND_NAME);
        assertEquals(null, returned);
    }
    
    public void testGetCookieWhenFieldNameEmpty() {
        AbstractHttpClient abstractClient = Mockito.mock(AbstractHttpClient.class);
        List<Cookie> cookieList = fillMockList();
        Cookie c = Mockito.mock(Cookie.class);
        CookieStore cookieStore = Mockito.mock(CookieStore.class);
        Mockito.doReturn(cookieList).when(cookieStore).getCookies();
        Mockito.doReturn(cookieStore).when(abstractClient).getCookieStore();
        Mockito.doReturn(COOKIE_TO_FIND_NAME).when(c).getName();
        cookieList.add(c);
        
        Cookie returned = mHttpUtils.getCookie(abstractClient, "");
        assertEquals(null, returned);
    }
    
    public void testGetCookieWhenClientNull() {
        Cookie returned = mHttpUtils.getCookie(null, "test");
        assertEquals(null, returned);
    }
    
    public void testGetTokenFromHeader() {
        Header location = Mockito.mock(Header.class);
        Mockito.doReturn(LOCATION_VALUE).when(location).getValue();
        
        String returned = null;
        try {
            returned = mHttpUtils.getTokenFromHeader(location);
        } catch (ClientProtocolException e) {
            e.printStackTrace();
            fail();
        }
        
        assertEquals(TOKEN_HEADER, returned);
    }
    
    public void testGetTokenFromHeaderWhenLocationNull() {
        try {
            mHttpUtils.getTokenFromHeader(null);
            fail(EXCEPTION_SHOULD_BE_RAISED);
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        }
    }
    
    public void testGetTokenFromHeaderWhenLocationNotWellFormated() {
        Header location = Mockito.mock(Header.class);
        Mockito.doReturn(MALFORMED_LOCATION_VALUE).when(location).getValue();
        try {
            mHttpUtils.getTokenFromHeader(location);
            fail(EXCEPTION_SHOULD_BE_RAISED);
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        }
    }
    
    public void testHandleResponse() {
        try {
            HttpUtils.handleResponse(200);
        } catch (CalendarClientException e) {
            e.printStackTrace();
            fail(EXCEPTION_SHOULD_NOT_BE_RAISED);
        }
    }
    
    public void testHandleResponseWhenNot200() {
        try {
            HttpUtils.handleResponse(404);
            fail(EXCEPTION_SHOULD_BE_RAISED);
        } catch (CalendarClientException e) {
            e.printStackTrace();
        }
        
        try {
            HttpUtils.handleResponse(302);
            fail(EXCEPTION_SHOULD_BE_RAISED);
        } catch (CalendarClientException e) {
            e.printStackTrace();
        }
        
        try {
            HttpUtils.handleResponse(500);
            fail(EXCEPTION_SHOULD_BE_RAISED);
        } catch (CalendarClientException e) {
            e.printStackTrace();
        }
        
        try {
            HttpUtils.handleResponse(503);
            fail(EXCEPTION_SHOULD_BE_RAISED);
        } catch (CalendarClientException e) {
            e.printStackTrace();
        }
    }
}

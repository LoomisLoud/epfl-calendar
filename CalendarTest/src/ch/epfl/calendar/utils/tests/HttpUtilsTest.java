/**
 * 
 */
package ch.epfl.calendar.utils.tests;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.AbstractHttpClient;
import org.mockito.Mockito;

import android.content.Context;
import android.net.NetworkInfo;
import ch.epfl.calendar.testing.utils.MockTestCase;
import ch.epfl.calendar.utils.HttpUtils;

/**
 * @author gilbrechbuhler
 *
 */
public class HttpUtilsTest extends MockTestCase {
    /*private Context context;
    
    public void setUp() throws Exception {
        super.setUp();
        context = getInstrumentation().getTargetContext();
    }
    
    public void testGetCookieWhenOK() {
        AbstractHttpClient abstractClient = Mockito.mock(AbstractHttpClient.class);
        List<Cookie> cookieList = new ArrayList<Cookie>();
        Cookie c = Mockito.mock(Cookie.class);
        Mockito.doReturn("cookie_test").when(c)
        cookieList.add(c);
    }*/
    
//    public void testIsNetworkWorkingWhenNetInfoNull() {
//        Mockito.doReturn(null).when(httpUtilsWithoutNet).localGetNetworkInfo(context);
//        assertEquals(false, httpUtilsWithoutNet.isNetworkWorking(context));
//    }
//    
//    /*
//     * We precise here that with Mockito it is impossible to mock a static method
//     */
//    public void testIsNetworkWorkingOK() {
//        assertEquals(true, HttpUtils.isNetworkWorking(context));
//    }
}

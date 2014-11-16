package ch.epfl.calendar.authentication.tests;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicStatusLine;
import org.apache.http.protocol.HttpContext;
import org.mockito.Mockito;

import android.content.Context;
import ch.epfl.calendar.R;
import ch.epfl.calendar.authentication.HttpClientFactory;
import ch.epfl.calendar.authentication.TequilaAuthenticationAPI;
import ch.epfl.calendar.authentication.TequilaAuthenticationException;
import ch.epfl.calendar.authentication.TequilaAuthenticationTask;
import ch.epfl.calendar.authentication.TequilaResponseHandler;
import ch.epfl.calendar.authentication.TequilaAuthenticationTask.TequilaAuthenticationListener;
import ch.epfl.calendar.testing.utils.MockTestCase;
import ch.epfl.calendar.utils.GlobalPreferences;
import ch.epfl.calendar.utils.HttpUtils;
import ch.epfl.calendar.utils.NetworkException;

/**
 * This class tests the TequilaAuthenticationTask class
 * @author AblionGE
 *
 */
public class TequilaAuthenticationTaskTest extends MockTestCase {

    private AbstractHttpClient client = null;
    private HttpResponse response = null;
    private StatusLine statusLine = null;
    private HttpUtils httpUtils = null;
    private TequilaAuthenticationTask task = null;
    private TequilaAuthenticationListener listener = null;
    private Context context = null;
    private static final int HTTP_CODE_OK = 200;
    private static final String SESSIONID = "JSESSIONID";
    private static final String KEY = "key";
    private static final int SIZE_POST_BODY_WITH_CREDENTIALS = 3;
    private static final int SIZE_POST_BODY_WITHOUT_CREDENTIALS = 1;
    private static final String TEQUILA_KEY = "tequila_key";
    private static final String TEQUILA_USER = "tequila_user";


    protected void setUp() throws Exception {
        super.setUp();
        client = Mockito.mock(AbstractHttpClient.class);
        response = Mockito.mock(HttpResponse.class);
        statusLine = Mockito.mock(BasicStatusLine.class);
        task = Mockito.mock(TequilaAuthenticationTask.class);
        httpUtils = Mockito.mock(HttpUtils.class);
        listener = Mockito.mock(TequilaAuthenticationListener.class);
        Mockito.doReturn(client).when(task).getClient();
        Mockito.doReturn(statusLine).when(response).getStatusLine();
        Mockito.doReturn(HTTP_CODE_OK).when(statusLine).getStatusCode();
        context = getInstrumentation().getTargetContext();
    }

    //onPreExecute needs to be tested in an UI test
    
    //onCancelled needs to be tested in an UI test
    
    //onPostExecute needs to be tested in an UI test
    
    public final void testGetAccessToIsa() {
        TequilaAuthenticationTask instance =
                Mockito.spy(new TequilaAuthenticationTask(context, listener, "test", "test"));
        try {
            Method getAccessToIsa;
            getAccessToIsa = (TequilaAuthenticationTask.class).getDeclaredMethod("getAccessToIsa",
                    new Class[] {String.class, String.class});
            getAccessToIsa.setAccessible(true);

            Mockito.doReturn(httpUtils).when(instance).getHttpUtils();
            Mockito.doReturn(response).when(httpUtils).executeGet(Mockito.any(AbstractHttpClient.class),
                    Mockito.any(HttpGet.class), Mockito.any(HttpContext.class));

            HttpEntity httpEntity = Mockito.mock(HttpEntity.class);
            Mockito.doReturn(httpEntity).when(response).getEntity();

            InputStream content = Mockito.mock(InputStream.class);
            Mockito.doReturn(content).when(httpEntity).getContent();

            Mockito.doNothing().when(content).close();

            //With Null args
            int result = (Integer) getAccessToIsa.invoke(instance, new Object[] {null, null});
            assertEquals(HTTP_CODE_OK, result);

            //Only with sessionID
            result = (Integer) getAccessToIsa.invoke(instance, new Object[] {"sessionID", null});
            assertEquals(HTTP_CODE_OK, result);

          //Only with tokenList
            result = (Integer) getAccessToIsa.invoke(instance, new Object[] {null, "tokenList"});
            assertEquals(HTTP_CODE_OK, result);

          //with Both args
            result = (Integer) getAccessToIsa.invoke(instance, new Object[] {"sessionID", "tokenList"});
            assertEquals(HTTP_CODE_OK, result);

        } catch (IllegalAccessException e) {
            fail();
        } catch (IllegalArgumentException e) {
            fail();
        } catch (InvocationTargetException e) {
            fail();
        } catch (ClientProtocolException e) {
            fail();
        } catch (IOException e) {
            fail();
        } catch (NoSuchMethodException e) {
            fail();
        }
    }

    public final void testSetCookiesAndHeaders() {
        TequilaAuthenticationTask instance =
                Mockito.spy(new TequilaAuthenticationTask(context, listener, "test", "test"));
        try {
            Method setCookiesAndHeaders;
            setCookiesAndHeaders = (TequilaAuthenticationTask.class).getDeclaredMethod("setCookiesAndHeaders",
                    new Class[] {HttpGet.class, String.class});
            setCookiesAndHeaders.setAccessible(true);

            HttpGet realGet = new HttpGet();

            //With realGet and HttpResponse null
            Mockito.doReturn(null).when(instance).getRespGetTimetable();
            
            Cookie cookie = new BasicClientCookie("name", "value");
            GlobalPreferences.getInstance().setSessionIDCookie(cookie);

            HttpGet result = (HttpGet) setCookiesAndHeaders.invoke(instance, new Object[] {realGet, "sessionID"});
            assertEquals(SESSIONID + "=" +"sessionID", result.getFirstHeader("Set-Cookie").getValue());

            assertEquals(BasicCookieStore.class, HttpClientFactory.getInstance().getCookieStore().getClass());

            assertTrue(HttpClientFactory.getInstance().getCookieStore().getCookies()
                    .contains(cookie));

            assertEquals(realGet.getAllHeaders().length, 1);

        } catch (IllegalAccessException e) {
            fail();
        } catch (IllegalArgumentException e) {
            fail();
        } catch (InvocationTargetException e) {
            fail();
        } catch (NoSuchMethodException e) {
            fail();
        }
    }
    
    public final void testGetIsaCompleteURL() {
        Method getIsaCompleteURL;
        try {
            getIsaCompleteURL = (TequilaAuthenticationTask.class).getDeclaredMethod("getIsaCompleteURL",
                    new Class[] {String.class});
            getIsaCompleteURL.setAccessible(true);
            
            TequilaAuthenticationTask instance = 
                    new TequilaAuthenticationTask(context, listener, "test", "test");

            //With arg null
            HttpGet result = (HttpGet) getIsaCompleteURL.invoke(instance, new Object[] {null});
            String waitedUrl = TequilaAuthenticationAPI.getInstance().getIsAcademiaLoginURL();
            assertEquals(waitedUrl, result.getURI().toString());

            //With a token
            result = (HttpGet) getIsaCompleteURL.invoke(instance, new Object[] {"token"});
            waitedUrl = TequilaAuthenticationAPI.getInstance().getIsAcademiaLoginURL()+"?"+KEY+"="+"token";
            assertEquals(waitedUrl, result.getURI().toString());
        } catch (NoSuchMethodException e) {
            fail();
        } catch (IllegalAccessException e) {
            fail();
        } catch (IllegalArgumentException e) {
            fail();
        } catch (InvocationTargetException e) {
            fail();
        }
        
    }
    
    public final void testAuthenticateOnTequila() {
        Method authenticateOnTequila;
        
        try {
            authenticateOnTequila = (TequilaAuthenticationTask.class).getDeclaredMethod("authenticateOnTequila",
                    new Class[] {String.class, boolean.class});
            authenticateOnTequila.setAccessible(true);

            TequilaAuthenticationTask instance = 
                    Mockito.spy(new TequilaAuthenticationTask(context, listener, "test", "test"));

            Mockito.doReturn(httpUtils).when(instance).getHttpUtils();
            Mockito.doReturn("result").when(httpUtils).executePost(Mockito.any(AbstractHttpClient.class),
                    Mockito.any(HttpPost.class), Mockito.any(TequilaResponseHandler.class));

            //FirstTtry = true
            authenticateOnTequila.invoke(instance, new Object[] {"token", true});

          //FirstTtry = false
            authenticateOnTequila.invoke(instance, new Object[] {"token", false});

          //Nothing to test, it shouldn't throw any exceptions

          //Test with ClientProtocolException
            Mockito.doThrow(new ClientProtocolException("errorWaited"))
                .when(httpUtils).executePost(Mockito.any(AbstractHttpClient.class),
                    Mockito.any(HttpPost.class), Mockito.any(TequilaResponseHandler.class));
            //Test with exception
            authenticateOnTequila.invoke(instance, new Object[] {"token", false});

            fail();
        } catch (NoSuchMethodException e) {
            fail();
        } catch (ClientProtocolException e) {
            fail();
        } catch (IOException e) {
            fail();
        } catch (IllegalAccessException e) {
            fail();
        } catch (IllegalArgumentException e) {
            fail();
        } catch (InvocationTargetException e) {
            if (e.getTargetException() instanceof TequilaAuthenticationException) {
                if (e.getTargetException().getMessage().equals("errorWaited")) {
                    //Waited
                } else {
                    e.printStackTrace();
                    System.out.println(e.getMessage());
                    fail();
                }
            } else {
                fail();
            }
        }
    }

    public final void testGetTequilaUrl() {
        Method getTequilaUrl;
        try {
            getTequilaUrl = (TequilaAuthenticationTask.class).getDeclaredMethod("getTequilaUrl",
                    new Class[] {});
            getTequilaUrl.setAccessible(true);

            TequilaAuthenticationTask instance = 
                    new TequilaAuthenticationTask(context, listener, "test", "test");

            HttpPost result = (HttpPost) getTequilaUrl.invoke(instance, new Object[] {});

            assertEquals(TequilaAuthenticationAPI.getInstance().getTequilaAuthenticationURL(),
                    result.getURI().toString());
        } catch (NoSuchMethodException e) {
            fail();
        } catch (IllegalAccessException e) {
            fail();
        } catch (IllegalArgumentException e) {
            fail();
        } catch (InvocationTargetException e) {
            fail();
        }
    }

    public final void testsetPostBody() {
        Method setPostBody;
        try {
            setPostBody = (TequilaAuthenticationTask.class).getDeclaredMethod("setPostBody",
                    new Class[] {String.class, boolean.class});
            setPostBody.setAccessible(true);

            //Test with username and password
            TequilaAuthenticationTask instance = 
                    new TequilaAuthenticationTask(context, listener, "test", "test");

            @SuppressWarnings("unchecked")
            List<NameValuePair> result = (List<NameValuePair>) setPostBody
                    .invoke(instance, new Object[] {"token", true});

            assertNotNull(result);
            assertEquals(SIZE_POST_BODY_WITH_CREDENTIALS, result.size());

            //Test without username and password
            instance = 
                    new TequilaAuthenticationTask(context, listener, null, null);

            @SuppressWarnings("unchecked")
            List<NameValuePair> resultWithoutCredentials = (List<NameValuePair>) setPostBody
                    .invoke(instance, new Object[] {"token", false});

            assertNotNull(resultWithoutCredentials);
            assertEquals(SIZE_POST_BODY_WITHOUT_CREDENTIALS, resultWithoutCredentials.size());

        } catch (NoSuchMethodException e) {
            fail();
        } catch (IllegalAccessException e) {
            fail();
        } catch (IllegalArgumentException e) {
            fail();
        } catch (InvocationTargetException e) {
            fail();
        }
    }

    public final void testSetCookiesForTequila() {
        Method setCookiesForTequila;
        try {
            setCookiesForTequila = (TequilaAuthenticationTask.class).getDeclaredMethod("setCookiesForTequila",
                    new Class[] {boolean.class});
            setCookiesForTequila.setAccessible(true);

            TequilaAuthenticationTask instance = 
                    new TequilaAuthenticationTask(context, listener, "test", "test");

            Cookie tequilaKeyCookie = new BasicClientCookie("tequila", "key");
            Cookie tequilaUsernameCookie = new BasicClientCookie("cookie", "username");
            GlobalPreferences.getInstance().setTequilaKeyCookie(tequilaKeyCookie);
            GlobalPreferences.getInstance().setTequilaUsernameCookie(tequilaUsernameCookie);

            setCookiesForTequila.invoke(instance, new Object[] {false});
            assertEquals(BasicCookieStore.class, HttpClientFactory.getInstance().getCookieStore().getClass());

            assertTrue(HttpClientFactory.getInstance().getCookieStore().getCookies()
                    .contains(tequilaKeyCookie));

            assertTrue(HttpClientFactory.getInstance().getCookieStore().getCookies()
                    .contains(tequilaUsernameCookie));

            //Nothing happened
            setCookiesForTequila.invoke(instance, new Object[] {true});

        } catch (NoSuchMethodException e) {
            fail();
        } catch (IllegalAccessException e) {
            fail();
        } catch (IllegalArgumentException e) {
            fail();
        } catch (InvocationTargetException e) {
            fail();
        }
    }

    public final void testStoreCookiesFromTequila() {
        Method storeCookiesFromTequila;
        try {
            storeCookiesFromTequila = (TequilaAuthenticationTask.class).getDeclaredMethod("storeCookiesFromTequila",
                    new Class[] {});
            storeCookiesFromTequila.setAccessible(true);

            TequilaAuthenticationTask instance = 
                    Mockito.spy(new TequilaAuthenticationTask(context, listener, "test", "test"));

            Cookie cookie = new BasicClientCookie("tequila", "cookie");

            Mockito.doReturn(httpUtils).when(instance).getHttpUtils();
            Mockito.doReturn(cookie).when(httpUtils).getCookie(
                    Mockito.any(AbstractHttpClient.class), Mockito.any(String.class));

            storeCookiesFromTequila.invoke(instance, new Object[] {});
            assertEquals(cookie, GlobalPreferences.getInstance().getTequilaKeyCookie());
            assertEquals(cookie, GlobalPreferences.getInstance().getTequilaUsernameCookie());
        } catch (NoSuchMethodException e) {
            fail();
        } catch (IllegalAccessException e) {
            fail();
        } catch (IllegalArgumentException e) {
            fail();
        } catch (InvocationTargetException e) {
            fail();
        }
    }

    public final void testDoInBackgroundVoidArrayWithoutNetworking() {
        Method doInBackground;
        try {
            doInBackground = (TequilaAuthenticationTask.class).getDeclaredMethod("doInBackground",
                    new Class[] {Void.class});
            doInBackground.setAccessible(true);

            
            TequilaAuthenticationTask instance = 
                    Mockito.spy(new TequilaAuthenticationTask(context, listener, "test", "test"));

            Mockito.doReturn(false).when(instance).isNetworkWorking(context);

            doInBackground.invoke(instance, new Object[] {});
        } catch (NoSuchMethodException e) {
            fail(e.getMessage());
        } catch (IllegalAccessException e) {
            fail(e.getMessage());
        } catch (IllegalArgumentException e) {
            fail(e.getMessage());
        } catch (InvocationTargetException e) {
            if (e.getTargetException() instanceof NetworkException) {
                if (e.getTargetException().getMessage().equals(context.getString(R.string.network_unreachable))) {
                    //Waited
                } else {
                    fail();
                }
            } else {
                fail();
            }
        }
    }
    
}

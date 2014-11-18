package ch.epfl.calendar.authentication.tests;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import org.apache.http.Header;
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
    private HttpEntity httpEntity = null;
    private TequilaAuthenticationTask task = null;
    private TequilaAuthenticationListener listener = null;
    private GlobalPreferences globalPreferences = null;
    private TequilaAuthenticationAPI tequilaApi = null;
    private TequilaAuthenticationTask instance = null;
    private Context context = null;
    private static final int HTTP_CODE_OK = 200;
    private static final int HTTP_CODE_REDIRECT = 302;
    private static final int HTTP_CODE_NO_FOUND = 404;
    private static final String SESSIONID = "JSESSIONID";
    private static final String KEY = "key";
    private static final int SIZE_POST_BODY_WITH_CREDENTIALS = 3;
    private static final int SIZE_POST_BODY_WITHOUT_CREDENTIALS = 1;

    private void setUpForAuthentication() throws ClientProtocolException, IOException {
        //Network works
        Mockito.doReturn(true).when(instance).isNetworkWorking(context);
        //Authenticated = true
        Mockito.doReturn(true).when(globalPreferences).isAuthenticated(Mockito.any(Context.class));
        Mockito.doReturn("test").when(tequilaApi).getSessionID(Mockito.any(Context.class));
        Mockito.doReturn("test").when(tequilaApi).getUsername(Mockito.any(Context.class));
        Mockito.doReturn("test").when(tequilaApi).getTequilaKey(Mockito.any(Context.class));

        //For the HttpGet
        Mockito.doReturn(httpUtils).when(instance).getHttpUtils();
        Mockito.doReturn(response).when(httpUtils).executeGet(Mockito.any(AbstractHttpClient.class),
                Mockito.any(HttpGet.class), Mockito.any(HttpContext.class));

        httpEntity = Mockito.mock(HttpEntity.class);
        Mockito.doReturn(httpEntity).when(response).getEntity();

        InputStream content = new ByteArrayInputStream("blablabla".getBytes());
        Mockito.doReturn(content).when(httpEntity).getContent();

        //For the HttpPost
        Mockito.doReturn(httpUtils).when(instance).getHttpUtils();
        Mockito.doReturn("result").when(httpUtils).executePost(Mockito.any(AbstractHttpClient.class),
                Mockito.any(HttpPost.class), Mockito.any(TequilaResponseHandler.class));
    }

    protected void setUp() throws Exception {
        super.setUp();
        client = Mockito.mock(AbstractHttpClient.class);
        response = Mockito.mock(HttpResponse.class);
        statusLine = Mockito.mock(BasicStatusLine.class);
        task = Mockito.mock(TequilaAuthenticationTask.class);
        httpUtils = Mockito.mock(HttpUtils.class);
        httpEntity = Mockito.mock(HttpEntity.class);
        listener = Mockito.mock(TequilaAuthenticationListener.class);
        globalPreferences = Mockito.mock(GlobalPreferences.class);
        tequilaApi = Mockito.mock(TequilaAuthenticationAPI.class);
        context = getInstrumentation().getTargetContext();
        instance = Mockito.spy(new TequilaAuthenticationTask(context, listener, "test", "test"));

        Mockito.doReturn(client).when(task).getClient();
        Mockito.doReturn(statusLine).when(response).getStatusLine();
        Mockito.doReturn(HTTP_CODE_OK).when(statusLine).getStatusCode();
    }

    //onPreExecute needs to be tested in an UI test
    
    //onCancelled needs to be tested in an UI test
    
    //onPostExecute needs to be tested in an UI test
    
    public final void testGetAccessToIsa() throws IllegalAccessException,
    IllegalArgumentException, InvocationTargetException, NoSuchMethodException, ClientProtocolException, IOException {
        Method getAccessToIsa;
        getAccessToIsa = (TequilaAuthenticationTask.class).getDeclaredMethod("getAccessToIsa",
                new Class[] {String.class, String.class});
        getAccessToIsa.setAccessible(true);

        Mockito.doReturn(httpUtils).when(instance).getHttpUtils();
        Mockito.doReturn(response).when(httpUtils).executeGet(Mockito.any(AbstractHttpClient.class),
                Mockito.any(HttpGet.class), Mockito.any(HttpContext.class));

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
    }

    public final void testSetCookiesAndHeaders() throws NoSuchMethodException,
    IllegalAccessException, IllegalArgumentException, InvocationTargetException {
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
    }
    
    public final void testGetIsaCompleteURL() throws IllegalAccessException,
    IllegalArgumentException, InvocationTargetException, NoSuchMethodException {
        Method getIsaCompleteURL;
        getIsaCompleteURL = (TequilaAuthenticationTask.class).getDeclaredMethod("getIsaCompleteURL",
                new Class[] {String.class});
        getIsaCompleteURL.setAccessible(true);

        //With arg null
        HttpGet result = (HttpGet) getIsaCompleteURL.invoke(instance, new Object[] {null});
        String waitedUrl = TequilaAuthenticationAPI.getInstance().getIsAcademiaLoginURL();
        assertEquals(waitedUrl, result.getURI().toString());

        //With a token
        result = (HttpGet) getIsaCompleteURL.invoke(instance, new Object[] {"token"});
        waitedUrl = TequilaAuthenticationAPI.getInstance().getIsAcademiaLoginURL()+"?"+KEY+"="+"token";
        assertEquals(waitedUrl, result.getURI().toString());
    }
    
    public final void testAuthenticateOnTequila() throws IllegalAccessException,
    IllegalArgumentException, NoSuchMethodException, ClientProtocolException, IOException {
        Method authenticateOnTequila;
        
        try {
            authenticateOnTequila = (TequilaAuthenticationTask.class).getDeclaredMethod("authenticateOnTequila",
                    new Class[] {String.class, boolean.class});
            authenticateOnTequila.setAccessible(true);

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

    public final void testGetTequilaUrl() throws NoSuchMethodException,
    IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Method getTequilaUrl;
        getTequilaUrl = (TequilaAuthenticationTask.class).getDeclaredMethod("getTequilaUrl",
                new Class[] {});
        getTequilaUrl.setAccessible(true);

        HttpPost result = (HttpPost) getTequilaUrl.invoke(instance, new Object[] {});

        assertEquals(TequilaAuthenticationAPI.getInstance().getTequilaAuthenticationURL(),
                result.getURI().toString());
    }

    public final void testsetPostBody() throws IllegalAccessException,
    IllegalArgumentException, InvocationTargetException, NoSuchMethodException {
        Method setPostBody;
        setPostBody = (TequilaAuthenticationTask.class).getDeclaredMethod("setPostBody",
                new Class[] {String.class, boolean.class});
        setPostBody.setAccessible(true);

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
    }

    public final void testSetCookiesForTequila() throws NoSuchMethodException,
    IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Method setCookiesForTequila;
        setCookiesForTequila = (TequilaAuthenticationTask.class).getDeclaredMethod("setCookiesForTequila",
                new Class[] {boolean.class});
        setCookiesForTequila.setAccessible(true);

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
    }

    public final void testStoreCookiesFromTequila() throws NoSuchMethodException,
    IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Method storeCookiesFromTequila;
        storeCookiesFromTequila = (TequilaAuthenticationTask.class).getDeclaredMethod("storeCookiesFromTequila",
                new Class[] {});
        storeCookiesFromTequila.setAccessible(true);

        Cookie cookie = new BasicClientCookie("tequila", "cookie");

        Mockito.doReturn(httpUtils).when(instance).getHttpUtils();
        Mockito.doReturn(cookie).when(httpUtils).getCookie(
                Mockito.any(AbstractHttpClient.class), Mockito.any(String.class));

        storeCookiesFromTequila.invoke(instance, new Object[] {});
        assertEquals(cookie, GlobalPreferences.getInstance().getTequilaKeyCookie());
        assertEquals(cookie, GlobalPreferences.getInstance().getTequilaUsernameCookie());
    }

    public final void testDoInBackgroundVoidArrayWithoutNetworking()
        throws NoSuchMethodException, IllegalAccessException, IllegalArgumentException,
        ClientProtocolException, IOException {
        Method doInBackground;
        try {
            doInBackground = (TequilaAuthenticationTask.class).getDeclaredMethod("doInBackground",
                    Void[].class);
            doInBackground.setAccessible(true);

            Mockito.doReturn(false).when(instance).isNetworkWorking(context);

            doInBackground.invoke(instance, new Object[] {new Void[]{}});
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
        
        //Test when enter into the loop
        try {
            doInBackground = (TequilaAuthenticationTask.class).getDeclaredMethod("doInBackground",
                    Void[].class);
            doInBackground.setAccessible(true);

            setUpForAuthentication();
            Mockito.doReturn(false).when(instance).isNetworkWorking(context);

            doInBackground.invoke(instance, new Object[] {new Void[]{}});
            Mockito.doReturn(false).when(instance).isNetworkWorking(context);
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

    public final void testDoInBackgroundWhenSuccess() throws ClientProtocolException,
    IOException, NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Method doInBackground;
        doInBackground = (TequilaAuthenticationTask.class).getDeclaredMethod("doInBackground",
                Void[].class);
        doInBackground.setAccessible(true);

        setUpForAuthentication();

        InputStream content = new ByteArrayInputStream("blablabla".getBytes());
        Mockito.doReturn(content).when(httpEntity).getContent();

        String resultAuthenticated = (String) doInBackground.invoke(instance, new Object[] {new Void[]{}});
        assertEquals("blablabla", resultAuthenticated);

      //Authenticated = false
        content = new ByteArrayInputStream("blablabla".getBytes());
        Mockito.doReturn(content).when(httpEntity).getContent();
        Mockito.doReturn(false).when(globalPreferences).isAuthenticated(Mockito.any(Context.class));
        String resultNotAuthenticated = (String) doInBackground.invoke(instance, new Object[] {new Void[]{}});
        assertEquals("blablabla", resultNotAuthenticated);
    }

    public final void testDoInBackgroundWhenWrongHttpCode() throws ClientProtocolException,
    IOException, NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Method doInBackground;
        doInBackground = (TequilaAuthenticationTask.class).getDeclaredMethod("doInBackground",
                Void[].class);
        doInBackground.setAccessible(true);

        setUpForAuthentication();

        Mockito.doReturn(HTTP_CODE_NO_FOUND).when(statusLine).getStatusCode();

        String result = (String) doInBackground.invoke(instance, new Object[] {new Void[]{}});
        
        assertEquals(context.getString(R.string.error_http_protocol), result);
    }

    public final void testDoInBackgroundWhenNoTokenInHeader() throws NoSuchMethodException,
    IllegalAccessException, IllegalArgumentException, IOException {
        Method doInBackground;
        String result = "";
        try {
            doInBackground = (TequilaAuthenticationTask.class).getDeclaredMethod("doInBackground",
                    Void[].class);
            doInBackground.setAccessible(true);

            setUpForAuthentication();
            
            Mockito.doReturn(HTTP_CODE_REDIRECT).when(statusLine).getStatusCode();
            Mockito.doReturn(httpUtils).when(instance).getHttpUtils();
            Mockito.doThrow(new ClientProtocolException("error")).when(httpUtils)
                .getTokenFromHeader(Mockito.any(Header.class));
            result = (String) doInBackground.invoke(instance, new Object[] {new Void[]{}});

        } catch (InvocationTargetException e) {
            if (e.getTargetException() instanceof ClientProtocolException) {
                if (e.getTargetException().getMessage().equals("error")) {
                    if (result.equals(context.getString(R.string.error_http_protocol))) {
                        //Waited
                    } else {
                        fail();
                    }
                } else {
                    fail();
                }
            } else {
                fail();
            }
        }
    }
    
    public final void testDoInBackgroundWhenTimeout() throws IllegalAccessException,
    IllegalArgumentException, NoSuchMethodException, IOException {
        Method doInBackground;
        String result = "";
        try {
            doInBackground = (TequilaAuthenticationTask.class).getDeclaredMethod("doInBackground",
                    Void[].class);
            doInBackground.setAccessible(true);

            setUpForAuthentication();
            
            Mockito.doReturn(HTTP_CODE_REDIRECT).when(statusLine).getStatusCode();
            Mockito.doReturn(httpUtils).when(instance).getHttpUtils();
            Mockito.doReturn("token").when(httpUtils)
                .getTokenFromHeader(Mockito.any(Header.class));
            Mockito.doReturn(globalPreferences).when(instance).getGlobalPrefs();
            Cookie cookie = Mockito.mock(Cookie.class);
            Mockito.doReturn(cookie).when(globalPreferences).getSessionIDCookie();
            Mockito.doReturn("sessionID").when(cookie).getValue();

            result = (String) doInBackground.invoke(instance, new Object[] {new Void[]{}});

        } catch (InvocationTargetException e) {
            if (e.getTargetException() instanceof ClientProtocolException) {
                if (e.getTargetException().getMessage().equals("Authentication Timeout")) {
                    if (result.equals(context.getString(R.string.error_http_protocol))) {
                        //Waited
                    } else {
                        fail();
                    }
                } else {
                    fail();
                }
            } else {
                e.printStackTrace();
                fail();
            }
        }
    }
    
    public final void testDoInBackgroundWhenIOException() throws ClientProtocolException,
    IOException, NoSuchMethodException, IllegalAccessException, IllegalArgumentException {
        Method doInBackground;
        String result = "";
        try {
            doInBackground = (TequilaAuthenticationTask.class).getDeclaredMethod("doInBackground",
                    Void[].class);
            doInBackground.setAccessible(true);

            setUpForAuthentication();

            Mockito.doThrow(new IOException("error")).when(httpEntity).getContent();

            result = (String) doInBackground.invoke(instance, new Object[] {new Void[]{}});

        } catch (InvocationTargetException e) {
            if (e.getTargetException() instanceof IOException) {
                if (e.getTargetException().getMessage().equals("error")) {
                    if (result.equals(context.getString(R.string.error_http_protocol))) {
                        //Waited
                    } else {
                        fail();
                    }
                } else {
                    fail();
                }
            } else {
                e.printStackTrace();
                fail();
            }
        }
    }
}

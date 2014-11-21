package ch.epfl.calendar.authentication.tests;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Calendar;
import java.util.GregorianCalendar;

import android.content.Context;
import android.test.ActivityInstrumentationTestCase2;

import ch.epfl.calendar.MainActivity;
import ch.epfl.calendar.authentication.TequilaAuthenticationAPI;

/**
 * This class tests the TequilaAuthenticationAPI class
 * @author AblionGE
 *
 */
public class TequilaAuthenticationAPITest
    extends ActivityInstrumentationTestCase2<MainActivity> {
    
    public TequilaAuthenticationAPITest() {
        super(MainActivity.class);
    }

    private static final int YEAR_2014 = 2014;
    private static final int JANUAR_MONTH = 1;
    private static final int OCTOBER_MONTH = 10;
    private static final int DAY_1 = 1;
    
    private TequilaAuthenticationAPI tequilaApi;
    
    protected void setUp() throws Exception {
        tequilaApi = null;
        super.setUp();
    }
    
    public final void testGetInstance() {
        
        assertNull(tequilaApi);
        //First get
        tequilaApi = TequilaAuthenticationAPI.getInstance();
        assertEquals(tequilaApi.getClass(), TequilaAuthenticationAPI.class);
        
        //Second get
        TequilaAuthenticationAPI secondTequilaApi = TequilaAuthenticationAPI.getInstance();
        assertEquals(secondTequilaApi, tequilaApi);
    }
    
    public final void testCalculateDate()
        throws NoSuchMethodException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        Method calculateDate =
                (TequilaAuthenticationAPI.class).getDeclaredMethod("calculateDate", new Class[] {Calendar.class});
        calculateDate.setAccessible(true);
        
        Calendar firstDate = new GregorianCalendar(YEAR_2014, JANUAR_MONTH-1, DAY_1);
        Calendar secondDate = new GregorianCalendar(YEAR_2014, OCTOBER_MONTH-1, DAY_1);

        TequilaAuthenticationAPI instance = TequilaAuthenticationAPI.getInstance();
        String result = (String) calculateDate.invoke(instance, new Object[] {firstDate});
        assertNotNull(result);
        assertEquals("from=31.8.2013&to=31.8.2014", result);

        result = (String) calculateDate.invoke(instance, new Object[] {secondDate});
        assertNotNull(result);
        assertEquals("from=31.8.2014&to=31.8.2015", result);
    }
    
    public final void testClearStoredData() {
        Context context = getInstrumentation().getTargetContext();
        tequilaApi = TequilaAuthenticationAPI.getInstance();
        tequilaApi.clearStoredData(context);
        String username = tequilaApi.getUsername(context);
        String tequilaKey = tequilaApi.getTequilaKey(context);
        String sessionID = tequilaApi.getSessionID(context);
        assertEquals("", username);
        assertEquals("", tequilaKey);
        assertEquals("", sessionID);
    }
    
    public final void testGetAndSetSessionID() {
        String sessionID = "1234";
        Context context = getInstrumentation().getTargetContext();
        tequilaApi = TequilaAuthenticationAPI.getInstance();
        tequilaApi.setSessionID(context, sessionID);
        assertEquals("1234", tequilaApi.getSessionID(context));
    }
    
    public final void testClearSessionID() {
        Context context = getInstrumentation().getTargetContext();
        tequilaApi = TequilaAuthenticationAPI.getInstance();
        tequilaApi.clearSessionID(context);
        String result = tequilaApi.getSessionID(context);
        assertEquals("", result);
    }
    
    public final void testGetAndSetUsername() {
        Context context = getInstrumentation().getTargetContext();
        String username = "username";
        tequilaApi = TequilaAuthenticationAPI.getInstance();
        tequilaApi.setUsername(context, username);
        String result = tequilaApi.getUsername(context);
        assertEquals("username", result);
    }
    
    public final void testClearUsername() {
        Context context = getInstrumentation().getTargetContext();
        tequilaApi = TequilaAuthenticationAPI.getInstance();
        tequilaApi.clearUsername(context);
        String result = tequilaApi.getUsername(context);
        assertEquals("", result);
    }
    
    public final void testGetAndSetTequilaKey() {
        Context context = getInstrumentation().getTargetContext();
        String tequilaKey = "key";
        tequilaApi = TequilaAuthenticationAPI.getInstance();
        tequilaApi.setTequilaKey(context, tequilaKey);
        String result = tequilaApi.getTequilaKey(context);
        assertEquals("key", result);
    }
    
    public final void testClearTequilaKey() {
        Context context = getInstrumentation().getTargetContext();
        tequilaApi = TequilaAuthenticationAPI.getInstance();
        tequilaApi.clearTequilaKey(context);
        String result = tequilaApi.getTequilaKey(context);
        assertEquals("", result);
    }
    
    public final void testGetIsAcademiaLoginURL()
        throws NoSuchMethodException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        tequilaApi = TequilaAuthenticationAPI.getInstance();
        String result = tequilaApi.getIsAcademiaLoginURL();
        
        Method calculateDate =
                (TequilaAuthenticationAPI.class).getDeclaredMethod("calculateDate", new Class[] {Calendar.class});
        calculateDate.setAccessible(true);

        TequilaAuthenticationAPI instance = TequilaAuthenticationAPI.getInstance();
        String period = (String) calculateDate.invoke(instance, new Object[] {new GregorianCalendar()});
        
        assertEquals("https://isa.epfl.ch/service/secure/student/timetable/period?" + period,
                result);
    }
    
    public final void testGetTequilaAuthenticationURL() {
        tequilaApi = TequilaAuthenticationAPI.getInstance();
        String expected = "https://tequila.epfl.ch/cgi-bin/tequila/login";
        String result = tequilaApi.getTequilaAuthenticationURL();
        assertEquals(expected, result);
    }
    
}

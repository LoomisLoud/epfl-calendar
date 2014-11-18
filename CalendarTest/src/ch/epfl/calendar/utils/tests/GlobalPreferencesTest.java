/**
 * 
 */
package ch.epfl.calendar.utils.tests;

import org.mockito.Mockito;

import android.content.Context;
import ch.epfl.calendar.authentication.TequilaAuthenticationAPI;
import ch.epfl.calendar.testing.utils.MockTestCase;
import ch.epfl.calendar.utils.GlobalPreferences;

/**
 * @author gilbrechbuhler
 *
 */
public class GlobalPreferencesTest extends MockTestCase {
    private TequilaAuthenticationAPI tequilaApi = null;
    private GlobalPreferences globalPrefs = null;
    private Context context;
    
    private static final String SESSION_ID = "a1b2c3";
    private static final String SESSION_ID_EMPTY = "";
    
    public void setUp() throws Exception {
        super.setUp();
        tequilaApi = Mockito.mock(TequilaAuthenticationAPI.class);
        globalPrefs = Mockito.spy(new GlobalPreferences());
        context = getInstrumentation().getTargetContext();
    }
    
    public void testIsAuthenticatedWorking() {
        Mockito.doReturn(tequilaApi).when(globalPrefs).getTequilaAPI();
        Mockito.doReturn(SESSION_ID).when(tequilaApi).getSessionID(context);
        
        boolean isAuth = globalPrefs.isAuthenticated(context);
        assertEquals(true, isAuth);
    }
    
    public void testIsAuthenticatedWhenSessionIDEmpty() {
        Mockito.doReturn(tequilaApi).when(globalPrefs).getTequilaAPI();
        Mockito.doReturn(SESSION_ID_EMPTY).when(tequilaApi).getSessionID(context);
        
        boolean isAuth = globalPrefs.isAuthenticated(context);
        assertEquals(false, isAuth);
    }
    
    public void testIsAuthenticatedWhenSessionIDNull() {
        Mockito.doReturn(tequilaApi).when(globalPrefs).getTequilaAPI();
        Mockito.doReturn(null).when(tequilaApi).getSessionID(context);
        
        boolean isAuth = globalPrefs.isAuthenticated(context);
        assertEquals(false, isAuth);
    }
}

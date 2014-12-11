/**
 * 
 */
package ch.epfl.calendar.test.utils;

import org.mockito.Mockito;

import android.content.Context;
import ch.epfl.calendar.authentication.TequilaAuthenticationAPI;
import ch.epfl.calendar.utils.AuthenticationUtils;

/**
 * @author gilbrechbuhler
 *
 */
public class AuthenticationUtilsTest extends MockTestCase {
    private TequilaAuthenticationAPI tequilaApi = null;
    private AuthenticationUtils authUtils = null;
    private Context context;
    
    private static final String SESSION_ID = "a1b2c3";
    private static final String SESSION_ID_EMPTY = "";
    
    public void setUp() throws Exception {
        super.setUp();
        tequilaApi = Mockito.mock(TequilaAuthenticationAPI.class);
        authUtils = Mockito.spy(new AuthenticationUtils());
        context = getInstrumentation().getTargetContext();
    }
    
    public void testIsAuthenticatedWorking() {
        Mockito.doReturn(tequilaApi).when(authUtils).getTequilaAPI();
        Mockito.doReturn(SESSION_ID).when(tequilaApi).getSessionID(context);
        
        boolean isAuth = authUtils.isAuthenticated(context);
        assertEquals(true, isAuth);
    }
    
    public void testIsAuthenticatedWhenSessionIDEmpty() {
        Mockito.doReturn(tequilaApi).when(authUtils).getTequilaAPI();
        Mockito.doReturn(SESSION_ID_EMPTY).when(tequilaApi).getSessionID(context);
        
        boolean isAuth = authUtils.isAuthenticated(context);
        assertEquals(false, isAuth);
    }
    
    public void testIsAuthenticatedWhenSessionIDNull() {
        Mockito.doReturn(tequilaApi).when(authUtils).getTequilaAPI();
        Mockito.doReturn(null).when(tequilaApi).getSessionID(context);
        
        boolean isAuth = authUtils.isAuthenticated(context);
        assertEquals(false, isAuth);
    }
    
    public void testGetTequilaAPI() {
        Mockito.doReturn(tequilaApi).when(authUtils).getTequilaAPI();
        assertEquals(tequilaApi, authUtils.getTequilaAPI());
    }
}

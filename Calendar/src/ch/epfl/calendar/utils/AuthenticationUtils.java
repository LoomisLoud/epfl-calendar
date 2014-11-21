/**
 * 
 */
package ch.epfl.calendar.utils;

import android.content.Context;
import ch.epfl.calendar.authentication.TequilaAuthenticationAPI;

/**
 * @author gilbrechbuhler
 *
 */
public class AuthenticationUtils {
    
    /**
     * 
     * @param context
     * @return true if the user is authenticated, false otherwise.
     */
    public boolean isAuthenticated(Context context) {
        String tokenID = getTequilaAPI().getSessionID(context);
        return (tokenID != null) && !tokenID.isEmpty();
    }
    
    /**
     * 
     * @return a TequilaAPI instance.
     */
    public TequilaAuthenticationAPI getTequilaAPI() {
        return TequilaAuthenticationAPI.getInstance();
    }
}

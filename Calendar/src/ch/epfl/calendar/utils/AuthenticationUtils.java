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
    
    public boolean isAuthenticated(Context context) {
        String tokenID = getTequilaAPI().getSessionID(context);
        return (tokenID != null) && !tokenID.isEmpty();
    }
    
    public TequilaAuthenticationAPI getTequilaAPI() {
        return TequilaAuthenticationAPI.getInstance();
    }
}

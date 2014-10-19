package ch.epfl.calendar.utils;

import android.content.Context;
import ch.epfl.calendar.authentication.TequilaAuthenticationAPI;

/**
 * Serves as a place for everyone to have access to the preferences of the app
 * @author lweingart
 *
 */
public class GlobalPreferences {

    private static GlobalPreferences mInstance;

    public static GlobalPreferences getInstance() {
        if (mInstance == null) {
            mInstance = new GlobalPreferences();
        }
        return mInstance;
    }

    public static boolean isAuthenticated(Context context) {
        String tokenID = TequilaAuthenticationAPI.getInstance().getSessionID(context);
        return (tokenID != null) && !tokenID.isEmpty();
    }

}

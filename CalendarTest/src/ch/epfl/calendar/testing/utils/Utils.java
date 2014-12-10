package ch.epfl.calendar.testing.utils;

import android.app.Activity;

/**
 * 
 * @author AblionGE
 *
 */
public class Utils {
    
    public static void pressBack(Activity activity) {
        for (int i = 0; i < 3 ; i++) {
            activity.onBackPressed();
        }
    }
}

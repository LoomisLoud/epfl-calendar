/**
 * 
 */
package ch.epfl.calendar.utils;

/**
 * @author gilbrechbuhler
 *
 */
public class UniqueInstance {

    private static final GlobalPreferences globalPrefs = new GlobalPreferences();

    public static GlobalPreferences getGlobalPrefsInstance() {
        return globalPrefs;
    }
    
    
}

/**
 * 
 */
package ch.epfl.calendar.utils;

/**
 * @author gilbrechbuhler
 *
 */
public class UniqueInstance {

    private static final GlobalPreferences GLOBAL_PREFS = new GlobalPreferences();

    public static GlobalPreferences getGlobalPrefsInstance() {
        return GLOBAL_PREFS;
    }
    
    
}

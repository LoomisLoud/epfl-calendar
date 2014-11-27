/**
 * 
 */
package ch.epfl.calendar.testing.utils;

import java.lang.reflect.Field;

/**
 * @author gilbrechbuhler
 *
 */
public class TestsHelper {

    public static void setFinalStatic(Field field, Object newValue) throws Exception {
        field.setAccessible(true);

        field.set(null, newValue);
    }
}

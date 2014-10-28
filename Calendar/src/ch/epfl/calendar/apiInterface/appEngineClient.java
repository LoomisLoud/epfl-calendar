/**
 * 
 */
package ch.epfl.calendar.apiInterface;

import ch.epfl.calendar.data.Course;

/**
 * @author gilbrechbuhler
 *
 */
public class appEngineClient implements databaseInterface {
    
    private static final String DB_URL = "http://localhost:8080";

    /* (non-Javadoc)
     * @see ch.epfl.calendar.apiInterface.databaseInterface#getCourseByName(java.lang.String)
     */
    @Override
    public Course getCourseByName(String name) {
        
        return null;
    }

    /* (non-Javadoc)
     * @see ch.epfl.calendar.apiInterface.databaseInterface#getCourseByCode(java.lang.String)
     */
    @Override
    public Course getCourseByCode(String code) {
        
        return null;
    }

}

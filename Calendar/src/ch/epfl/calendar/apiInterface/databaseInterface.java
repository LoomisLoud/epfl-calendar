/**
 * 
 */
package ch.epfl.calendar.apiInterface;

import ch.epfl.calendar.data.Course;

/**
 * This interface is used to access the database in a transparent way.
 * 
 * @author gilbrechbuhler
 *
 */
public interface databaseInterface {

    /**
     * 
     * @param name The name of the course to fetch
     * @return The Course object filled in with the course's informations.
     */
    Course getCourseByName(String name);
    
    /**
     * 
     * @param code The code of the course to fetch
     * @return The course Object filled in with the course's informations
     */
    Course getCourseByCode(String code);
}

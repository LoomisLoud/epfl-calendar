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
public interface DatabaseInterface {

    /**
     * 
     * @param name The name of the course to fetch
     * @return The Course object filled in with the course's informations.
     * @throws CalendarClientException 
     */
    Course getCourseByName(String name) throws CalendarClientException;
    
    /**
     * 
     * @param code The code of the course to fetch
     * @return The course Object filled in with the course's informations
     * @throws CalendarClientException 
     */
    Course getCourseByCode(String code) throws CalendarClientException;
    
    /**
     * 
     * @param period The period to put in the database
     * @param courseCode the code of the course of which the period belongs.
     * @throws CalendarClientException
     */
    //void createPeriod(Period period, String courseCode) throws CalendarClientException;
}

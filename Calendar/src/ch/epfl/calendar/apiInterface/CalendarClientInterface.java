/**
 * 
 */
package ch.epfl.calendar.apiInterface;

import java.util.List;

import ch.epfl.calendar.mock.MockCourse;
import ch.epfl.calendar.mock.MockStudent;

/**
 * A client to get student's and classes' informations. This interfaces is meant to abstract the underlying
 * network protocol and data formats.
 * 
 * Every method listed in this class is temporary. These temporary methods will be modified when we now how things
 * work with ISA.
 * 
 * @author gilbrechbuhler
 *
 */
public interface CalendarClientInterface {
    
    /**
     * 
     * @param username
     * @param password
     * @return
     */
    void login(String username, String password);
    
    /**
     * 
     * @param student
     * @return
     */
    List<MockCourse> getCoursesFromStudent(MockStudent student);
}

/**
 * 
 */
package ch.epfl.calendar;

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
public interface CalendarClient {
    
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
    List<Lecture> fetchLectures(Student student);
}

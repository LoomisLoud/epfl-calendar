/**
 *
 */
package ch.epfl.calendar.apiInterface;

import java.util.List;

import ch.epfl.calendar.authentication.TequilaAuthenticationException;
import ch.epfl.calendar.data.Course;

/**
 * A client to get student's and classes' informations. This interfaces is meant to abstract the underlying
 * network protocol and data formats.
 *
 * @author gilbrechbuhler
 *
 */
public interface CalendarClientInterface {

    /**
     *
     * @param student
     * @return a list of courses
     */
    List<Course> getISAInformations() throws CalendarClientException, TequilaAuthenticationException;
}
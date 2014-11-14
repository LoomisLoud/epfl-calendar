/**
 *
 */
package ch.epfl.calendar.apiInterface;

/**
 * A client to get student's periods informations. This interfaces is meant to abstract the underlying
 * network protocol and data formats.
 *
 * @author gilbrechbuhler
 *
 */
public interface CalendarClientInterface {

    /**
     * This method gets informations obtained at https://isa.epfl.ch/service/
     *
     * @param student
     * @return a list of courses
     */
    void getISAInformations();
}
